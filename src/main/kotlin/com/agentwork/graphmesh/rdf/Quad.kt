package com.agentwork.graphmesh.rdf

data class Triple(
    val subject: RdfTerm,
    val predicate: RdfTerm.Uri,
    val objectTerm: RdfTerm
) {
    fun toNTriples(): String =
        "${subject.toNTriples()} ${predicate.toNTriples()} ${objectTerm.toNTriples()} ."
}

data class Quad(
    val subject: RdfTerm,
    val predicate: RdfTerm.Uri,
    val objectTerm: RdfTerm,
    val graph: String = NamedGraph.DEFAULT
) {
    val triple: Triple get() = Triple(subject, predicate, objectTerm)

    fun toNQuads(): String =
        if (graph == NamedGraph.DEFAULT)
            "${subject.toNTriples()} ${predicate.toNTriples()} ${objectTerm.toNTriples()} ."
        else
            "${subject.toNTriples()} ${predicate.toNTriples()} ${objectTerm.toNTriples()} <$graph> ."
}

object NamedGraph {
    const val DEFAULT = ""
    const val SOURCE = "urn:graph:source"
    const val RETRIEVAL = "urn:graph:retrieval"

    /**
     * UI-Label fuer den RDF-Default-Graph. Das physische Storage-Format
     * bleibt der leere String (W3C-Semantik); diese Konstante erscheint
     * nur in API-Responses, damit die UI nie eine leere Dataset-Spalte zeigt.
     */
    const val DEFAULT_DISPLAY = "urn:graph:default"

    fun isStandardGraph(graph: String): Boolean =
        graph in setOf(DEFAULT, SOURCE, RETRIEVAL)

    /** Storage-Wert -> UI-Label. */
    fun displayName(graph: String): String =
        if (graph.isBlank()) DEFAULT_DISPLAY else graph

    /** UI-Label -> Storage-Wert (symmetrische Umkehrung fuer Filter). */
    fun fromDisplay(graph: String?): String? = when (graph) {
        null -> null
        DEFAULT_DISPLAY -> DEFAULT
        else -> graph
    }
}
