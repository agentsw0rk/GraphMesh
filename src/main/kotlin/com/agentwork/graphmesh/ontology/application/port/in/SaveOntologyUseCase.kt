package com.agentwork.graphmesh.ontology.application.port.`in`

import com.agentwork.graphmesh.ontology.Ontology
import com.agentwork.graphmesh.ontology.ValidationError

interface SaveOntologyUseCase {
    fun saveOntology(key: String, ontology: Ontology): List<ValidationError>
}
