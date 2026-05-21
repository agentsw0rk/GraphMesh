package com.agentwork.graphmesh.skos

import com.agentwork.graphmesh.rdf.SkosTypes
import com.agentwork.graphmesh.skos.application.port.`in`.SkosUseCases
import com.agentwork.graphmesh.storage.InMemoryQuadStore
import com.agentwork.graphmesh.storage.ObjectType
import com.agentwork.graphmesh.storage.RDF_TYPE_URI
import com.agentwork.graphmesh.storage.StoredQuad
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SkosUseCasesTest {

    private val quadStore = InMemoryQuadStore()

    // Assigned to interface type — proves SkosService satisfies SkosUseCases
    private val useCases: SkosUseCases = SkosService(quadStore)

    private val collectionId = "col-test"
    private val schemeUri = "http://example.org/scheme/animals"
    private val catUri = "http://example.org/concept/cat"
    private val dogUri = "http://example.org/concept/dog"

    @Test
    fun `getConceptSchemes returns empty list for unknown collection`() {
        val result = useCases.getConceptSchemes("no-such-collection")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getConcepts delegates to store`() {
        // Set up a scheme and two concepts in the store
        quadStore.insert(collectionId, StoredQuad(schemeUri, RDF_TYPE_URI, SkosTypes.CONCEPT_SCHEME, ""))
        quadStore.insert(collectionId, StoredQuad(catUri, RDF_TYPE_URI, SkosTypes.CONCEPT, ""))
        quadStore.insert(collectionId, StoredQuad(catUri, SkosTypes.PREF_LABEL, "Cat", "", ObjectType.LITERAL, language = "en"))
        quadStore.insert(collectionId, StoredQuad(catUri, SkosTypes.IN_SCHEME, schemeUri, ""))
        quadStore.insert(collectionId, StoredQuad(dogUri, RDF_TYPE_URI, SkosTypes.CONCEPT, ""))
        quadStore.insert(collectionId, StoredQuad(dogUri, SkosTypes.PREF_LABEL, "Dog", "", ObjectType.LITERAL, language = "en"))
        quadStore.insert(collectionId, StoredQuad(dogUri, SkosTypes.IN_SCHEME, schemeUri, ""))

        val result = useCases.getConcepts(collectionId, schemeUri)

        assertEquals(2, result.size)
    }

    @Test
    fun `getConcept returns null for unknown concept URI`() {
        val result = useCases.getConcept(collectionId, "http://example.org/concept/unknown")

        assertNull(result)
    }

    @Test
    fun `getConceptSchemes returns scheme when present`() {
        quadStore.insert(collectionId, StoredQuad(schemeUri, RDF_TYPE_URI, SkosTypes.CONCEPT_SCHEME, ""))
        quadStore.insert(collectionId, StoredQuad(schemeUri, SkosTypes.PREF_LABEL, "Animals", "", ObjectType.LITERAL, language = "en"))

        val result = useCases.getConceptSchemes(collectionId)

        assertEquals(1, result.size)
        assertEquals(schemeUri, result[0].uri)
    }
}
