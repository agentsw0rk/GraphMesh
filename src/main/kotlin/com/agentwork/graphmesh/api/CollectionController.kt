package com.agentwork.graphmesh.api

import com.agentwork.graphmesh.collection.Collection
import com.agentwork.graphmesh.collection.application.port.`in`.CollectionUseCases
import com.agentwork.graphmesh.collection.application.port.`in`.CreateCollectionUseCase
import com.agentwork.graphmesh.collection.application.port.`in`.DeleteCollectionUseCase
import com.agentwork.graphmesh.collection.application.port.`in`.UpdateCollectionUseCase
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class CollectionController(
    private val collectionUseCases: CollectionUseCases,
    private val createCollectionUseCase: CreateCollectionUseCase,
    private val updateCollectionUseCase: UpdateCollectionUseCase,
    private val deleteCollectionUseCase: DeleteCollectionUseCase,
) {

    @QueryMapping
    fun collections(@Argument tags: List<String>?): List<Collection> {
        return collectionUseCases.findAll(tags?.toSet() ?: emptySet())
    }

    @QueryMapping
    fun collection(@Argument id: String): Collection? {
        return collectionUseCases.findById(id)
    }

    @SchemaMapping(typeName = "Collection", field = "metadata")
    fun metadata(collection: Collection): List<Map<String, String>> {
        return collection.metadata.map { (k, v) -> mapOf("key" to k, "value" to v) }
    }

    @MutationMapping
    fun createCollection(@Argument input: CreateCollectionInput): Collection {
        return createCollectionUseCase.createCollection(
            name = input.name,
            description = input.description ?: "",
            tags = input.tags?.toSet() ?: emptySet(),
            metadata = input.metadata?.associate { it.key to it.value } ?: emptyMap()
        )
    }

    @MutationMapping
    fun updateCollection(@Argument id: String, @Argument input: UpdateCollectionInput): Collection {
        return updateCollectionUseCase.updateCollection(
            id = id,
            name = input.name,
            description = input.description,
            tags = input.tags?.toSet(),
            metadata = input.metadata?.associate { it.key to it.value }
        )
    }

    @MutationMapping
    fun deleteCollection(@Argument id: String): Boolean {
        deleteCollectionUseCase.deleteCollection(id)
        return true
    }
}
