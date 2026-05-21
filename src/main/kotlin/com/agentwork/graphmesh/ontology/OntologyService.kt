package com.agentwork.graphmesh.ontology

import com.agentwork.graphmesh.ontology.application.port.`in`.ImportRdfXmlOntologyUseCase
import com.agentwork.graphmesh.ontology.application.port.`in`.ImportTurtleOntologyUseCase
import com.agentwork.graphmesh.ontology.application.port.`in`.OntologyUseCases
import com.agentwork.graphmesh.ontology.application.port.`in`.SaveOntologyUseCase
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OntologyService(
    private val store: OntologyStore,
    private val cache: OntologyCache,
    private val validator: DefaultOntologyValidator,
    private val jenaAdapter: JenaAdapter
) : OntologyUseCases, SaveOntologyUseCase, ImportTurtleOntologyUseCase, ImportRdfXmlOntologyUseCase {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun saveOntology(key: String, ontology: Ontology): List<ValidationError> {
        val errors = validator.validate(ontology)
        if (errors.isNotEmpty()) {
            logger.warn("Ontology '{}' has {} validation errors, not saving", key, errors.size)
            return errors
        }
        store.save(key, ontology)
        logger.info("Ontology '{}' saved (version {})", key, ontology.metadata.version)
        return emptyList()
    }

    override fun get(key: String): Ontology? = cache.get(key)

    override fun list(): List<String> = store.listKeys()

    override fun delete(key: String) {
        store.delete(key)
        logger.info("Ontology '{}' deleted", key)
    }

    override fun importTurtle(key: String, content: String, metadata: OntologyMetadata): Ontology {
        val model = jenaAdapter.parseTurtle(content)
        val ontology = jenaAdapter.fromJenaModel(model, metadata)
        val errors = validator.validate(ontology)
        require(errors.isEmpty()) { "Imported Turtle ontology has validation errors: $errors" }
        store.save(key, ontology)
        logger.info("Ontology '{}' imported from Turtle", key)
        return ontology
    }

    override fun exportTurtle(key: String): String {
        val ontology = cache.get(key)
            ?: throw IllegalArgumentException("Ontology '$key' not found")
        val model = jenaAdapter.toJenaModel(ontology)
        return jenaAdapter.serializeTurtle(model)
    }

    override fun importRdfXml(key: String, content: String, metadata: OntologyMetadata): Ontology {
        val model = jenaAdapter.parseRdfXml(content)
        val ontology = jenaAdapter.fromJenaModel(model, metadata)
        val errors = validator.validate(ontology)
        require(errors.isEmpty()) { "Imported RDF/XML ontology has validation errors: $errors" }
        store.save(key, ontology)
        logger.info("Ontology '{}' imported from RDF/XML", key)
        return ontology
    }

    override fun exportRdfXml(key: String): String {
        val ontology = cache.get(key)
            ?: throw IllegalArgumentException("Ontology '$key' not found")
        val model = jenaAdapter.toJenaModel(ontology)
        return jenaAdapter.serializeRdfXml(model)
    }

    override fun validate(ontology: Ontology): List<ValidationError> = validator.validate(ontology)
}
