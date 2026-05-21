package com.agentwork.graphmesh.provenance.application.port.`in`

import com.agentwork.graphmesh.provenance.SubgraphProvenance
import com.agentwork.graphmesh.rdf.Quad

interface ProvenanceUseCases {
    fun buildSubgraphQuads(provenance: SubgraphProvenance): List<Quad>
}
