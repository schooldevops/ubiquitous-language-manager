package com.aulms.term

import com.aulms.model.AliasType
import com.aulms.model.ExpressionType
import com.aulms.model.PageMetadata
import com.aulms.model.RelationshipType
import com.aulms.model.Term
import com.aulms.model.TermAlias
import com.aulms.model.TermChangeHistory
import com.aulms.model.TermExpression
import com.aulms.model.TermListResponse
import com.aulms.model.TermStatus
import com.aulms.model.TermSummary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.concurrent.atomic.AtomicLong

@Repository
@Profile("!postgres")
class InMemoryTermRepository : TermRepository {
    private val terms = linkedMapOf<String, TermRecord>()
    private val relationships = mutableListOf<TermRelationshipRecord>()
    private val histories = mutableListOf<TermChangeHistory>()
    private val termSequence = AtomicLong(18)
    private val expressionSequence = AtomicLong(36)
    private val aliasSequence = AtomicLong(13)
    private val historySequence = AtomicLong(1)

    init {
        seed()
    }

    override fun list(q: String?, domainName: String?, status: TermStatus?, page: Int, size: Int): TermListResponse {
        val filtered = terms.values
            .filter { domainName == null || it.term.domainName == domainName }
            .filter { status == null || it.term.status == status }
            .filter { q.isNullOrBlank() || it.matches(q) }
        val fromIndex = (page * size).coerceAtMost(filtered.size)
        val toIndex = (fromIndex + size).coerceAtMost(filtered.size)
        val items = filtered.subList(fromIndex, toIndex).map { it.toSummary() }
        val totalPages = if (filtered.isEmpty()) 0 else ((filtered.size - 1) / size) + 1
        return TermListResponse(
            items = items,
            page = PageMetadata(page = page, propertySize = size, totalElements = filtered.size.toLong(), totalPages = totalPages),
        )
    }

    override fun get(termId: String): Term = record(termId).toTerm()

    override fun create(command: TermCommand): Term {
        if (terms.values.any { it.term.koreanName == command.koreanName || it.term.englishAbbreviation == command.englishAbbreviation }) {
            throw TermConflictException("Term already exists: ${command.koreanName}/${command.englishAbbreviation}")
        }
        val next = termSequence.getAndIncrement()
        val termId = "T-%06d".format(next)
        val now = OffsetDateTime.now()
        val term = command.toTerm(termId = termId, termNumber = "TERM-%06d".format(next), now = now)
        terms[termId] = TermRecord(term = term, expressions = mutableListOf(), aliases = mutableListOf())
        addHistory(termId, "CREATE", null, term.status, "표준 용어 등록", null, null)
        return get(termId)
    }

    override fun update(termId: String, command: TermCommand, version: String, reason: String): Term {
        val existing = record(termId)
        val now = OffsetDateTime.now()
        val updated = command.toTerm(termId = termId, termNumber = existing.term.termNumber, version = version, now = now)
        terms[termId] = existing.copy(term = updated)
        addHistory(termId, "UPDATE", existing.term.status, updated.status, reason, null, null)
        return get(termId)
    }

    override fun approve(termId: String, approver: String, reason: String): Term {
        val existing = record(termId)
        val updated = existing.term.copy(status = TermStatus.Approved, updatedAt = OffsetDateTime.now())
        terms[termId] = existing.copy(term = updated)
        addHistory(termId, "APPROVE", existing.term.status, updated.status, reason, null, approver)
        return get(termId)
    }

    override fun deprecate(termId: String, approver: String, replacementTermId: String, reason: String, impactAnalysisId: String?): Term {
        record(replacementTermId)
        val existing = record(termId)
        val updated = existing.term.copy(status = TermStatus.Deprecated, updatedAt = OffsetDateTime.now())
        terms[termId] = existing.copy(term = updated)
        addHistory(termId, "DEPRECATE", existing.term.status, updated.status, reason, null, approver, impactAnalysisId)
        return get(termId)
    }

    override fun listExpressions(termId: String): List<TermExpression> = record(termId).expressions.toList()

    override fun addExpression(termId: String, expressionType: ExpressionType, expressionValue: String, language: String?, style: String?, standard: Boolean): TermExpression {
        val record = record(termId)
        val expression = TermExpression(
            expressionId = expressionSequence.getAndIncrement(),
            termId = termId,
            expressionType = expressionType,
            expressionValue = expressionValue,
            isStandard = standard,
            language = language,
            style = style,
        )
        record.expressions.add(expression)
        addHistory(termId, "ADD_EXPRESSION", record.term.status, record.term.status, "표현 매핑 등록: $expressionValue", null, null)
        return expression
    }

    override fun deleteExpressions(termId: String) {
        record(termId).expressions.clear()
    }

    override fun listAliases(termId: String): List<TermAlias> = record(termId).aliases.toList()

    override fun addAlias(termId: String, aliasName: String, aliasType: AliasType, recommendationAction: String, reason: String): TermAlias {
        val record = record(termId)
        val alias = TermAlias(
            aliasId = "A-%06d".format(aliasSequence.getAndIncrement()),
            termId = termId,
            aliasName = aliasName,
            aliasType = aliasType,
            recommendationAction = recommendationAction,
            reason = reason,
        )
        record.aliases.add(alias)
        addHistory(termId, "ADD_ALIAS", record.term.status, record.term.status, "별칭 등록: $aliasName", null, null)
        return alias
    }

    override fun deleteAliases(termId: String) {
        record(termId).aliases.clear()
    }

    override fun searchDocuments(): List<TermSearchDocument> = terms.values.map { it.toSearchDocument() }

    override fun relationshipDocuments(): List<TermRelationshipRecord> = relationships.toList()

    override fun listHistory(termId: String, page: Int, size: Int): Pair<List<TermChangeHistory>, PageMetadata> {
        record(termId)
        val filtered = histories.filter { it.termId == termId }
        val fromIndex = (page * size).coerceAtMost(filtered.size)
        val toIndex = (fromIndex + size).coerceAtMost(filtered.size)
        val totalPages = if (filtered.isEmpty()) 0 else ((filtered.size - 1) / size) + 1
        return filtered.subList(fromIndex, toIndex) to PageMetadata(page, size, filtered.size.toLong(), totalPages)
    }

    private fun record(termId: String): TermRecord = terms[termId] ?: throw TermNotFoundException(termId)

    private fun addHistory(
        termId: String,
        changeType: String,
        previousStatus: TermStatus?,
        newStatus: TermStatus?,
        reason: String,
        requestedBy: String?,
        approvedBy: String?,
        impactAnalysisId: String? = null,
    ) {
        histories.add(
            TermChangeHistory(
                changeHistoryId = historySequence.getAndIncrement(),
                changeType = changeType,
                reason = reason,
                createdAt = OffsetDateTime.now(),
                termId = termId,
                previousStatus = previousStatus,
                newStatus = newStatus,
                requestedBy = requestedBy,
                approvedBy = approvedBy,
                impactAnalysisId = impactAnalysisId,
            ),
        )
    }

    private fun seed() {
        val now = OffsetDateTime.now()
        addSeedTerm("T-000001", "TERM-000001", "고객", "고객번호", "Customer Number", "CUST_NO", "회사에서 고객을 업무적으로 식별하기 위해 사용하는 번호", "주문, 계약, 청구, 상담 등에서 고객 식별 기준으로 사용", "VARCHAR", 20, 0, "고객도메인 데이터스튜어드", now)
        addSeedTerm("T-000002", "TERM-000002", "고객", "고객명", "Customer Name", "CUST_NM", "고객의 이름", "고객 조회, 상담, 주문 상세 화면에서 고객을 표시할 때 사용", "VARCHAR", 100, 0, "고객도메인 데이터스튜어드", now)
        addSeedTerm("T-000003", "TERM-000003", "고객", "고객상태코드", "Customer Status Code", "CUST_STS_CD", "고객의 업무 상태를 분류하는 코드", "정상, 휴면, 탈퇴 등 고객 상태 판단에 사용", "VARCHAR", 10, 0, "고객도메인 데이터스튜어드", now)
        addSeedTerm("T-000004", "TERM-000004", "주문", "주문번호", "Order Number", "ORD_NO", "주문을 업무적으로 식별하기 위해 사용하는 번호", "주문 조회, 결제, 배송, 상담에서 주문 식별 기준으로 사용", "VARCHAR", 20, 0, "주문도메인 데이터스튜어드", now)
        addSeedTerm("T-000005", "TERM-000005", "주문", "주문일자", "Order Date", "ORD_DT", "주문이 발생한 날짜", "주문 목록, 매출 집계, 주문 검색 조건에서 날짜 단위로 사용", "DATE", 8, 0, "주문도메인 데이터스튜어드", now)
        addSeedTerm("T-000006", "TERM-000006", "주문", "주문일시", "Order Date Time", "ORD_DTTM", "주문이 발생한 날짜와 시각", "주문 생성 시점, 주문 처리 순서, 로그 추적에서 시분초 단위로 사용", "TIMESTAMP", 14, 0, "주문도메인 데이터스튜어드", now)
        addSeedTerm("T-000007", "TERM-000007", "주문", "주문금액", "Order Amount", "ORD_AMT", "주문에 대해 산정된 금액", "주문 목록, 결제 요청, 매출 집계에서 사용", "NUMERIC", 15, 2, "주문도메인 데이터스튜어드", now)
        addSeedTerm("T-000008", "TERM-000008", "주문", "주문상태코드", "Order Status Code", "ORD_STS_CD", "주문의 처리 상태를 분류하는 코드", "접수, 결제완료, 취소, 배송중 등 주문 상태 판단에 사용", "VARCHAR", 10, 0, "주문도메인 데이터스튜어드", now)
        addSeedTerm("T-000009", "TERM-000009", "주문", "주문목록", "Order List", "ORD_LIST", "조회 조건에 따라 반환되는 주문의 목록", "주문 조회 화면과 주문 검색 API 응답에서 사용", "VARCHAR", 0, 0, "주문도메인 데이터스튜어드", now)
        val customer = record("T-000001")
        customer.aliases.addAll(
            listOf(
                TermAlias("A-000001", "T-000001", "고객ID", AliasType.Synonym, "고객번호로 변환 권장", "업무 고객 식별 번호 의미로 사용되는 경우 표준 용어는 고객번호"),
                TermAlias("A-000004", "T-000001", "customerId", AliasType.Synonym, "customerNumber로 변환 권장", "API 표준 필드명은 customerNumber"),
                TermAlias("A-000006", "T-000001", "CUST_ID", AliasType.Forbidden, "CUST_NO 사용", "기술 ID와 업무 고객번호가 혼동될 수 있음"),
            ),
        )
        val orderList = record("T-000009")
        orderList.aliases.add(
            TermAlias("A-000013", "T-000009", "주문 리스트", AliasType.Synonym, "주문목록으로 변환 권장", "기획서와 화면 용어는 주문목록을 표준 표현으로 사용"),
        )
        relationships.add(TermRelationshipRecord("T-000001", RelationshipType.usedWith, "T-000002", "고객번호는 고객명과 함께 고객 식별과 표시 정보로 사용"))
        relationships.add(TermRelationshipRecord("T-000001", RelationshipType.usedWith, "T-000003", "고객번호는 고객상태코드와 함께 고객 상태 판단에 사용"))
        relationships.add(TermRelationshipRecord("T-000001", RelationshipType.usedWith, "T-000004", "고객번호는 주문번호와 함께 주문 조회 조건과 응답에서 사용"))
        relationships.add(TermRelationshipRecord("T-000004", RelationshipType.relatedTo, "T-000005", "주문번호는 주문일자와 함께 주문 내역에서 사용"))
        relationships.add(TermRelationshipRecord("T-000004", RelationshipType.relatedTo, "T-000007", "주문번호는 주문금액과 함께 주문 내역에서 사용"))
        relationships.add(TermRelationshipRecord("T-000004", RelationshipType.relatedTo, "T-000008", "주문번호는 주문상태코드와 함께 주문 내역에서 사용"))
    }

    private fun addSeedTerm(
        termId: String,
        termNumber: String,
        domainName: String,
        koreanName: String,
        englishName: String,
        dbColumn: String,
        businessDefinition: String,
        usageContext: String,
        physicalType: String,
        digits: Int,
        decimalPoint: Int,
        owner: String,
        now: OffsetDateTime,
    ) {
        val apiField = englishName.split(" ").map { it.lowercase() }.mapIndexed { index, part ->
            if (index == 0) part else part.replaceFirstChar { it.uppercase() }
        }.joinToString("")
        val term = Term(
            termId = termId,
            termNumber = termNumber,
            domainName = domainName,
            usageType = "표준항목",
            koreanName = koreanName,
            englishName = englishName,
            englishAbbreviation = dbColumn,
            businessDefinition = businessDefinition,
            physicalType = physicalType,
            digits = digits,
            decimalPoint = decimalPoint,
            status = TermStatus.Approved,
            owner = owner,
            version = "1.0",
            expressions = emptyList(),
            aliases = emptyList(),
            usageContext = usageContext,
            createdAt = now,
            updatedAt = now,
        )
        val expressions = mutableListOf(
            TermExpression(expressionSequence.getAndIncrement(), termId, ExpressionType.Korean, koreanName, true, "ko", "standard"),
            TermExpression(expressionSequence.getAndIncrement(), termId, ExpressionType.English, englishName, true, "en", "title"),
            TermExpression(expressionSequence.getAndIncrement(), termId, ExpressionType.DB_COLUMN, dbColumn, true, "en", "UPPER_SNAKE"),
            TermExpression(expressionSequence.getAndIncrement(), termId, ExpressionType.API_FIELD, apiField, true, "en", "camelCase"),
            TermExpression(expressionSequence.getAndIncrement(), termId, ExpressionType.CODE_VARIABLE, apiField, true, "en", "camelCase"),
            TermExpression(expressionSequence.getAndIncrement(), termId, ExpressionType.UI_LABEL, koreanName, true, "ko", "label"),
            TermExpression(expressionSequence.getAndIncrement(), termId, ExpressionType.TEST_WORD, koreanName, true, "ko", "gherkin"),
        )
        terms[termId] = TermRecord(term, expressions, mutableListOf())
    }
}

private data class TermRecord(
    val term: Term,
    val expressions: MutableList<TermExpression>,
    val aliases: MutableList<TermAlias>,
) {
    fun toTerm(): Term = term.copy(expressions = expressions.toList(), aliases = aliases.toList())

    fun toSummary(): TermSummary = TermSummary(
        termId = term.termId,
        termNumber = term.termNumber,
        domainName = term.domainName,
        koreanName = term.koreanName,
        englishName = term.englishName,
        englishAbbreviation = term.englishAbbreviation,
        status = term.status,
        apiFieldName = expressions.firstOrNull { it.expressionType == ExpressionType.API_FIELD }?.expressionValue,
        relatedSystems = listOf(term.domainName),
    )

    fun toSearchDocument(): TermSearchDocument = TermSearchDocument(
        term = term,
        expressions = expressions.toList(),
        aliases = aliases.toList(),
    )

    fun matches(query: String): Boolean {
        val normalized = query.trim()
        return term.koreanName.contains(normalized, ignoreCase = true) ||
            term.englishName.contains(normalized, ignoreCase = true) ||
            term.englishAbbreviation.equals(normalized, ignoreCase = true) ||
            expressions.any { it.expressionValue.equals(normalized, ignoreCase = true) } ||
            aliases.any { it.aliasName.equals(normalized, ignoreCase = true) }
    }
}

private fun TermCommand.toTerm(termId: String, termNumber: String, version: String = "1.0", now: OffsetDateTime): Term = Term(
    termId = termId,
    termNumber = termNumber,
    domainName = domainName,
    usageType = usageType,
    koreanName = koreanName,
    englishName = englishName,
    englishAbbreviation = englishAbbreviation,
    businessDefinition = businessDefinition,
    usageContext = usageContext,
    physicalType = physicalType,
    digits = digits,
    decimalPoint = decimalPoint,
    status = status,
    owner = owner,
    version = version,
    expressions = emptyList(),
    aliases = emptyList(),
    createdAt = now,
    updatedAt = now,
)
