package com.agentwork.graphmesh.query.nlp.application.port.`in`

import com.agentwork.graphmesh.query.nlp.NlpQuery
import com.agentwork.graphmesh.query.nlp.NlpQueryResult

interface NlpQueryUseCase {
    fun query(query: NlpQuery): NlpQueryResult
}
