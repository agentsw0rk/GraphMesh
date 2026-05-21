package com.agentwork.graphmesh.structured.application.port.`in`

import com.agentwork.graphmesh.structured.DataRow
import com.agentwork.graphmesh.structured.QueryResult
import com.agentwork.graphmesh.structured.StoreResult
import com.agentwork.graphmesh.structured.StructuredQuery
import com.agentwork.graphmesh.structured.TableSchema

interface StructuredDataUseCases {
    fun store(row: DataRow): StoreResult
    fun storeBatch(rows: List<DataRow>): List<StoreResult>
    fun query(query: StructuredQuery): QueryResult
    fun saveSchema(schema: TableSchema)
    fun getSchema(name: String): TableSchema?
    fun listSchemas(): List<String>
    fun deleteSchema(name: String)
}
