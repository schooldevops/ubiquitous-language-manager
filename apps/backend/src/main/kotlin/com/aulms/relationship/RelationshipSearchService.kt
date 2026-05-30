package com.aulms.relationship

import com.aulms.graph.GraphEdge
import com.aulms.graph.GraphNode
import com.aulms.graph.GraphSyncWorker
import com.aulms.model.ColumnSystemUsage
import com.aulms.model.ColumnSystemUsageResponse
import com.aulms.model.DeprecatedUsage
import com.aulms.model.DeprecatedUsageResponse
import com.aulms.model.GraphPathEdge
import com.aulms.model.GraphPathNode
import com.aulms.model.RelationshipPath
import com.aulms.model.RelationshipSearchResponse
import com.aulms.model.RelationshipSearchResult
import com.aulms.model.TermStatus
import com.aulms.term.TermRepository
import org.springframework.stereotype.Service

@Service
class RelationshipSearchService(
    private val graphSyncWorker: GraphSyncWorker,
    private val termRepository: TermRepository,
) {
    fun getTermRelationships(termId: String, relationshipType: String?): RelationshipSearchResponse {
        termRepository.get(termId)
        val snapshot = graphSyncWorker.buildSnapshot()
        val sourceKey = "term:$termId"
        val termNodes = snapshot.nodes.associateBy { it.nodeKey }
        val relationshipEdges = snapshot.edges
            .filter { it.fromNodeKey == sourceKey || it.toNodeKey == sourceKey }
            .filter { it.fromNodeKey.startsWith("term:") && it.toNodeKey.startsWith("term:") }
            .filter { relationshipType == null || it.relationshipType == relationshipType }

        val items = relationshipEdges.mapNotNull { edge ->
            val otherKey = if (edge.fromNodeKey == sourceKey) edge.toNodeKey else edge.fromNodeKey
            val otherNode = termNodes[otherKey] ?: return@mapNotNull null
            RelationshipSearchResult(
                termId = otherNode.properties.getValue("termId"),
                standardTerm = otherNode.properties.getValue("koreanName"),
                englishName = otherNode.properties.getValue("englishName"),
                relationshipType = edge.relationshipType,
                direction = if (edge.fromNodeKey == sourceKey) RelationshipSearchResult.Direction.OUTGOING else RelationshipSearchResult.Direction.INCOMING,
                reason = edge.properties["description"] ?: edge.properties["evidence"] ?: "Graphify relationship",
            )
        }
        val paths = relationshipEdges.mapNotNull { edge ->
            val from = termNodes[edge.fromNodeKey] ?: return@mapNotNull null
            val to = termNodes[edge.toNodeKey] ?: return@mapNotNull null
            edge.toPath(from, to)
        }
        return RelationshipSearchResponse(query = termId, items = items, paths = paths)
    }

    fun getDomainTerms(domainName: String): RelationshipSearchResponse {
        val items = termRepository.searchDocuments()
            .filter { it.term.domainName == domainName && it.term.status == TermStatus.Approved }
            .map {
                RelationshipSearchResult(
                    termId = it.term.termId,
                    standardTerm = it.term.koreanName,
                    englishName = it.term.englishName,
                    relationshipType = "belongsTo",
                    direction = RelationshipSearchResult.Direction.OUTGOING,
                    reason = "${domainName} 도메인의 승인된 표준 용어",
                )
            }
        return RelationshipSearchResponse(query = domainName, items = items, paths = emptyList())
    }

    fun getColumnSystems(columnName: String): ColumnSystemUsageResponse {
        val normalized = columnName.uppercase()
        val items = termRepository.searchDocuments()
            .filter { it.term.englishAbbreviation.uppercase() == normalized }
            .map { document ->
                ColumnSystemUsage(
                    systemCode = "DICTIONARY",
                    systemName = "데이터 사전",
                    tableName = document.term.domainName,
                    columnName = document.term.englishAbbreviation,
                    termId = document.term.termId,
                    standardTerm = document.term.koreanName,
                    apiFields = listOf(document.apiField()),
                )
            }
        return ColumnSystemUsageResponse(columnName = columnName, items = items)
    }

    fun getDeprecatedUsages(): DeprecatedUsageResponse {
        val items = termRepository.searchDocuments()
            .flatMap { document ->
                document.aliases
                    .filter { it.aliasType.value == "Forbidden" || it.aliasType.value == "Deprecated" }
                    .map {
                        DeprecatedUsage(
                            expression = it.aliasName,
                            aliasType = it.aliasType.value,
                            termId = document.term.termId,
                            standardTerm = document.term.koreanName,
                            recommendedExpression = if (it.aliasName.all { char -> char.isUpperCase() || char == '_' || char.isDigit() }) {
                                document.term.englishAbbreviation
                            } else {
                                document.apiField()
                            },
                            reason = it.reason,
                        )
                    }
            }
        return DeprecatedUsageResponse(items = items)
    }

    private fun GraphEdge.toPath(from: GraphNode, to: GraphNode): RelationshipPath =
        RelationshipPath(
            nodes = listOf(from.toPathNode(), to.toPathNode()),
            edges = listOf(
                GraphPathEdge(
                    edgeKey = edgeKey,
                    relationshipType = relationshipType,
                    fromNodeKey = fromNodeKey,
                    toNodeKey = toNodeKey,
                ),
            ),
        )

    private fun GraphNode.toPathNode(): GraphPathNode =
        GraphPathNode(
            nodeKey = nodeKey,
            nodeType = nodeType,
            label = properties["koreanName"] ?: properties["domainName"] ?: properties["expressionValue"] ?: nodeKey,
        )
}
