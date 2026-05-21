package com.agentwork.graphmesh.collection.application.port.`in`

import com.agentwork.graphmesh.collection.Collection

interface CreateCollectionUseCase {
    fun createCollection(
        name: String,
        description: String = "",
        tags: Set<String> = emptySet(),
        metadata: Map<String, String> = emptyMap()
    ): Collection
}
