package com.agentwork.graphmesh.query.graphrag.application.port.`in`

import com.agentwork.graphmesh.query.graphrag.GraphRagQuery
import com.agentwork.graphmesh.query.graphrag.GraphRagResult

interface GraphRagUseCase {
    fun query(query: GraphRagQuery): GraphRagResult
}
