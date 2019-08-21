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
    fun save(params: GraphParams): Graph {
        return transaction {
            if (Graph.findById(params.id) == null) {
                create(params)
            } else {
                update(params)
            }
        }
    }

    private fun create(params: GraphParams): Graph {
        val savedGraph = Graph.new {
            name = params.name
        }
        createNodes(savedGraph, params)
        createEdges(savedGraph, params)

        return savedGraph
    }

    private fun update(params: GraphParams): Graph {
        val graph: Graph = Graph.findById(params.id) ?: throw Exception("No graph found with id: ")
        graph.name = params.name

        val currentNodeIds = graph.nodes.map { it.id.value }
        val (oldNodes, newNodes) = params.nodes.partition { it.id in currentNodeIds }
        removeUnused(graph, params)
        updateOldNodes(graph, oldNodes)
        addNewNodes(graph, newNodes)
        addNewEdges(graph, params)

        return graph
    }

    private fun createNodes(graph: Graph, params: GraphParams) {
        params.nodes.forEach {
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

    private fun createEdges(graph: Graph, params: GraphParams) {
        params.edges.forEach {
            val fromNode = graph.nodeById(it.fromNodeId) ?: throw Exception("No graph found with id: ")
            val toNode = graph.nodeById(it.toNodeId) ?: throw Exception("No graph found with id: ")
            Edge.new(it.id) {
                this.fromNode = fromNode
                this.toNode = toNode
            }
        }
    }

    private fun removeUnused(graph: Graph, params: GraphParams) {
        val persistedNodeIds = graph.nodes.map { it.id.value }
        val paramsNodeIds = params.nodes.mapNotNull { it.id }
        val toBeDeleted = persistedNodeIds.minus(paramsNodeIds)
        graph.nodes.forEach {
            if (toBeDeleted.contains(it.id.value)) it.delete()
        }

        graph.uniqueEdges().forEach {
            val inParams = params.edges.find { edgeParams -> edgeParams.id == it.id.value }
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

    private fun addNewEdges(graph: Graph, params: GraphParams) {
        val currentEdges = graph.uniqueEdges().map { it.id.value }
        val newEdges = params.edges.filter { it.id !in currentEdges }
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

data class GraphParams(
        val id: UUID,
        val name: String,
        val nodes: List<NodeParams> = listOf(),
        val edges: List<EdgeParams> = listOf()
)

data class NodeParams(var id: UUID, val name: String, val type: String, val x: Float, val y: Float)
data class EdgeParams(val id: UUID, val fromNodeId: UUID, val toNodeId: UUID)
