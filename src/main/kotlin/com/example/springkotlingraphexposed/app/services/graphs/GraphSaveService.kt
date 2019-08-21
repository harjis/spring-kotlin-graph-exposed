package com.example.springkotlingraphexposed.app.services.graphs

import com.example.springkotlingraphexposed.app.models.Edge
import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.models.Node
import com.example.springkotlingraphexposed.app.models.SomeOtherJson
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.util.*

@Service
class GraphSaveService {
    fun save(request: GraphRequest): Graph {
        return transaction {
            if (Graph.findById(request.graph.id) == null) {
                create(request)
            } else {
                update(request)
            }
        }
    }

    private fun create(request: GraphRequest): Graph {
        val savedGraph = Graph.new {
            name = request.graph.name
        }
        createNodes(savedGraph, request)
        createEdges(savedGraph, request)

        return savedGraph
    }

    private fun update(request: GraphRequest): Graph {
        val graph: Graph = Graph.findById(request.graph.id) ?: throw Exception("No graph found with id: ")
        graph.name = request.graph.name

        val currentNodeIds = graph.nodes.map { it.id.value }
        val (oldNodes, newNodes) = request.nodes.partition { it.id in currentNodeIds }
        removeUnused(graph, request)
        updateOldNodes(graph, oldNodes)
        addNewNodes(graph, newNodes)
        addNewEdges(graph, request)

        return graph
    }

    private fun createNodes(graph: Graph, request: GraphRequest) {
        request.nodes.forEach {
            Node.new(it.id) {
                content = SomeOtherJson()
                this.graph = graph
                name = it.name
                type = it.type
                x = it.x
                y = it.y
            }
        }
    }

    private fun createEdges(graph: Graph, request: GraphRequest) {
        request.edges.forEach {
            val fromNode = graph.nodeById(it.fromNodeId) ?: throw Exception("No graph found with id: ")
            val toNode = graph.nodeById(it.toNodeId) ?: throw Exception("No graph found with id: ")
            Edge.new(it.id) {
                this.fromNode = fromNode
                this.toNode = toNode
            }
        }
    }

    private fun removeUnused(graph: Graph, request: GraphRequest) {
        val persistedNodeIds = graph.nodes.map { it.id.value }
        val paramsNodeIds = request.nodes.mapNotNull { it.id }
        val toBeDeleted = persistedNodeIds.minus(paramsNodeIds)
        graph.nodes.forEach {
            if (toBeDeleted.contains(it.id.value)) it.delete()
        }

        graph.uniqueEdges().forEach {
            val inParams = request.edges.find { edgeParams -> edgeParams.id == it.id.value }
            if (inParams == null) {
                it.delete()
            }
        }
    }

    private fun updateOldNodes(graph: Graph, oldNodes: List<NodeParams>) {
        oldNodes.forEach {
            val node = graph.nodeById(it.id) ?: throw Exception("No graph found with id: ")
            node.apply {
                content = SomeOtherJson()
                name = it.name
                x = it.x
                y = it.y
            }

        }
    }

    private fun addNewNodes(graph: Graph, newNodes: List<NodeParams>) {
        newNodes.forEach {
            Node.new(it.id) {
                content = SomeOtherJson()
                this.graph = graph
                name = it.name
                type = it.type
                x = it.x
                y = it.y
            }
        }
    }

    private fun addNewEdges(graph: Graph, request: GraphRequest) {
        val currentEdges = graph.uniqueEdges().map { it.id.value }
        val newEdges = request.edges.filter { it.id !in currentEdges }
        val nodeIdToNode = graph.nodes.associateBy { it.id.value }
        newEdges.forEach { edgeParam ->
            val fromNode = nodeIdToNode[edgeParam.fromNodeId] ?: throw IllegalStateException("")
            val toNode = nodeIdToNode[edgeParam.toNodeId] ?: throw IllegalStateException("")
            Edge.new(edgeParam.id) {
                this.fromNode = fromNode
                this.toNode = toNode
            }
        }
    }
}

data class GraphRequest(
        val graph: GraphParams,
        val nodes: List<NodeParams> = listOf(),
        val edges: List<EdgeParams> = listOf()
)

data class GraphParams(val id: UUID, val name: String)
data class NodeParams(val content: Any? = null, val graphId: Any? = null, val id: UUID, val name: String, val toEdgeIds: Any? = null, val type: String, val x: Float, val y: Float)
data class EdgeParams(val id: UUID, val fromNodeId: UUID, val toNodeId: UUID)
