package com.agentwork.graphmesh.skos.application.port.`in`

import com.agentwork.graphmesh.skos.SkosConcept
import com.agentwork.graphmesh.skos.SkosConceptScheme

interface SkosUseCases {
    fun getConceptSchemes(collectionId: String): List<SkosConceptScheme>
    fun getConcepts(collectionId: String, schemeUri: String): List<SkosConcept>
    fun getTopConcepts(collectionId: String, schemeUri: String): List<SkosConcept>
    fun getNarrower(collectionId: String, conceptUri: String): List<SkosConcept>
    fun getBroader(collectionId: String, conceptUri: String): List<SkosConcept>
    fun getRelated(collectionId: String, conceptUri: String): List<SkosConcept>
    fun findByLabel(collectionId: String, label: String): List<SkosConcept>
    fun getConcept(collectionId: String, conceptUri: String): SkosConcept?
    fun countConcepts(collectionId: String, schemeUri: String): Int
}
