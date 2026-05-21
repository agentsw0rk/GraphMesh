package com.agentwork.graphmesh.ontology

import com.agentwork.graphmesh.ontology.application.port.`in`.OntologyUseCases
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OntologyUseCasesTest {

    private val store = mockk<OntologyStore>(relaxed = true)
    private val cache = mockk<OntologyCache>()
    private val validator = DefaultOntologyValidator()
    private val jenaAdapter = mockk<JenaAdapter>(relaxed = true)

    // Assigned to interface type — proves OntologyService satisfies OntologyUseCases
    private val useCases: OntologyUseCases = OntologyService(
        store = store,
        cache = cache,
        validator = validator,
        jenaAdapter = jenaAdapter
    )

    @Test
    fun `get returns null when ontology not found`() {
        every { cache.get("nonexistent") } returns null

        assertNull(useCases.get("nonexistent"))
    }

    @Test
    fun `list returns keys from store`() {
        every { store.listKeys() } returns listOf("onto-a", "onto-b")

        val keys = useCases.list()

        assertEquals(listOf("onto-a", "onto-b"), keys)
    }

    @Test
    fun `exportTurtle throws when ontology not found`() {
        every { cache.get("missing") } returns null

        assertThrows<IllegalArgumentException> {
            useCases.exportTurtle("missing")
        }
    }

    @Test
    fun `validate returns empty list for valid ontology`() {
        val ontology = Ontology(
            metadata = OntologyMetadata(name = "Valid", namespace = "http://valid.org/"),
            classes = mapOf("A" to OntologyClass(id = "A", uri = "http://valid.org/A"))
        )

        val errors = useCases.validate(ontology)

        assertEquals(emptyList(), errors)
    }
}
