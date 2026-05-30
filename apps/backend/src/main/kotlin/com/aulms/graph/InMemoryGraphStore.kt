package com.aulms.graph

import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicLong

@Repository
class InMemoryGraphStore {
    private val nodes = linkedMapOf<String, GraphNode>()
    private val edges = linkedMapOf<String, GraphEdge>()
    private val logs = mutableListOf<GraphSyncLog>()
    private val logSequence = AtomicLong(1)

    fun upsertNode(node: GraphNode): GraphUpsertResult {
        val existing = nodes[node.nodeKey]
        if (existing == null) {
            nodes[node.nodeKey] = node
            return GraphUpsertResult(created = true, updated = false)
        }
        if (existing.checksum == node.checksum && existing.status == node.status) {
            return GraphUpsertResult.SKIPPED
        }
        nodes[node.nodeKey] = node
        return GraphUpsertResult(created = false, updated = true)
    }

    fun upsertEdge(edge: GraphEdge): GraphUpsertResult {
        val existing = edges[edge.edgeKey]
        if (existing == null) {
            edges[edge.edgeKey] = edge
            return GraphUpsertResult(created = true, updated = false)
        }
        if (existing.checksum == edge.checksum && existing.status == edge.status) {
            return GraphUpsertResult.SKIPPED
        }
        edges[edge.edgeKey] = edge
        return GraphUpsertResult(created = false, updated = true)
    }

    fun replaceAll(nodes: List<GraphNode>, edges: List<GraphEdge>) {
        this.nodes.clear()
        this.edges.clear()
        nodes.forEach { this.nodes[it.nodeKey] = it }
        edges.forEach { this.edges[it.edgeKey] = it }
    }

    fun addLog(result: GraphSyncResult): GraphSyncLog {
        val log = GraphSyncLog(
            logId = logSequence.getAndIncrement(),
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
        logs.add(log)
        return log
    }

    fun nodes(): List<GraphNode> = nodes.values.toList()

    fun edges(): List<GraphEdge> = edges.values.toList()

    fun logs(): List<GraphSyncLog> = logs.toList()

    fun findNode(nodeKey: String): GraphNode? = nodes[nodeKey]

    fun findEdge(edgeKey: String): GraphEdge? = edges[edgeKey]

    fun clear() {
        nodes.clear()
        edges.clear()
        logs.clear()
    }
}
