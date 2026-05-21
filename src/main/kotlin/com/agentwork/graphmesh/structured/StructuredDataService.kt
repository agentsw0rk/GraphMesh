package com.agentwork.graphmesh.structured

import com.agentwork.graphmesh.structured.application.port.`in`.StructuredDataUseCases
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StructuredDataService(
    private val schemaStore: SchemaStore,
    private val rowStore: CassandraRowStore
) : StructuredDataUseCases {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun store(row: DataRow): StoreResult {
        val schema = schemaStore.load(row.schemaName)
            ?: return StoreResult(success = false, error = "Schema '${row.schemaName}' not found")

        val validationError = validateRow(row, schema)
        if (validationError != null) {
            return StoreResult(success = false, error = validationError)
        }

        rowStore.insert(row)
        return StoreResult(success = true, rowsWritten = 1)
    }

    override fun storeBatch(rows: List<DataRow>): List<StoreResult> {
        if (rows.isEmpty()) return emptyList()

        val results = mutableListOf<StoreResult>()
        val validRows = mutableListOf<DataRow>()

        for (row in rows) {
            val schema = schemaStore.load(row.schemaName)
            if (schema == null) {
                results.add(StoreResult(success = false, error = "Schema '${row.schemaName}' not found"))
                continue
            }

            val validationError = validateRow(row, schema)
            if (validationError != null) {
                results.add(StoreResult(success = false, error = validationError))
                continue
            }

            validRows.add(row)
            results.add(StoreResult(success = true, rowsWritten = 1))
        }

        if (validRows.isNotEmpty()) {
            rowStore.insertBatch(validRows)
        }

        return results
    }

    override fun query(query: StructuredQuery): QueryResult = rowStore.query(query)

    override fun saveSchema(schema: TableSchema) {
        schemaStore.save(schema)
        logger.info("Schema '{}' saved", schema.name)
    }

    override fun getSchema(name: String): TableSchema? = schemaStore.load(name)

    override fun listSchemas(): List<String> = schemaStore.listNames()

    override fun deleteSchema(name: String) {
        schemaStore.delete(name)
        logger.info("Schema '{}' deleted", name)
    }

    internal fun validateRow(row: DataRow, schema: TableSchema): String? {
        // Check required (non-nullable) columns have values
        val requiredColumns = schema.columns.filter { !it.nullable }
        for (col in requiredColumns) {
            if (row.values[col.name].isNullOrBlank()) {
                return "Required column '${col.name}' is missing or blank"
            }
        }

        // Check at least one PK column has a value
        val pkColumns = schema.primaryKeyColumns
        if (pkColumns.isNotEmpty() && pkColumns.all { row.values[it.name].isNullOrBlank() }) {
            return "At least one primary key column must have a value"
        }

        return null
    }
}
