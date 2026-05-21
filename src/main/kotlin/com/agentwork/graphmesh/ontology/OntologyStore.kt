package com.agentwork.graphmesh.ontology

import com.agentwork.graphmesh.config.ConfigItem
import com.agentwork.graphmesh.config.ConfigType
import com.agentwork.graphmesh.config.application.port.`in`.ConfigUseCases
import com.agentwork.graphmesh.config.application.port.`in`.SaveConfigUseCase
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class OntologyStore(
    private val configUseCases: ConfigUseCases,
    private val saveConfigUseCase: SaveConfigUseCase,
    private val objectMapper: ObjectMapper
) {

    fun save(key: String, ontology: Ontology): ConfigItem {
        val json = objectMapper.writeValueAsString(ontology)
        val existing = configUseCases.findByTypeAndKey(ConfigType.ONTOLOGY, key)
        val item = ConfigItem(
            id = existing?.id ?: UUID.randomUUID().toString(),
            type = ConfigType.ONTOLOGY,
            key = key,
            value = json,
            description = ontology.metadata.description ?: ""
        )
        return saveConfigUseCase.saveConfig(item)
    }

    fun load(key: String): Ontology? {
        val item = configUseCases.findByTypeAndKey(ConfigType.ONTOLOGY, key) ?: return null
        return objectMapper.readValue(item.value, Ontology::class.java)
    }

    fun listKeys(): List<String> =
        configUseCases.findByType(ConfigType.ONTOLOGY).map { it.key }

    fun delete(key: String) {
        val item = configUseCases.findByTypeAndKey(ConfigType.ONTOLOGY, key) ?: return
        configUseCases.delete(item.id)
    }
}
