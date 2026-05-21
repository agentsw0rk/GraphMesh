package com.agentwork.graphmesh.collection.application.port.`in`

import com.agentwork.graphmesh.collection.Collection

interface UpdateCollectionUseCase {
    fun updateCollection(
        id: String,
        name: String? = null,
        description: String? = null,
        tags: Set<String>? = null,
        metadata: Map<String, String>? = null
    ): Collection
}
