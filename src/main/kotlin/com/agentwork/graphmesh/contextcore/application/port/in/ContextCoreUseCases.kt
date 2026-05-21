package com.agentwork.graphmesh.contextcore.application.port.`in`

import com.agentwork.graphmesh.contextcore.CoreManifest

interface ContextCoreUseCases {
    fun list(): List<CoreManifest>
    fun find(coreId: String, version: String): CoreManifest?
    fun findByTag(coreId: String, tag: String): CoreManifest?
    fun delete(coreId: String, version: String)
    fun tag(coreId: String, version: String, tag: String): CoreManifest?
}
