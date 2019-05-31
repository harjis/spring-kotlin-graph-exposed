package com.example.springkotlingraphexposed.services.graphs

import com.example.springkotlingraphexposed.WithTestDatabase
import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.services.graphs.GraphParams
import com.example.springkotlingraphexposed.app.services.graphs.GraphSaveService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GraphSaveServiceTest : WithTestDatabase() {
    val graphSaveService: GraphSaveService = GraphSaveService()

    @Test
    fun canSaveGraph() {
        withDb {
            val params = GraphParams(name = "Graph 1")
            val saved = graphSaveService.save(params)
            assertThat(saved.id).isNotNull()
            assertThat(saved.name).isEqualTo(params.name)
            assertThat(Graph.count()).isEqualTo(1)
        }
    }

    @Test
    fun canUpdateGraph() {
        withDb {
            val params = GraphParams(name = "Graph 1")
            val saved = graphSaveService.save(params)
            val updatedParams = GraphParams(id = saved.id.value, name = "Updated Graph 1")
            val updated = graphSaveService.save(updatedParams)
            assertThat(updated.name).isEqualTo(updatedParams.name)
            assertThat(Graph.count()).isEqualTo(1)
        }
    }
}
