package com.agentwork.graphmesh.config.application.port.`in`

import com.agentwork.graphmesh.config.ConfigItem

interface SaveConfigUseCase {
    fun saveConfig(item: ConfigItem): ConfigItem
}
