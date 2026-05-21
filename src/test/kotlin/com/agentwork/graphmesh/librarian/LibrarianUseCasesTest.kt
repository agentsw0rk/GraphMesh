package com.agentwork.graphmesh.librarian

import com.agentwork.graphmesh.collection.application.port.`in`.CollectionUseCases
import com.agentwork.graphmesh.librarian.application.port.`in`.LibrarianUseCases
import com.agentwork.graphmesh.messaging.DocumentIngestedProducer
import com.agentwork.graphmesh.storage.blob.BlobStore
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LibrarianUseCasesTest {

    private val documentStore = mockk<DocumentStore>(relaxed = true)
    private val blobStore = mockk<BlobStore>(relaxed = true)
    private val collectionService = mockk<CollectionUseCases>(relaxed = true)
    private val documentIngestedProducer = mockk<DocumentIngestedProducer>(relaxed = true)

    // Assigned to interface type — proves LibrarianService satisfies LibrarianUseCases
    private val useCases: LibrarianUseCases = LibrarianService(
        documentStore = documentStore,
        blobStore = blobStore,
        collectionService = collectionService,
        documentIngestedProducer = documentIngestedProducer,
        defaultBucket = "graphmesh"
    )

    @Test
    fun `findById returns null when document does not exist`() {
        every { documentStore.findById("doc-unknown") } returns null

        assertNull(useCases.findById("doc-unknown"))
    }

    @Test
    fun `findByCollection delegates to document store`() {
        val docs = listOf(
            Document(id = "doc-1", collectionId = "col-1", type = DocumentType.SOURCE),
            Document(id = "doc-2", collectionId = "col-1", type = DocumentType.SOURCE)
        )
        every { documentStore.findByCollection("col-1", null) } returns docs

        val result = useCases.findByCollection("col-1")

        assertEquals(2, result.size)
    }

    @Test
    fun `updateState calls store`() {
        val doc = Document(id = "doc-1", collectionId = "col-1", state = DocumentState.UPLOADED)
        every { documentStore.findById("doc-1") } returns doc

        useCases.updateState("doc-1", DocumentState.PROCESSING)

        verify { documentStore.updateState("doc-1", DocumentState.PROCESSING) }
    }

    @Test
    fun `updateState throws when document does not exist`() {
        every { documentStore.findById("doc-missing") } returns null

        assertThrows<DocumentNotFoundException> {
            useCases.updateState("doc-missing", DocumentState.PROCESSING)
        }
    }
}
