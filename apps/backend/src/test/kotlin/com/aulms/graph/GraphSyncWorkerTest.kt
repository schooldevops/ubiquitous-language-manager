package com.aulms.graph

import com.aulms.term.TermRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GraphSyncWorkerTest {
    @Test
    fun `full sync creates customer number graph nodes and edges`() {
        val store = InMemoryGraphStore()
        val worker = GraphSyncWorker(TermRepository(), store)

        val result = worker.syncFull()

        assertTrue(result.success)
        assertNotNull(store.findNode("term:T-000001"))
        assertNotNull(store.findNode("domain:고객"))
        assertNotNull(store.findNode("column:DICTIONARY.고객.CUST_NO"))
        assertNotNull(store.findNode("apiField:DICTIONARY.고객.customerNumber"))
        assertNotNull(store.findNode("alias:A-000001"))
        assertNotNull(store.findEdge("REPRESENTS:column:DICTIONARY.고객.CUST_NO:term:T-000001"))
        assertNotNull(store.findEdge("REPRESENTS:apiField:DICTIONARY.고객.customerNumber:term:T-000001"))
        assertNotNull(store.findEdge("SYNONYM:alias:A-000001:term:T-000001"))
        assertNotNull(store.findEdge("FORBIDDEN:alias:A-000006:term:T-000001"))
        assertTrue(store.edges().any { it.relationshipType == "usedWith" && it.fromNodeKey == "term:T-000001" && it.toNodeKey == "term:T-000004" })
        assertEquals(GraphSyncMode.FULL, store.logs().last().mode)
    }

    @Test
    fun `incremental sync skips unchanged graph items`() {
        val store = InMemoryGraphStore()
        val worker = GraphSyncWorker(TermRepository(), store)

        val first = worker.syncIncremental()
        val second = worker.syncIncremental()

        assertTrue(first.createdNodes > 0)
        assertTrue(first.createdEdges > 0)
        assertEquals(0, second.createdNodes)
        assertEquals(0, second.updatedNodes)
        assertEquals(0, second.createdEdges)
        assertEquals(0, second.updatedEdges)
        assertTrue(second.skippedItems > 0)
        assertEquals(2, store.logs().size)
    }
}
