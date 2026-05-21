package com.agentwork.graphmesh.contextcore.application.port.`in`

import com.agentwork.graphmesh.contextcore.ImportRequest
import com.agentwork.graphmesh.contextcore.ImportResult

interface ImportContextCoreUseCase {
    fun importContextCore(request: ImportRequest): ImportResult
}
