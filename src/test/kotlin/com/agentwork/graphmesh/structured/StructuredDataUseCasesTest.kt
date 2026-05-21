package com.agentwork.graphmesh.structured

import com.agentwork.graphmesh.structured.application.port.`in`.StructuredDataUseCases
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StructuredDataUseCasesTest {

    private val schemaStore = mockk<SchemaStore>(relaxed = true)
    private val rowStore = mockk<CassandraRowStore>(relaxed = true)

    // Assigned to interface type — proves StructuredDataService satisfies StructuredDataUseCases
    private val useCases: StructuredDataUseCases = StructuredDataService(
        schemaStore = schemaStore,
        rowStore = rowStore
    )

    private val schema = TableSchema(
        name = "items",
        columns = listOf(
            ColumnDescriptor(name = "id", type = ColumnType.STRING, primaryKey = true),
            ColumnDescriptor(name = "name", type = ColumnType.STRING, nullable = false)
        )
    )

    @Test
    fun `store delegates to backend when schema is found`() {
        every { schemaStore.load("items") } returns schema

        val row = DataRow(collection = "col-1", schemaName = "items", values = mapOf("id" to "1", "name" to "Alice"))
        val result = useCases.store(row)

        assertTrue(result.success)
        verify { rowStore.insert(row) }
    }

    @Test
    fun `store returns failure when schema not found`() {
        every { schemaStore.load("unknown") } returns null

        val row = DataRow(collection = "col-1", schemaName = "unknown", values = mapOf("id" to "1"))
        val result = useCases.store(row)

        assertFalse(result.success)
        assertTrue(result.error!!.contains("not found"))
    }

    @Test
    fun `getSchema returns null when not found`() {
        every { schemaStore.load("missing") } returns null

        assertNull(useCases.getSchema("missing"))
    }

    @Test
    fun `getSchema returns schema when found`() {
        every { schemaStore.load("items") } returns schema

        val result = useCases.getSchema("items")

        assertTrue(result != null)
        assertTrue(result.name == "items")
    }
}
