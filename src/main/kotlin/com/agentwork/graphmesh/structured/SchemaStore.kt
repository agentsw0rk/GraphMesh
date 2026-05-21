package com.agentwork.graphmesh.structured

import com.agentwork.graphmesh.config.ConfigItem
import com.agentwork.graphmesh.config.ConfigType
import com.agentwork.graphmesh.config.application.port.`in`.ConfigUseCases
import com.agentwork.graphmesh.config.application.port.`in`.SaveConfigUseCase
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SchemaStore(
    private val configUseCases: ConfigUseCases,
    private val saveConfigUseCase: SaveConfigUseCase,
    private val objectMapper: ObjectMapper
) {

    fun save(schema: TableSchema): ConfigItem {
        val json = objectMapper.writeValueAsString(schema)
        val existing = configUseCases.findByTypeAndKey(ConfigType.SCHEMA, schema.name)
        val item = ConfigItem(
            id = existing?.id ?: UUID.randomUUID().toString(),
            type = ConfigType.SCHEMA,
            key = schema.name,
            value = json,
            description = schema.description ?: ""
        )
        return saveConfigUseCase.saveConfig(item)
    }

    fun load(name: String): TableSchema? {
        val item = configUseCases.findByTypeAndKey(ConfigType.SCHEMA, name) ?: return null
        return objectMapper.readValue(item.value, TableSchema::class.java)
    }

    fun listNames(): List<String> =
        configUseCases.findByType(ConfigType.SCHEMA).map { it.key }

    fun delete(name: String) {
        val item = configUseCases.findByTypeAndKey(ConfigType.SCHEMA, name) ?: return
        configUseCases.delete(item.id)
    }
}
