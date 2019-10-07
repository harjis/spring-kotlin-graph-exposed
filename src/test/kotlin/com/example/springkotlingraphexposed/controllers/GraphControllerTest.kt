package com.example.springkotlingraphexposed.controllers

import com.example.springkotlingraphexposed.WithTestDatabase
import com.example.springkotlingraphexposed.app.controllers.GraphsController
import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.models.render
import com.example.springkotlingraphexposed.app.services.graphs.GraphSaveService
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ActiveProfiles("test")
@WebMvcTest(controllers = [GraphsController::class])
class GraphControllerTest : WithTestDatabase() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var graphSaveService: GraphSaveService

    private val url = "/api/graphs"

    @Test
    fun respondsToIndexAction() {
        withDb {
            val graph = Graph.new {
                name = "Test"
            }
            val result = mockMvc
                    .perform(MockMvcRequestBuilders.get(url))
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andReturn()

            val expected = objectMapper.writeValueAsString(listOf(graph.render()))
            Assertions.assertThat(result.response.contentAsString).isEqualTo(expected)
        }
    }
}
