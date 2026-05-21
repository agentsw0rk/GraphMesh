package com.agentwork.graphmesh.storage.application.port.`in`

data class OrphanSweepResult(
    val quadTablesTruncated: Int,
    val vectorCollectionsDeleted: Int,
    val blobsDeleted: Int,
)

interface OrphanSweepUseCase {
    fun sweep(): OrphanSweepResult
}
