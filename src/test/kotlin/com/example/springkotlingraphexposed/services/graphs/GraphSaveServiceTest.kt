package com.example.springkotlingraphexposed.services.graphs

import com.example.springkotlingraphexposed.WithTestDatabase
import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.services.graphs.GraphParams
import com.example.springkotlingraphexposed.app.services.graphs.GraphSaveService
import com.example.springkotlingraphexposed.app.services.graphs.NodeParams
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
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

    @Test
    fun canSaveNodes() {
        withDb {
            val params = GraphParams(
                    name = "Graph 1",
                    nodes = listOf(
                            NodeParams(name = "Node 1")
                    )
            )
            val saved = graphSaveService.save(params)
            assertThat(saved.nodes.count()).isEqualTo(1)
        }
    }

    @Test
    fun canUpdateNodes() {
        withDb {
            val params = GraphParams(
                    name = "Graph 1",
                    nodes = listOf(
                            NodeParams(name = "Node 1")
                    )
            )
            val saved = graphSaveService.save(params)
            val updateParams = GraphParams(
                    id = saved.id.value,
                    name = saved.name,
                    nodes = listOf(
                            NodeParams(id = saved.nodes.first().id.value, name = "Updated Node 1")
                    )
            )
            val updated = graphSaveService.save(updateParams)
            assertThat(updated.nodes.count()).isEqualTo(1)
            assertThat(updated.nodes.first().name).isEqualTo("Updated Node 1")
        }
    }

    @Test
    fun canDeleteNodes() {
        withDb {
            val params = GraphParams(
                    name = "Graph 1",
                    nodes = listOf(
                            NodeParams(name = "Node 1"),
                            NodeParams(name = "Node 2")
                    )
            )
            val saved = graphSaveService.save(params)
            val updateParams = GraphParams(
                    id = saved.id.value,
                    name = saved.name,
                    nodes = listOf(
                            NodeParams(
                                    id = saved.nodes.first().id.value,
                                    name = saved.nodes.first().name
                            )
                    )
            )
            val updated = graphSaveService.save(updateParams)
            assertThat(updated.nodes.count()).isEqualTo(1)
            assertThat(updated.nodes.first().name).isEqualTo("Node 1")
        }
    }
}
