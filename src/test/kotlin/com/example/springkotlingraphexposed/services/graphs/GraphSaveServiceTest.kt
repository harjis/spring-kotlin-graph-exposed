package com.example.springkotlingraphexposed.services.graphs

import com.example.springkotlingraphexposed.WithTestDatabase
import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.services.graphs.EdgeParams
import com.example.springkotlingraphexposed.app.services.graphs.GraphParams
import com.example.springkotlingraphexposed.app.services.graphs.GraphSaveService
import com.example.springkotlingraphexposed.app.services.graphs.NodeParams
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

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
                            NodeParams(name = "Node 1", clientId = UUID.randomUUID())
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
                            NodeParams(name = "Node 1", clientId = UUID.randomUUID())
                    )
            )
            val saved = graphSaveService.save(params)
            val updateParams = GraphParams(
                    id = saved.id.value,
                    name = saved.name,
                    nodes = listOf(
                            NodeParams(
                                    id = saved.nodes.first().id.value,
                                    name = "Updated Node 1",
                                    clientId = UUID.randomUUID()
                            )
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
                            NodeParams(name = "Node 1", clientId = UUID.randomUUID()),
                            NodeParams(name = "Node 2", clientId = UUID.randomUUID())
                    )
            )
            val saved = graphSaveService.save(params)
            val updateParams = GraphParams(
                    id = saved.id.value,
                    name = saved.name,
                    nodes = listOf(
                            NodeParams(
                                    id = saved.nodes.first().id.value,
                                    name = saved.nodes.first().name,
                                    clientId = UUID.randomUUID()
                            )
                    )
            )
            val updated = graphSaveService.save(updateParams)
            assertThat(updated.nodes.count()).isEqualTo(1)
            assertThat(updated.nodes.first().name).isEqualTo("Node 1")
        }
    }

    @Test
    fun canAddAndUpdateNodes() {
        withDb {
            val params = GraphParams(
                    name = "Graph1",
                    nodes = listOf(
                            NodeParams(
                                    name = "Node 1", clientId = UUID.randomUUID()
                            ),
                            NodeParams(name = "Node 2", clientId = UUID.randomUUID())
                    )
            )
            val saved = graphSaveService.save(params)
            val updatedParams = GraphParams(
                    id = saved.id.value,
                    name = "Updated " + saved.name,
                    nodes = saved.nodes.mapIndexed { index, node ->
                        NodeParams(
                                id = node.id.value,
                                name = "Updated " + node.name,
                                clientId = params.nodes[index].clientId
                        )
                    }.plus(NodeParams(name = "New Node 3", clientId = UUID.randomUUID()))
            )
            val updated = graphSaveService.save(updatedParams)
            assertThat(updated.name).isEqualTo("Updated Graph1")
            assertThat(updated.nodes.count()).isEqualTo(3)
            assertThat(updated.nodesByInsertOrder().first().name).isEqualTo("Updated Node 1")
            assertThat(updated.nodesByInsertOrder().elementAt(1).name).isEqualTo("Updated Node 2")
            assertThat(updated.nodesByInsertOrder().last().name).isEqualTo("New Node 3")
        }
    }

    @Test
    fun canCreateEdges() {
        withDb {
            val nodeId = UUID.randomUUID()
            val nodeId2 = UUID.randomUUID()
            val params = GraphParams(
                    name = "Graph 1",
                    nodes = listOf(
                            NodeParams(name = "Node 1", clientId = nodeId),
                            NodeParams(name = "Node 2", clientId = nodeId2)
                    ),
                    edges = listOf(
                            EdgeParams(fromNode = nodeId, toNode = nodeId2)
                    )
            )
            val saved = graphSaveService.save(params)
            assertThat(saved.uniqueEdges().count()).isEqualTo(1)
        }
    }

    @Test
    fun canNotCreateDuplicateEdges() {
        assertThrows<ExposedSQLException> {
            withDb {
                val nodeId = UUID.randomUUID()
                val nodeId2 = UUID.randomUUID()
                val params = GraphParams(
                        name = "Graph 1",
                        nodes = listOf(
                                NodeParams(name = "Node 1", clientId = nodeId),
                                NodeParams(name = "Node 2", clientId = nodeId2)
                        ),
                        edges = listOf(
                                EdgeParams(fromNode = nodeId, toNode = nodeId2)
                        )
                )
                val saved = graphSaveService.save(params)
                val updateParams = GraphParams(
                        id = saved.id.value,
                        name = "Graph 1",
                        nodes = listOf(
                                NodeParams(
                                        id = saved.nodes.first().id.value,
                                        name = "Node 1",
                                        clientId = nodeId
                                ),
                                NodeParams(
                                        id = saved.nodes.last().id.value,
                                        name = "Node 2",
                                        clientId = nodeId2
                                )
                        ),
                        edges = listOf(
                                EdgeParams(
                                        id = saved.uniqueEdges().first().id.value,
                                        fromNode = saved.nodes.first().id.value,
                                        toNode = saved.nodes.last().id.value
                                ),
                                EdgeParams(
                                        fromNode = saved.nodes.first().id.value,
                                        toNode = saved.nodes.last().id.value
                                )
                        )
                )
                graphSaveService.save(updateParams)
            }
        }
    }

    @Test
    fun canDeleteEdge() {
        withDb {
            val nodeId = UUID.randomUUID()
            val nodeId2 = UUID.randomUUID()
            val params = GraphParams(
                    name = "Graph 1",
                    nodes = listOf(
                            NodeParams(name = "Node 1", clientId = nodeId),
                            NodeParams(name = "Node 2", clientId = nodeId2)
                    ),
                    edges = listOf(
                            EdgeParams(fromNode = nodeId, toNode = nodeId2)
                    )
            )
            val saved = graphSaveService.save(params)
            val updateParams = GraphParams(
                    id = saved.id.value,
                    name = "Graph 1",
                    nodes = listOf(
                            NodeParams(
                                    id = saved.nodes.first().id.value,
                                    name = "Node 1",
                                    clientId = nodeId
                            ),
                            NodeParams(
                                    id = saved.nodes.last().id.value,
                                    name = "Node 2",
                                    clientId = nodeId2
                            )
                    )
            )
            val updated = graphSaveService.save(updateParams)
            assertThat(updated.uniqueEdges().count()).isEqualTo(0)
        }
    }
}
