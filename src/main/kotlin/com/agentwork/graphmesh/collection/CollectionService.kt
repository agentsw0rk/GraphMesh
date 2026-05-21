package com.agentwork.graphmesh.collection

import com.agentwork.graphmesh.collection.application.port.`in`.CollectionUseCases
import com.agentwork.graphmesh.collection.application.port.`in`.CreateCollectionUseCase
import com.agentwork.graphmesh.collection.application.port.`in`.DeleteCollectionUseCase
import com.agentwork.graphmesh.collection.application.port.`in`.UpdateCollectionUseCase
import com.agentwork.graphmesh.tenant.AccessDeniedException
import com.agentwork.graphmesh.tenant.TenantContext
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class CollectionService(
    private val collectionStore: CollectionStore,
    private val lifecycleManager: CollectionLifecycleManager,
    private val eventPublisher: ApplicationEventPublisher,
    private val collectionEventProducer: CollectionEventProducer,
) : CollectionUseCases, CreateCollectionUseCase, UpdateCollectionUseCase, DeleteCollectionUseCase {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun createCollection(
        name: String,
        description: String,
        tags: Set<String>,
        metadata: Map<String, String>
    ): Collection {
        require(collectionStore.findByName(name) == null) {
            "Collection with name '$name' already exists"
        }

        val tenant = TenantContext.getOrNull()

        val collection = Collection(
            name = name,
            description = description,
            tags = tags,
            metadata = metadata,
            tenantId = tenant?.tenantId,
            ownerId = tenant?.userId
        )

        collectionStore.save(collection)

        val event = CollectionEvent(CollectionEventType.CREATED, collection.id, collection.name)
        eventPublisher.publishEvent(event)
        collectionEventProducer.send(event)

        logger.info("Collection created: id={}, name={}, tenantId={}", collection.id, collection.name, collection.tenantId)
        return collection
    }

    override fun deleteCollection(id: String) {
        val collection = findByIdWithAccessCheck(id)

        val result = lifecycleManager.purge(id)
        if (!result.complete) {
            logger.warn("Partial purge for collection {}: {}", id, result.failures)
        }

        val event = CollectionEvent(CollectionEventType.DELETED, id, collection.name)
        eventPublisher.publishEvent(event)
        collectionEventProducer.send(event)

        logger.info("Collection deleted (cascade): id={}, name={}", id, collection.name)
    }

    override fun updateCollection(
        id: String,
        name: String?,
        description: String?,
        tags: Set<String>?,
        metadata: Map<String, String>?
    ): Collection {
        val existing = findByIdWithAccessCheck(id)

        if (name != null && name != existing.name) {
            require(collectionStore.findByName(name) == null) {
                "Collection with name '$name' already exists"
            }
        }

        val updated = existing.copy(
            name = name ?: existing.name,
            description = description ?: existing.description,
            tags = tags ?: existing.tags,
            metadata = metadata ?: existing.metadata,
            updatedAt = Instant.now()
        )

        collectionStore.save(updated)

        val event = CollectionEvent(CollectionEventType.UPDATED, id, updated.name)
        eventPublisher.publishEvent(event)
        collectionEventProducer.send(event)

        logger.info("Collection updated: id={}, name={}", id, updated.name)
        return updated
    }

    override fun findById(id: String): Collection? {
        val collection = collectionStore.findById(id) ?: return null
        checkTenantAccess(collection)
        return collection
    }

    override fun findByName(name: String): Collection? {
        val collection = collectionStore.findByName(name) ?: return null
        checkTenantAccess(collection)
        return collection
    }

    override fun findAll(tags: Set<String>): List<Collection> {
        var all = collectionStore.findAll()

        val tenant = TenantContext.getOrNull()
        if (tenant != null) {
            all = all.filter { it.tenantId == null || it.tenantId == tenant.tenantId }
        }

        if (tags.isNotEmpty()) {
            all = all.filter { it.tags.containsAll(tags) }
        }
        return all
    }

    override fun requireExists(id: String) {
        if (!collectionStore.exists(id)) {
            throw CollectionNotFoundException(id)
        }
    }

    private fun findByIdWithAccessCheck(id: String): Collection {
        val collection = collectionStore.findById(id)
            ?: throw CollectionNotFoundException(id)
        checkTenantAccess(collection)
        return collection
    }

    private fun checkTenantAccess(collection: Collection) {
        val tenant = TenantContext.getOrNull() ?: return
        if (collection.tenantId != null && collection.tenantId != tenant.tenantId) {
            throw AccessDeniedException(
                "Tenant '${tenant.tenantId}' has no access to collection '${collection.id}'."
            )
        }
    }

}
