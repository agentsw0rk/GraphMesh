package com.agentwork.graphmesh.agent.application.port.`in`

import com.agentwork.graphmesh.agent.AgentQueryConfig
import com.agentwork.graphmesh.agent.AgentQueryResult
import com.agentwork.graphmesh.agent.ToolGroup
import com.agentwork.graphmesh.agent.ToolInfo

interface AgentUseCases {
    fun query(
        question: String,
        collectionId: String,
        config: AgentQueryConfig = AgentQueryConfig(),
        allowedGroups: Set<String> = setOf("all")
    ): AgentQueryResult

    fun getAvailableTools(): List<ToolInfo>
    fun getToolGroups(): List<ToolGroup>
}
