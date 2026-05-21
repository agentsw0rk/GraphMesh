package com.agentwork.graphmesh.api

import com.agentwork.graphmesh.collection.application.port.`in`.CollectionOntologyUseCases
import com.agentwork.graphmesh.ontology.OntologyInfoPayload
import com.agentwork.graphmesh.ontology.OntologyService
import com.agentwork.graphmesh.rdf.NamedGraph
import com.agentwork.graphmesh.storage.QuadStore
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class CollectionDataController(
    private val collectionOntologyService: CollectionOntologyUseCases,
    private val quadStore: QuadStore,
    private val ontologyService: OntologyService
) {

    @QueryMapping
    fun collectionOntologies(@Argument collectionId: String): List<CollectionOntologyPayload> =
        collectionOntologyService.listForCollection(collectionId).map { record ->
            val ontology = ontologyService.get(record.ontologyKey)
            CollectionOntologyPayload(
                ontologyKey = record.ontologyKey,
                role = record.role,
                assignedAt = record.assignedAt.toString(),
                assignedBy = record.assignedBy,
                ontology = ontology?.let {
                    OntologyInfoPayload(
                        key = record.ontologyKey,
                        name = it.metadata.name,
                        namespace = it.metadata.namespace,
                        version = it.metadata.version,
                        classCount = it.classes.size,
                        objectPropertyCount = it.objectProperties.size,
                        datatypePropertyCount = it.datatypeProperties.size,
                    )
                }
            )
        }

    @QueryMapping
    fun collectionDataStats(@Argument collectionId: String): CollectionDataStatsPayload {
        val stats = quadStore.stats(collectionId)
        return CollectionDataStatsPayload(
            tripleCount = stats.tripleCount.toInt(),
            entityCount = stats.entityCount.toInt(),
            predicateCount = stats.predicateCount.toInt(),
            datasets = stats.datasets.map { NamedGraph.displayName(it) }
        )
    }

    @MutationMapping
    fun assignOntology(
        @Argument collectionId: String,
        @Argument ontologyKey: String,
        @Argument role: String
    ): CollectionOntologyPayload {
        val record = collectionOntologyService.assign(collectionId, ontologyKey, role, "system")
        val ontology = ontologyService.get(ontologyKey)
        return CollectionOntologyPayload(
            ontologyKey = record.ontologyKey,
            role = record.role,
            assignedAt = record.assignedAt.toString(),
            assignedBy = record.assignedBy,
            ontology = ontology?.let {
                OntologyInfoPayload(
                    key = record.ontologyKey,
                    name = it.metadata.name,
                    namespace = it.metadata.namespace,
                    version = it.metadata.version,
                    classCount = it.classes.size,
                    objectPropertyCount = it.objectProperties.size,
                    datatypePropertyCount = it.datatypeProperties.size,
                )
            }
        )
    }

    @MutationMapping
    fun unassignOntology(@Argument collectionId: String, @Argument ontologyKey: String): Boolean {
        collectionOntologyService.unassign(collectionId, ontologyKey)
        return true
    }

    @MutationMapping
    fun deleteTriples(@Argument collectionId: String, @Argument dataset: String?): Int {
        val storageDataset = NamedGraph.fromDisplay(dataset)
        return if (storageDataset != null) {
            quadStore.deleteByDataset(collectionId, storageDataset).toInt()
        } else {
            quadStore.deleteCollection(collectionId)
            0
        }
    }
}

data class CollectionOntologyPayload(
    val ontologyKey: String,
    val role: String,
    val assignedAt: String,
    val assignedBy: String,
    val ontology: OntologyInfoPayload?
)

data class CollectionDataStatsPayload(
    val tripleCount: Int,
    val entityCount: Int,
    val predicateCount: Int,
    val datasets: List<String>
)
