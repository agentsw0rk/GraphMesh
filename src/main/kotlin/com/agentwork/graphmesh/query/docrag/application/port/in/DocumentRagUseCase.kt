package com.agentwork.graphmesh.query.docrag.application.port.`in`

import com.agentwork.graphmesh.query.docrag.DocumentRagQuery
import com.agentwork.graphmesh.query.docrag.DocumentRagResult

interface DocumentRagUseCase {
    fun query(query: DocumentRagQuery): DocumentRagResult
}
