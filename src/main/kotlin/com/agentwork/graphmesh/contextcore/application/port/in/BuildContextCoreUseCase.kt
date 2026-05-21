package com.agentwork.graphmesh.contextcore.application.port.`in`

import com.agentwork.graphmesh.contextcore.BuildRequest
import com.agentwork.graphmesh.contextcore.CoreManifest

interface BuildContextCoreUseCase {
    fun buildContextCore(request: BuildRequest): CoreManifest
}
