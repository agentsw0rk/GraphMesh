package com.agentwork.graphmesh.collection.application.port.`in`

import com.agentwork.graphmesh.collection.Collection

interface CollectionUseCases {
    fun findById(id: String): Collection?
    fun findByName(name: String): Collection?
    fun findAll(tags: Set<String> = emptySet()): List<Collection>
    fun requireExists(id: String)
}
