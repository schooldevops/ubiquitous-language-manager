package com.aulms.graph

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Profile
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

/**
 * Neo4j 기반 GraphStore (postgres 프로파일).
 * 노드는 :GraphNode(nodeKey unique), 엣지는 동적 타입 대신 :REL(relationshipType 속성)로 표현한다.
 * properties 맵은 propertiesJson 으로 직렬화해 저장한다.
 */
@Repository
@Profile("postgres")
class Neo4jGraphStore(
    private val client: Neo4jClient,
    private val objectMapper: ObjectMapper,
) : GraphStore {

    override fun upsertNode(node: GraphNode): GraphUpsertResult {
        val existing = client.query("MATCH (n:GraphNode {nodeKey: \$k}) RETURN n.checksum AS checksum, n.status AS status")
            .bind(node.nodeKey).to("k").fetch().one()
        client.query(
            """
            MERGE (n:GraphNode {nodeKey: ${'$'}nodeKey})
            SET n.nodeType = ${'$'}nodeType, n.syncKey = ${'$'}syncKey, n.checksum = ${'$'}checksum,
                n.status = ${'$'}status, n.updatedAt = ${'$'}updatedAt, n.propertiesJson = ${'$'}propertiesJson
            """.trimIndent(),
        ).bindAll(
            mapOf(
                "nodeKey" to node.nodeKey,
                "nodeType" to node.nodeType,
                "syncKey" to node.syncKey,
                "checksum" to node.checksum,
                "status" to node.status,
                "updatedAt" to node.updatedAt.toString(),
                "propertiesJson" to objectMapper.writeValueAsString(node.properties),
            ),
        ).run()
        return result(existing, node.checksum, node.status)
    }

    override fun upsertEdge(edge: GraphEdge): GraphUpsertResult {
        val existing = client.query("MATCH ()-[r:REL {edgeKey: \$k}]->() RETURN r.checksum AS checksum, r.status AS status")
            .bind(edge.edgeKey).to("k").fetch().one()
        client.query(
            """
            MERGE (a:GraphNode {nodeKey: ${'$'}fromKey})
            MERGE (b:GraphNode {nodeKey: ${'$'}toKey})
            MERGE (a)-[r:REL {edgeKey: ${'$'}edgeKey}]->(b)
            SET r.relationshipType = ${'$'}relationshipType, r.syncKey = ${'$'}syncKey, r.checksum = ${'$'}checksum,
                r.status = ${'$'}status, r.updatedAt = ${'$'}updatedAt, r.propertiesJson = ${'$'}propertiesJson
            """.trimIndent(),
        ).bindAll(
            mapOf(
                "fromKey" to edge.fromNodeKey,
                "toKey" to edge.toNodeKey,
                "edgeKey" to edge.edgeKey,
                "relationshipType" to edge.relationshipType,
                "syncKey" to edge.syncKey,
                "checksum" to edge.checksum,
                "status" to edge.status,
                "updatedAt" to edge.updatedAt.toString(),
                "propertiesJson" to objectMapper.writeValueAsString(edge.properties),
            ),
        ).run()
        return result(existing, edge.checksum, edge.status)
    }

    override fun replaceAll(nodes: List<GraphNode>, edges: List<GraphEdge>) {
        client.query("MATCH (n:GraphNode) DETACH DELETE n").run()
        nodes.forEach { upsertNode(it) }
        edges.forEach { upsertEdge(it) }
    }

    override fun addLog(result: GraphSyncResult): GraphSyncLog {
        val nextId = client.query("MATCH (l:GraphSyncLog) RETURN coalesce(max(l.logId), 0) + 1 AS next")
            .fetch().one().orElse(null)?.get("next") as? Long ?: 1L
        val log = GraphSyncLog(
            logId = nextId,
            mode = result.mode,
            success = result.success,
            createdNodes = result.createdNodes,
            updatedNodes = result.updatedNodes,
            createdEdges = result.createdEdges,
            updatedEdges = result.updatedEdges,
            skippedItems = result.skippedItems,
            retryCount = result.retryCount,
            message = result.message,
            createdAt = result.syncedAt,
        )
        client.query(
            """
            CREATE (l:GraphSyncLog {
                logId: ${'$'}logId, mode: ${'$'}mode, success: ${'$'}success,
                createdNodes: ${'$'}createdNodes, updatedNodes: ${'$'}updatedNodes,
                createdEdges: ${'$'}createdEdges, updatedEdges: ${'$'}updatedEdges,
                skippedItems: ${'$'}skippedItems, retryCount: ${'$'}retryCount,
                message: ${'$'}message, createdAt: ${'$'}createdAt
            })
            """.trimIndent(),
        ).bindAll(
            mapOf(
                "logId" to log.logId,
                "mode" to log.mode.name,
                "success" to log.success,
                "createdNodes" to log.createdNodes.toLong(),
                "updatedNodes" to log.updatedNodes.toLong(),
                "createdEdges" to log.createdEdges.toLong(),
                "updatedEdges" to log.updatedEdges.toLong(),
                "skippedItems" to log.skippedItems.toLong(),
                "retryCount" to log.retryCount.toLong(),
                "message" to log.message,
                "createdAt" to log.createdAt.toString(),
            ),
        ).run()
        return log
    }

    override fun nodes(): List<GraphNode> =
        client.query(
            "MATCH (n:GraphNode) RETURN n.nodeKey AS nodeKey, n.nodeType AS nodeType, n.syncKey AS syncKey, " +
                "n.checksum AS checksum, n.status AS status, n.updatedAt AS updatedAt, n.propertiesJson AS propertiesJson",
        ).fetch().all().map { it.toNode() }

    override fun edges(): List<GraphEdge> =
        client.query(
            "MATCH (a:GraphNode)-[r:REL]->(b:GraphNode) RETURN r.edgeKey AS edgeKey, r.relationshipType AS relationshipType, " +
                "a.nodeKey AS fromNodeKey, b.nodeKey AS toNodeKey, r.syncKey AS syncKey, r.checksum AS checksum, " +
                "r.status AS status, r.updatedAt AS updatedAt, r.propertiesJson AS propertiesJson",
        ).fetch().all().map { it.toEdge() }

    override fun logs(): List<GraphSyncLog> =
        client.query(
            "MATCH (l:GraphSyncLog) RETURN l.logId AS logId, l.mode AS mode, l.success AS success, " +
                "l.createdNodes AS createdNodes, l.updatedNodes AS updatedNodes, l.createdEdges AS createdEdges, " +
                "l.updatedEdges AS updatedEdges, l.skippedItems AS skippedItems, l.retryCount AS retryCount, " +
                "l.message AS message, l.createdAt AS createdAt ORDER BY l.logId",
        ).fetch().all().map { it.toLog() }

    override fun findNode(nodeKey: String): GraphNode? =
        client.query(
            "MATCH (n:GraphNode {nodeKey: \$k}) RETURN n.nodeKey AS nodeKey, n.nodeType AS nodeType, n.syncKey AS syncKey, " +
                "n.checksum AS checksum, n.status AS status, n.updatedAt AS updatedAt, n.propertiesJson AS propertiesJson",
        ).bind(nodeKey).to("k").fetch().one().orElse(null)?.toNode()

    override fun findEdge(edgeKey: String): GraphEdge? =
        client.query(
            "MATCH (a:GraphNode)-[r:REL {edgeKey: \$k}]->(b:GraphNode) RETURN r.edgeKey AS edgeKey, r.relationshipType AS relationshipType, " +
                "a.nodeKey AS fromNodeKey, b.nodeKey AS toNodeKey, r.syncKey AS syncKey, r.checksum AS checksum, " +
                "r.status AS status, r.updatedAt AS updatedAt, r.propertiesJson AS propertiesJson",
        ).bind(edgeKey).to("k").fetch().one().orElse(null)?.toEdge()

    override fun clear() {
        client.query("MATCH (n:GraphNode) DETACH DELETE n").run()
        client.query("MATCH (l:GraphSyncLog) DELETE l").run()
    }

    // --- helpers -----------------------------------------------------------

    private fun result(existing: java.util.Optional<Map<String, Any>>, checksum: String, status: String): GraphUpsertResult {
        if (existing.isEmpty) return GraphUpsertResult(created = true, updated = false)
        val row = existing.get()
        if (row["checksum"] == checksum && row["status"] == status) return GraphUpsertResult.SKIPPED
        return GraphUpsertResult(created = false, updated = true)
    }

    @Suppress("UNCHECKED_CAST")
    private fun propsOf(json: Any?): Map<String, String> =
        if (json == null) emptyMap() else objectMapper.readValue(json.toString(), Map::class.java) as Map<String, String>

    private fun Map<String, Any>.toNode(): GraphNode = GraphNode(
        nodeKey = this["nodeKey"] as String,
        nodeType = this["nodeType"] as String,
        properties = propsOf(this["propertiesJson"]),
        syncKey = this["syncKey"] as String,
        checksum = this["checksum"] as String,
        status = this["status"] as String,
        updatedAt = OffsetDateTime.parse(this["updatedAt"] as String),
    )

    private fun Map<String, Any>.toEdge(): GraphEdge = GraphEdge(
        edgeKey = this["edgeKey"] as String,
        relationshipType = this["relationshipType"] as String,
        fromNodeKey = this["fromNodeKey"] as String,
        toNodeKey = this["toNodeKey"] as String,
        properties = propsOf(this["propertiesJson"]),
        syncKey = this["syncKey"] as String,
        checksum = this["checksum"] as String,
        status = this["status"] as String,
        updatedAt = OffsetDateTime.parse(this["updatedAt"] as String),
    )

    private fun Map<String, Any>.toLog(): GraphSyncLog = GraphSyncLog(
        logId = (this["logId"] as Number).toLong(),
        mode = GraphSyncMode.valueOf(this["mode"] as String),
        success = this["success"] as Boolean,
        createdNodes = (this["createdNodes"] as Number).toInt(),
        updatedNodes = (this["updatedNodes"] as Number).toInt(),
        createdEdges = (this["createdEdges"] as Number).toInt(),
        updatedEdges = (this["updatedEdges"] as Number).toInt(),
        skippedItems = (this["skippedItems"] as Number).toInt(),
        retryCount = (this["retryCount"] as Number).toInt(),
        message = this["message"] as String,
        createdAt = OffsetDateTime.parse(this["createdAt"] as String),
    )
}
