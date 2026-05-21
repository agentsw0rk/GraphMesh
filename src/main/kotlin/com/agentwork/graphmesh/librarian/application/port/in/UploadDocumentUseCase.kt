package com.agentwork.graphmesh.librarian.application.port.`in`

import com.agentwork.graphmesh.librarian.Document

interface UploadDocumentUseCase {
    fun uploadDocument(
        collectionId: String,
        title: String,
        mimeType: String,
        content: ByteArray,
        metadata: Map<String, String> = emptyMap()
    ): Document
}
