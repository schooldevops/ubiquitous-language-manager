package com.aulms.graph

/**
 * Graphify 그래프 저장소. 영속화 모드별 구현:
 *  - [InMemoryGraphStore] : 기본(memory) 프로파일
 *  - Neo4jGraphStore      : postgres 프로파일 (Neo4j bolt 연동)
 */
interface GraphStore {
    fun upsertNode(node: GraphNode): GraphUpsertResult
    fun upsertEdge(edge: GraphEdge): GraphUpsertResult
    fun replaceAll(nodes: List<GraphNode>, edges: List<GraphEdge>)
    fun addLog(result: GraphSyncResult): GraphSyncLog
    fun nodes(): List<GraphNode>
    fun edges(): List<GraphEdge>
    fun logs(): List<GraphSyncLog>
    fun findNode(nodeKey: String): GraphNode?
    fun findEdge(edgeKey: String): GraphEdge?
    fun clear()
}
