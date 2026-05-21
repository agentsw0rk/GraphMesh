package com.agentwork.graphmesh.ontology.application.port.`in`

import com.agentwork.graphmesh.ontology.Ontology
import com.agentwork.graphmesh.ontology.ValidationError

interface OntologyUseCases {
    fun get(key: String): Ontology?
    fun list(): List<String>
    fun delete(key: String)
    fun exportTurtle(key: String): String
    fun exportRdfXml(key: String): String
    fun validate(ontology: Ontology): List<ValidationError>
}
