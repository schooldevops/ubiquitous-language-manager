package com.aulms.graph

import java.time.OffsetDateTime

data class GraphNode(
    val nodeKey: String,
    val nodeType: String,
    val properties: Map<String, String>,
    val syncKey: String,
    val checksum: String,
    val status: String = "Active",
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
)

data class GraphEdge(
    val edgeKey: String,
    val relationshipType: String,
    val fromNodeKey: String,
    val toNodeKey: String,
    val properties: Map<String, String>,
    val syncKey: String,
    val checksum: String,
    val status: String = "Active",
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
)

data class GraphSyncResult(
    val mode: GraphSyncMode,
    val success: Boolean,
    val createdNodes: Int,
    val updatedNodes: Int,
    val createdEdges: Int,
    val updatedEdges: Int,
    val skippedItems: Int,
    val retryCount: Int,
    val message: String,
    val syncedAt: OffsetDateTime = OffsetDateTime.now(),
)

data class GraphSyncLog(
    val logId: Long,
    val mode: GraphSyncMode,
    val success: Boolean,
    val createdNodes: Int,
    val updatedNodes: Int,
    val createdEdges: Int,
    val updatedEdges: Int,
    val skippedItems: Int,
    val retryCount: Int,
    val message: String,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
)

enum class GraphSyncMode {
    INCREMENTAL,
    FULL,
}

data class GraphUpsertResult(
    val created: Boolean,
    val updated: Boolean,
) {
    companion object {
        val SKIPPED = GraphUpsertResult(created = false, updated = false)
    }
}
