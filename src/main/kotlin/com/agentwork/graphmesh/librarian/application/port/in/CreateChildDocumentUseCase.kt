package com.agentwork.graphmesh.librarian.application.port.`in`

import com.agentwork.graphmesh.librarian.Document
import com.agentwork.graphmesh.librarian.DocumentType

interface CreateChildDocumentUseCase {
    fun createChildDocument(
        parentId: String,
        type: DocumentType,
        title: String,
        content: ByteArray,
        mimeType: String = "text/plain"
    ): Document
}
