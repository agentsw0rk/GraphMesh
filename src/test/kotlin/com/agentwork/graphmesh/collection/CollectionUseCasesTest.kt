package com.agentwork.graphmesh.collection

import com.agentwork.graphmesh.collection.application.port.`in`.CollectionUseCases
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.context.ApplicationEventPublisher
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CollectionUseCasesTest {

    private val store = mockk<CollectionStore>(relaxed = true)
    private val lifecycleManager = mockk<CollectionLifecycleManager>(relaxed = true)
    private val eventPublisher = ApplicationEventPublisher { }
    private val eventProducer = mockk<CollectionEventProducer>(relaxed = true)

    // Assigned to interface type — proves CollectionService satisfies CollectionUseCases
    private val useCases: CollectionUseCases = CollectionService(
        collectionStore = store,
        lifecycleManager = lifecycleManager,
        eventPublisher = eventPublisher,
        collectionEventProducer = eventProducer
    )

    @Test
    fun `findById returns null when collection does not exist`() {
        every { store.findById("unknown") } returns null

        assertNull(useCases.findById("unknown"))
    }

    @Test
    fun `findAll returns empty list when no collections exist`() {
        every { store.findAll() } returns emptyList()

        assertEquals(emptyList(), useCases.findAll())
    }

    @Test
    fun `requireExists throws CollectionNotFoundException for unknown id`() {
        every { store.exists("missing") } returns false

        assertThrows<CollectionNotFoundException> {
            useCases.requireExists("missing")
        }
    }

    @Test
    fun `findById returns collection when it exists`() {
        val collection = Collection(id = "col-1", name = "Test")
        every { store.findById("col-1") } returns collection

        val result = useCases.findById("col-1")

        assertEquals("col-1", result?.id)
        assertEquals("Test", result?.name)
    }
}
