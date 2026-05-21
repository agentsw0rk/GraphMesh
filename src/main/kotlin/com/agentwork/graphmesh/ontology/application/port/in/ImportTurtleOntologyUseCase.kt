package com.agentwork.graphmesh.ontology.application.port.`in`

import com.agentwork.graphmesh.ontology.Ontology
import com.agentwork.graphmesh.ontology.OntologyMetadata

interface ImportTurtleOntologyUseCase {
    fun importTurtle(key: String, content: String, metadata: OntologyMetadata): Ontology
}
