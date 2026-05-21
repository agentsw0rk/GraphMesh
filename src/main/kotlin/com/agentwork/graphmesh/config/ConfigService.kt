package com.agentwork.graphmesh.config

import com.agentwork.graphmesh.config.application.port.`in`.ConfigUseCases
import com.agentwork.graphmesh.config.application.port.`in`.SaveConfigUseCase
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ConfigService(
    private val store: ConfigStore,
    private val eventPublisher: ApplicationEventPublisher,
    private val configChangeProducer: ConfigChangeProducer
) : ConfigUseCases, SaveConfigUseCase {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun saveConfig(item: ConfigItem): ConfigItem {
        val existing = store.findById(item.id)
        val action = if (existing == null) ConfigAction.CREATED else ConfigAction.UPDATED
        val version = (existing?.version ?: 0) + 1

        val saved = store.save(item.copy(
            version = version,
            updatedAt = Instant.now(),
            createdAt = existing?.createdAt ?: item.createdAt
        ))

        val event = ConfigChangedEvent(saved.id, saved.type, saved.key, action, saved.version)
        eventPublisher.publishEvent(event)
        configChangeProducer.send(event)

        logger.info("Config {} {}: type={}, key={}, version={}", saved.id, action, saved.type, saved.key, saved.version)
        return saved
    }

    override fun findById(id: String): ConfigItem? = store.findById(id)

    override fun findByType(type: ConfigType): List<ConfigItem> = store.findByType(type)

    override fun findByTypeAndKey(type: ConfigType, key: String): ConfigItem? = store.findByTypeAndKey(type, key)

    /**
     * Returns all config items across every [ConfigType].
     * If [type] is non-null, restricts results to that type.
     */
    override fun findAll(type: ConfigType?): List<ConfigItem> {
        if (type != null) return store.findByType(type)
        return ConfigType.values().flatMap { store.findByType(it) }
    }

    override fun history(id: String, limit: Int): List<ConfigItem> = store.history(id, limit)

    override fun delete(id: String) {
        val item = store.findById(id) ?: return
        store.delete(id)

        val event = ConfigChangedEvent(item.id, item.type, item.key, ConfigAction.DELETED, item.version)
        eventPublisher.publishEvent(event)
        configChangeProducer.send(event)

        logger.info("Config {} DELETED: type={}, key={}", item.id, item.type, item.key)
    }
}
