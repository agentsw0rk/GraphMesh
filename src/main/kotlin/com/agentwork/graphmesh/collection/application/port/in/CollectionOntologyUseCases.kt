package com.agentwork.graphmesh.collection.application.port.`in`

import com.agentwork.graphmesh.collection.CollectionOntologyRecord

interface CollectionOntologyUseCases {
    fun assign(collectionId: String, ontologyKey: String, role: String, assignedBy: String): CollectionOntologyRecord
    fun unassign(collectionId: String, ontologyKey: String)
    fun listForCollection(collectionId: String): List<CollectionOntologyRecord>
}
