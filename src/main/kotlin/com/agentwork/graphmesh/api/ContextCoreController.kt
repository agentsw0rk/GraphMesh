package com.agentwork.graphmesh.api

import com.agentwork.graphmesh.contextcore.*
import com.agentwork.graphmesh.contextcore.application.port.`in`.BuildContextCoreUseCase
import com.agentwork.graphmesh.contextcore.application.port.`in`.ContextCoreUseCases
import com.agentwork.graphmesh.contextcore.application.port.`in`.ImportContextCoreUseCase
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class ContextCoreController(
    private val contextCoreUseCases: ContextCoreUseCases,
    private val buildContextCoreUseCase: BuildContextCoreUseCase,
    private val importContextCoreUseCase: ImportContextCoreUseCase
) {

    @QueryMapping
    fun contextCores(): List<CoreManifest> = contextCoreUseCases.list()

    @QueryMapping
    fun contextCore(@Argument coreId: String, @Argument version: String): CoreManifest? =
        contextCoreUseCases.find(coreId, version)

    @QueryMapping
    fun contextCoreByTag(@Argument coreId: String, @Argument tag: String): CoreManifest? =
        contextCoreUseCases.findByTag(coreId, tag)

    @MutationMapping
    fun buildContextCore(
        @Argument coreId: String,
        @Argument version: String,
        @Argument sourceCollection: String,
        @Argument description: String?,
        @Argument tags: List<String>?,
        @Argument embeddingModel: String?,
        @Argument embeddingDimension: Int?,
        @Argument ontologyKey: String?
    ): CoreManifest {
        val request = BuildRequest(
            coreId = coreId,
            version = version,
            sourceCollection = sourceCollection,
            embeddingModel = embeddingModel ?: "text-embedding-3-small",
            embeddingDimension = embeddingDimension ?: 1536,
            description = description,
            tags = tags?.toSet() ?: emptySet(),
            ontologyKey = ontologyKey
        )
        return buildContextCoreUseCase.buildContextCore(request)
    }

    @MutationMapping
    fun importContextCore(
        @Argument coreId: String,
        @Argument version: String,
        @Argument targetCollection: String,
        @Argument strategy: ConflictStrategy,
        @Argument namespaceFrom: String?,
        @Argument namespaceTo: String?
    ): ImportResult {
        val namespaceRewrite = if (namespaceFrom != null && namespaceTo != null) {
            NamespaceRewrite(namespaceFrom, namespaceTo)
        } else null

        return importContextCoreUseCase.importContextCore(ImportRequest(
            coreId = coreId,
            version = version,
            targetCollection = targetCollection,
            strategy = strategy,
            namespaceRewrite = namespaceRewrite
        ))
    }

    @MutationMapping
    fun tagContextCore(@Argument coreId: String, @Argument version: String, @Argument tag: String): CoreManifest? =
        contextCoreUseCases.tag(coreId, version, tag)

    @MutationMapping
    fun deleteContextCore(@Argument coreId: String, @Argument version: String): Boolean {
        contextCoreUseCases.delete(coreId, version)
        return true
    }
}
