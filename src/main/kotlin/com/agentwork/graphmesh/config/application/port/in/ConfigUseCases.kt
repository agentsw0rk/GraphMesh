package com.agentwork.graphmesh.config.application.port.`in`

import com.agentwork.graphmesh.config.ConfigItem
import com.agentwork.graphmesh.config.ConfigType

interface ConfigUseCases {
    fun findById(id: String): ConfigItem?
    fun findByType(type: ConfigType): List<ConfigItem>
    fun findByTypeAndKey(type: ConfigType, key: String): ConfigItem?
    fun findAll(type: ConfigType? = null): List<ConfigItem>
    fun history(id: String, limit: Int = 10): List<ConfigItem>
    fun delete(id: String)
}
