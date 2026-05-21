package com.agentwork.graphmesh.config

import com.agentwork.graphmesh.config.application.port.`in`.ConfigUseCases
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ConfigUseCasesTest {

    private val store = mockk<ConfigStore>(relaxed = true)
    private val eventPublisher = org.springframework.context.ApplicationEventPublisher { }
    private val configChangeProducer = mockk<ConfigChangeProducer>(relaxed = true)

    // Assigned to interface type — proves ConfigService satisfies ConfigUseCases
    private val useCases: ConfigUseCases = ConfigService(
        store = store,
        eventPublisher = eventPublisher,
        configChangeProducer = configChangeProducer
    )

    @Test
    fun `findById returns null when not found`() {
        every { store.findById("unknown") } returns null

        assertNull(useCases.findById("unknown"))
    }

    @Test
    fun `findAll returns all config items`() {
        val paramItem = ConfigItem(id = "1", type = ConfigType.PARAMETER, key = "k1", value = "v1")
        val ontologyItem = ConfigItem(id = "2", type = ConfigType.ONTOLOGY, key = "k2", value = "v2")
        // findAll iterates over every ConfigType; return items for two, empty for rest
        every { store.findByType(ConfigType.PARAMETER) } returns listOf(paramItem)
        every { store.findByType(ConfigType.ONTOLOGY) } returns listOf(ontologyItem)
        every { store.findByType(ConfigType.FLOW) } returns emptyList()
        every { store.findByType(ConfigType.TOOL) } returns emptyList()
        every { store.findByType(ConfigType.COLLECTION_SETTINGS) } returns emptyList()
        every { store.findByType(ConfigType.LLM_SETTINGS) } returns emptyList()
        every { store.findByType(ConfigType.SCHEMA) } returns emptyList()

        val result = useCases.findAll()

        assertEquals(2, result.size)
    }

    @Test
    fun `findById returns item when it exists`() {
        val item = ConfigItem(id = "cfg-1", type = ConfigType.PARAMETER, key = "max-tokens", value = "4096")
        every { store.findById("cfg-1") } returns item

        val result = useCases.findById("cfg-1")

        assertEquals("cfg-1", result?.id)
        assertEquals("4096", result?.value)
    }

    @Test
    fun `findByType returns items of that type`() {
        val items = listOf(
            ConfigItem(id = "1", type = ConfigType.PARAMETER, key = "max-tokens", value = "4096")
        )
        every { store.findByType(ConfigType.PARAMETER) } returns items

        val result = useCases.findByType(ConfigType.PARAMETER)

        assertEquals(1, result.size)
        assertEquals("max-tokens", result[0].key)
    }
}
