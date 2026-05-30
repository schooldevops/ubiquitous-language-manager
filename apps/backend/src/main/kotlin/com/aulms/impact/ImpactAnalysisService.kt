package com.aulms.impact

import com.aulms.graph.GraphNode
import com.aulms.graph.GraphSyncWorker
import com.aulms.model.ImpactAnalysisResponse
import com.aulms.model.ImpactChangeType
import com.aulms.model.ImpactRecommendation
import com.aulms.model.ImpactRiskLevel
import com.aulms.model.ImpactTarget
import com.aulms.term.TermRepository
import org.springframework.stereotype.Service

@Service
class ImpactAnalysisService(
    private val termRepository: TermRepository,
    private val graphSyncWorker: GraphSyncWorker,
) {
    fun getTermImpact(
        termId: String,
        requestedChangeType: ImpactChangeType?,
        includeTwoHop: Boolean,
    ): ImpactAnalysisResponse {
        val term = termRepository.get(termId)
        val changeType = requestedChangeType ?: ImpactChangeType.DESCRIPTION_UPDATE
        val targets = impactedTargets(termId, term.domainName, term.koreanName, includeTwoHop)
        val riskScore = riskScore(changeType, targets)
        return ImpactAnalysisResponse(
            termId = termId,
            standardTerm = term.koreanName,
            changeType = changeType,
            includeTwoHop = includeTwoHop,
            riskLevel = riskLevel(riskScore),
            riskScore = riskScore,
            impactedTargets = targets,
            recommendations = recommendations(changeType, includeTwoHop),
        )
    }

    private fun impactedTargets(
        termId: String,
        domainName: String,
        standardTerm: String,
        includeTwoHop: Boolean,
    ): List<ImpactTarget> {
        val snapshot = graphSyncWorker.buildSnapshot()
        val nodesByKey = snapshot.nodes.associateBy { it.nodeKey }
        val termNodeKey = "term:$termId"
        val directExpressionTargets = snapshot.edges
            .filter { it.relationshipType == "represents" && it.toNodeKey == termNodeKey }
            .mapNotNull { edge -> nodesByKey[edge.fromNodeKey]?.toImpactTarget(standardTerm) }

        val directTargets = listOf(
            ImpactTarget(
                targetType = ImpactTarget.TargetType.SYSTEM,
                targetName = "DICTIONARY",
                systemCode = "DICTIONARY",
                location = "TERM_MASTER.$termId",
                hop = 1,
                reason = "$standardTerm 용어를 관리하는 기준 시스템",
            ),
            ImpactTarget(
                targetType = ImpactTarget.TargetType.DB_TABLE,
                targetName = "${domainName.uppercase()}_MASTER",
                systemCode = "DICTIONARY",
                location = "DICTIONARY.${domainName.uppercase()}_MASTER",
                hop = 1,
                reason = "$standardTerm 용어가 속한 $domainName 도메인 대표 테이블",
            ),
            ImpactTarget(
                targetType = ImpactTarget.TargetType.API,
                targetName = "/${domainName.lowercase()}/search",
                systemCode = "DICTIONARY",
                location = "OpenAPI.paths.${domainName.lowercase()}Search",
                hop = 1,
                reason = "$standardTerm 용어를 조회 조건 또는 응답 필드로 사용할 수 있는 API",
            ),
        ) + directExpressionTargets

        val expandedTargets = if (includeTwoHop) {
            listOf(
                ImpactTarget(
                    targetType = ImpactTarget.TargetType.DOCUMENT,
                    targetName = "$standardTerm 기획서 표현",
                    systemCode = "PLANNING",
                    location = "docs/planning/${termId.lowercase()}-requirements.md",
                    hop = 2,
                    reason = "$standardTerm 변경 시 기획서 용어 매핑표와 요구사항 문장 확인 필요",
                ),
                ImpactTarget(
                    targetType = ImpactTarget.TargetType.TEST_CASE,
                    targetName = "$standardTerm 필수 검증",
                    systemCode = "QA",
                    location = "tests/${termId.lowercase()}-required.feature",
                    hop = 2,
                    reason = "$standardTerm 변경 시 Given/When/Then 테스트 용어와 오류 메시지 확인 필요",
                ),
            )
        } else {
            emptyList()
        }

        return (directTargets + expandedTargets)
            .distinctBy { "${it.targetType.value}:${it.targetName}:${it.location}" }
            .sortedWith(compareBy<ImpactTarget> { it.hop }.thenBy { it.targetType.value }.thenBy { it.targetName })
    }

    private fun GraphNode.toImpactTarget(standardTerm: String): ImpactTarget? {
        val targetType = when (nodeType) {
            "Column" -> ImpactTarget.TargetType.DB_COLUMN
            "APIField" -> ImpactTarget.TargetType.API_FIELD
            "DTOField" -> ImpactTarget.TargetType.DTO
            else -> return null
        }
        val name = properties["columnName"] ?: properties["fieldName"] ?: properties["expressionValue"] ?: nodeKey
        return ImpactTarget(
            targetType = targetType,
            targetName = name,
            systemCode = "DICTIONARY",
            location = nodeKey,
            hop = 1,
            reason = "$name 표현이 표준 용어 $standardTerm 를 참조함",
        )
    }

    private fun riskScore(changeType: ImpactChangeType, targets: List<ImpactTarget>): Int {
        val base = when (changeType) {
            ImpactChangeType.DESCRIPTION_UPDATE -> 15
            ImpactChangeType.ALIAS_ADD -> 25
            ImpactChangeType.API_FIELD_RENAME -> 55
            ImpactChangeType.DB_COLUMN_RENAME -> 70
            ImpactChangeType.PHYSICAL_TYPE_CHANGE -> 85
            ImpactChangeType.DIGITS_CHANGE -> 75
            ImpactChangeType.DEPRECATE_TERM -> 80
        }
        return (base + (targets.size * 4)).coerceAtMost(100)
    }

    private fun riskLevel(score: Int): ImpactRiskLevel = when {
        score >= 70 -> ImpactRiskLevel.HIGH
        score >= 31 -> ImpactRiskLevel.MEDIUM
        else -> ImpactRiskLevel.LOW
    }

    private fun recommendations(changeType: ImpactChangeType, includeTwoHop: Boolean): List<ImpactRecommendation> {
        val primary = when (changeType) {
            ImpactChangeType.API_FIELD_RENAME -> ImpactRecommendation(
                priority = 1,
                action = "OpenAPI v2 필드와 하위 호환 기간을 정의한다",
                reason = "API 필드명 변경은 외부 계약과 프런트엔드 클라이언트에 직접 영향이 있음",
            )
            ImpactChangeType.DB_COLUMN_RENAME -> ImpactRecommendation(
                priority = 1,
                action = "DB 마이그레이션, 뷰 또는 alias 컬럼 전략을 먼저 확정한다",
                reason = "DB 컬럼명 변경은 SQL, Entity, 배치, 리포트까지 연쇄 영향이 큼",
            )
            ImpactChangeType.PHYSICAL_TYPE_CHANGE, ImpactChangeType.DIGITS_CHANGE -> ImpactRecommendation(
                priority = 1,
                action = "데이터 변환 가능성과 기존 값 분포를 검증한다",
                reason = "물리 타입과 자릿수 변경은 데이터 손실 또는 검증 오류를 만들 수 있음",
            )
            ImpactChangeType.DEPRECATE_TERM -> ImpactRecommendation(
                priority = 1,
                action = "대체 표준 용어와 폐기 일정을 함께 공지한다",
                reason = "폐기 용어는 기획서, 코드, 테스트의 단계적 마이그레이션이 필요함",
            )
            else -> ImpactRecommendation(
                priority = 1,
                action = "용어 정의 변경 사항을 데이터 사전 변경 이력에 기록한다",
                reason = "낮은 위험 변경도 AI/RAG 인덱스와 문서 설명에는 반영되어야 함",
            )
        }
        val secondary = ImpactRecommendation(
            priority = 2,
            action = "OpenAPI, DTO, 테스트, 기획서 용어를 함께 검토한다",
            reason = "유비쿼터스 랭기지는 산출물 간 표현 일관성이 핵심임",
        )
        val graph = ImpactRecommendation(
            priority = 3,
            action = "변경 승인 후 RAG와 Graphify 인덱스를 재생성한다",
            reason = if (includeTwoHop) {
                "2-hop 영향 대상까지 확장했으므로 관계 검색 결과도 최신화해야 함"
            } else {
                "직접 영향 대상만 확인했으므로 배포 전 관계 인덱스 갱신이 필요함"
            },
        )
        return listOf(primary, secondary, graph)
    }
}
