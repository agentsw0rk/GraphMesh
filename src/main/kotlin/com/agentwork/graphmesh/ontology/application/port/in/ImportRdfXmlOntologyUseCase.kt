package com.agentwork.graphmesh.ontology.application.port.`in`

import com.agentwork.graphmesh.ontology.Ontology
import com.agentwork.graphmesh.ontology.OntologyMetadata

interface ImportRdfXmlOntologyUseCase {
    fun importRdfXml(key: String, content: String, metadata: OntologyMetadata): Ontology
}
