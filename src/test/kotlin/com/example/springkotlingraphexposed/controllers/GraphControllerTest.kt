package com.example.springkotlingraphexposed.controllers

import com.example.springkotlingraphexposed.WithTestDatabase
import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.views.graphs.GraphView
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GraphControllerTest : WithTestDatabase() {
    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    private val url = "/api/graphs"

    @Test
    fun respondsToIndexAction() {
        withDb {
            Graph.new {
                name = "Test Graph"
            }
            println(Graph.count())
            val response = testRestTemplate.getForEntity<List<GraphView>>(url)
            Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            println(response.body)
        }
    }
}
