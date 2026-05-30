package com.aulms.graph

import com.aulms.model.AliasType
import com.aulms.model.ExpressionType
import com.aulms.model.TermAlias
import com.aulms.model.TermExpression
import com.aulms.term.TermRepository
import com.aulms.term.TermSearchDocument
import org.springframework.stereotype.Component
import java.security.MessageDigest

@Component
class GraphSyncWorker(
    private val termRepository: TermRepository,
    private val graphStore: InMemoryGraphStore,
) {
    fun syncIncremental(maxRetries: Int = 3): GraphSyncResult =
        executeWithRetry(GraphSyncMode.INCREMENTAL, maxRetries) { sync(GraphSyncMode.INCREMENTAL) }

    fun syncFull(maxRetries: Int = 3): GraphSyncResult =
        executeWithRetry(GraphSyncMode.FULL, maxRetries) { sync(GraphSyncMode.FULL) }

    private fun executeWithRetry(mode: GraphSyncMode, maxRetries: Int, action: () -> GraphSyncResult): GraphSyncResult {
        var attempt = 0
        var lastFailure: Exception? = null
        while (attempt <= maxRetries) {
            try {
                val result = action().copy(retryCount = attempt)
                graphStore.addLog(result)
                return result
            } catch (exception: Exception) {
                lastFailure = exception
                attempt += 1
            }
        }
        val failure = GraphSyncResult(
            mode = mode,
            success = false,
            createdNodes = 0,
            updatedNodes = 0,
            createdEdges = 0,
            updatedEdges = 0,
            skippedItems = 0,
            retryCount = maxRetries,
            message = "Graph sync failed after retry: ${lastFailure?.message ?: "unknown"}",
        )
        graphStore.addLog(failure)
        return failure
    }

    private fun sync(mode: GraphSyncMode): GraphSyncResult {
        val snapshot = buildSnapshot()
        if (mode == GraphSyncMode.FULL) {
            graphStore.replaceAll(snapshot.nodes, snapshot.edges)
            return GraphSyncResult(
                mode = mode,
                success = true,
                createdNodes = snapshot.nodes.size,
                updatedNodes = 0,
                createdEdges = snapshot.edges.size,
                updatedEdges = 0,
                skippedItems = 0,
                retryCount = 0,
                message = "Full graph sync completed",
            )
        }

        var createdNodes = 0
        var updatedNodes = 0
        var createdEdges = 0
        var updatedEdges = 0
        var skipped = 0

        snapshot.nodes.forEach {
            val result = graphStore.upsertNode(it)
            if (result.created) createdNodes += 1
            if (result.updated) updatedNodes += 1
            if (!result.created && !result.updated) skipped += 1
        }
        snapshot.edges.forEach {
            val result = graphStore.upsertEdge(it)
            if (result.created) createdEdges += 1
            if (result.updated) updatedEdges += 1
            if (!result.created && !result.updated) skipped += 1
        }

        return GraphSyncResult(
            mode = mode,
            success = true,
            createdNodes = createdNodes,
            updatedNodes = updatedNodes,
            createdEdges = createdEdges,
            updatedEdges = updatedEdges,
            skippedItems = skipped,
            retryCount = 0,
            message = "Incremental graph sync completed",
        )
    }

    fun buildSnapshot(): GraphSnapshot {
        val nodes = linkedMapOf<String, GraphNode>()
        val edges = linkedMapOf<String, GraphEdge>()
        val documents = termRepository.searchDocuments()

        documents.forEach { document ->
            nodes.putIfAbsent(document.termNode().nodeKey, document.termNode())
            nodes.putIfAbsent(document.domainNode().nodeKey, document.domainNode())
            edges.putIfAbsent(document.termBelongsToDomainEdge().edgeKey, document.termBelongsToDomainEdge())

            document.expressions.forEach { expression ->
                val expressionNode = document.expressionNode(expression)
                if (expressionNode != null) {
                    nodes.putIfAbsent(expressionNode.nodeKey, expressionNode)
                    val edge = document.expressionRepresentsTermEdge(expression, expressionNode)
                    edges.putIfAbsent(edge.edgeKey, edge)
                }
            }

            document.aliases.forEach { alias ->
                val aliasNode = document.aliasNode(alias)
                nodes.putIfAbsent(aliasNode.nodeKey, aliasNode)
                val edge = document.aliasEdge(alias, aliasNode)
                edges.putIfAbsent(edge.edgeKey, edge)
            }
        }
        termRepository.relationshipDocuments().forEach { relationship ->
            val from = "term:${relationship.sourceTermId}"
            val to = "term:${relationship.targetTermId}"
            if (nodes.containsKey(from) && nodes.containsKey(to)) {
                val properties = mapOf(
                    "sourceType" to "DATA_DICTIONARY",
                    "relationshipType" to relationship.relationshipType.value,
                    "description" to relationship.description,
                    "evidence" to "TERM_RELATIONSHIP",
                    "confidence" to "1.0",
                )
                val edge = GraphEdge(
                    edgeKey = "TERM_RELATIONSHIP:$from:${relationship.relationshipType.value}:$to",
                    relationshipType = relationship.relationshipType.value,
                    fromNodeKey = from,
                    toNodeKey = to,
                    properties = properties,
                    syncKey = "TERM_RELATIONSHIP:${relationship.sourceTermId}:${relationship.relationshipType.value}:${relationship.targetTermId}",
                    checksum = checksum(properties),
                )
                edges.putIfAbsent(edge.edgeKey, edge)
            }
        }

        return GraphSnapshot(nodes = nodes.values.toList(), edges = edges.values.toList())
    }

    private fun TermSearchDocument.termNode(): GraphNode {
        val key = "term:${term.termId}"
        val properties = mapOf(
            "termId" to term.termId,
            "termNumber" to term.termNumber,
            "koreanName" to term.koreanName,
            "englishName" to term.englishName,
            "englishAbbreviation" to term.englishAbbreviation,
            "domainName" to term.domainName,
            "version" to term.version,
            "sourceType" to "DATA_DICTIONARY",
        )
        return GraphNode(key, "Term", properties, "TERM_MASTER:${term.termId}:${term.version}", checksum(properties), term.status.value)
    }

    private fun TermSearchDocument.domainNode(): GraphNode {
        val key = "domain:${term.domainName}"
        val properties = mapOf(
            "domainName" to term.domainName,
            "sourceType" to "DATA_DICTIONARY",
        )
        return GraphNode(key, "Domain", properties, "DOMAIN:${term.domainName}", checksum(properties))
    }

    private fun TermSearchDocument.termBelongsToDomainEdge(): GraphEdge {
        val from = "term:${term.termId}"
        val to = "domain:${term.domainName}"
        val properties = mapOf(
            "sourceType" to "DATA_DICTIONARY",
            "evidence" to "TERM_MASTER.domain_name",
            "confidence" to "1.0",
        )
        return GraphEdge(
            edgeKey = "BELONGS_TO:$from:$to",
            relationshipType = "belongsTo",
            fromNodeKey = from,
            toNodeKey = to,
            properties = properties,
            syncKey = "BELONGS_TO:$from:$to",
            checksum = checksum(properties),
        )
    }

    private fun TermSearchDocument.expressionNode(expression: TermExpression): GraphNode? {
        val sourceType = "DATA_DICTIONARY"
        val common = mapOf(
            "expressionId" to expression.expressionId.toString(),
            "expressionType" to expression.expressionType.value,
            "expressionValue" to expression.expressionValue,
            "termId" to term.termId,
            "sourceType" to sourceType,
        )
        return when (expression.expressionType) {
            ExpressionType.DB_COLUMN -> {
                val key = "column:DICTIONARY.${term.domainName}.${expression.expressionValue}"
                val properties = common + mapOf(
                    "columnName" to expression.expressionValue,
                    "physicalType" to term.physicalType,
                    "digits" to term.digits.toString(),
                    "decimalPoint" to term.decimalPoint.toString(),
                )
                GraphNode(key, "Column", properties, "TERM_EXPRESSION:${expression.expressionId}", checksum(properties))
            }
            ExpressionType.API_FIELD -> {
                val key = "apiField:DICTIONARY.${term.domainName}.${expression.expressionValue}"
                val properties = common + mapOf(
                    "fieldName" to expression.expressionValue,
                    "direction" to "unknown",
                    "schemaType" to term.physicalType,
                )
                GraphNode(key, "APIField", properties, "TERM_EXPRESSION:${expression.expressionId}", checksum(properties))
            }
            ExpressionType.CODE_VARIABLE -> {
                val key = "dtoField:DICTIONARY.${term.domainName}.${expression.expressionValue}"
                val properties = common + mapOf(
                    "fieldName" to expression.expressionValue,
                    "language" to "unknown",
                )
                GraphNode(key, "DTOField", properties, "TERM_EXPRESSION:${expression.expressionId}", checksum(properties))
            }
            else -> null
        }
    }

    private fun TermSearchDocument.expressionRepresentsTermEdge(expression: TermExpression, expressionNode: GraphNode): GraphEdge {
        val to = "term:${term.termId}"
        val properties = mapOf(
            "sourceType" to "DATA_DICTIONARY",
            "expressionType" to expression.expressionType.value,
            "expressionValue" to expression.expressionValue,
            "evidence" to "TERM_EXPRESSION.${expression.expressionType.value}",
            "confidence" to "1.0",
        )
        return GraphEdge(
            edgeKey = "REPRESENTS:${expressionNode.nodeKey}:$to",
            relationshipType = "represents",
            fromNodeKey = expressionNode.nodeKey,
            toNodeKey = to,
            properties = properties,
            syncKey = "REPRESENTS:${expressionNode.nodeKey}:${term.termId}",
            checksum = checksum(properties),
        )
    }

    private fun TermSearchDocument.aliasNode(alias: TermAlias): GraphNode {
        val key = "alias:${alias.aliasId}"
        val properties = mapOf(
            "aliasId" to alias.aliasId,
            "aliasName" to alias.aliasName,
            "aliasType" to alias.aliasType.value,
            "recommendationAction" to alias.recommendationAction,
            "reason" to alias.reason,
            "termId" to term.termId,
            "sourceType" to "DATA_DICTIONARY",
        )
        return GraphNode(key, "Alias", properties, "TERM_ALIAS:${alias.aliasId}", checksum(properties))
    }

    private fun TermSearchDocument.aliasEdge(alias: TermAlias, aliasNode: GraphNode): GraphEdge {
        val to = "term:${term.termId}"
        val relationship = when (alias.aliasType) {
            AliasType.Synonym -> "synonym"
            AliasType.Forbidden -> "forbidden"
            AliasType.Deprecated -> "deprecatedBy"
            AliasType.NeedsContext -> "mentions"
        }
        val properties = mapOf(
            "sourceType" to "DATA_DICTIONARY",
            "aliasType" to alias.aliasType.value,
            "aliasName" to alias.aliasName,
            "recommendationAction" to alias.recommendationAction,
            "evidence" to alias.reason,
            "confidence" to "1.0",
        )
        return GraphEdge(
            edgeKey = "${relationship.uppercase()}:${aliasNode.nodeKey}:$to",
            relationshipType = relationship,
            fromNodeKey = aliasNode.nodeKey,
            toNodeKey = to,
            properties = properties,
            syncKey = "${relationship.uppercase()}:${aliasNode.nodeKey}:${term.termId}",
            checksum = checksum(properties),
        )
    }

    private fun checksum(properties: Map<String, String>): String {
        val source = properties.toSortedMap().entries.joinToString("|") { "${it.key}=${it.value}" }
        val digest = MessageDigest.getInstance("SHA-256").digest(source.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}

data class GraphSnapshot(
    val nodes: List<GraphNode>,
    val edges: List<GraphEdge>,
)
