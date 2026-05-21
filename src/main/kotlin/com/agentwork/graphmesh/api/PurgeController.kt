package com.agentwork.graphmesh.api

import com.agentwork.graphmesh.storage.application.port.`in`.OrphanSweepResult
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

@Controller
class PurgeController(
    private val purgeService: PurgeService,
) {

    @MutationMapping
    fun purgeAllData(): PurgeService.PurgeResult = purgeService.purgeAll()

    @MutationMapping
    fun purgeOrphans(): OrphanSweepResult = purgeService.purgeOrphansOnly()
}
