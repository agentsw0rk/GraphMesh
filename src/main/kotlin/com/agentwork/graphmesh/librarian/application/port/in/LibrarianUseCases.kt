package com.agentwork.graphmesh.librarian.application.port.`in`

import com.agentwork.graphmesh.librarian.Document
import com.agentwork.graphmesh.librarian.DocumentFilterCriteria
import com.agentwork.graphmesh.librarian.DocumentPageResult
import com.agentwork.graphmesh.librarian.DocumentState
import com.agentwork.graphmesh.librarian.DocumentType

interface LibrarianUseCases {
    fun getContent(documentId: String): ByteArray
    fun findById(id: String): Document?
    fun findByCollection(collectionId: String, type: DocumentType? = null): List<Document>
    fun findByCollectionPaginated(
        collectionId: String,
        filter: DocumentFilterCriteria,
        page: Int,
        pageSize: Int
    ): DocumentPageResult
    fun findChunksOf(documentId: String): List<Document>
    fun findChildren(parentId: String): List<Document>
    fun updateState(documentId: String, state: DocumentState)
}
