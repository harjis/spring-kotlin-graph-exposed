package com.example.springkotlingraphexposed.app.services.graphs

import com.example.springkotlingraphexposed.app.models.Edge
import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.models.Node
import com.example.springkotlingraphexposed.app.models.SomeOtherJson
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.lang.Exception
import java.util.*

@Service
class GraphSaveService {
    fun save(params: GraphParams): Graph {
        return transaction {
            if (params.id == null) {
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
        upsertNodes(params.nodes, savedGraph)
        // nodeMap needs to be calculated exactly at this position
        val nodeMap = getNodeMap(params.nodes, savedGraph)
        saveEdges(params.edges, savedGraph, nodeMap)

        return savedGraph
    }

    private fun update(params: GraphParams): Graph {
        if (params.id == null) throw Exception("No update without id")
        val graph: Graph = Graph.findById(params.id) ?: throw Exception("No graph found with id: ")

        graph.name = params.name
        upsertNodes(params.nodes, graph)
        // nodeMap needs to be calculated exactly at this position
        val nodeMap = getNodeMap(params.nodes, graph)
        deleteNodes(params.nodes, graph)
        deleteEdges(params.edges, graph)
        saveEdges(params.edges, graph, nodeMap)


        return graph
    }

    private fun upsertNodes(nodes: List<NodeParams>, savedGraph: Graph) {
        nodes.forEach {
            val node = if (it.id == null) {
                Node.new {
                    name = it.name
                    graph = savedGraph
                    content = SomeOtherJson()
                }
            } else {
                val node = savedGraph.nodes.find { node -> node.id.value == it.id }
                if (node is Node) {
                    node.name = it.name
                }
                node
            }
            // NodeParams need to be updated so that primary key's are in sync.
            it.id = node!!.id.value
        }
    }

    private fun deleteNodes(nodes: List<NodeParams>, savedGraph: Graph) {
        val persistedNodeIds: List<Int> = savedGraph.nodes.map { it.id.value }
        val paramsNodeIds: List<Int> = nodes.mapNotNull { it.id }
        val toBeDeleted: List<Int> = persistedNodeIds.minus(paramsNodeIds)
        savedGraph.nodes.forEach {
            if (toBeDeleted.contains(it.id.value)) it.delete()
        }
    }

    private fun saveEdges(edges: List<EdgeParams>, graph: Graph, nodeMap: Map<UUID, Node>) {
        edges.filter { it.id == null }.forEach {
            val fromAndToNode = if (it.fromNode is Int && it.toNode is Int) {
                val fromNode = graph.nodeById(it.fromNode)
                val toNode = graph.nodeById(it.toNode)
                Pair(fromNode, toNode)
            } else if (it.fromNode is UUID && it.toNode is UUID) {
                val fromNode = nodeMap[it.fromNode]
                val toNode = nodeMap[it.toNode]
                Pair(fromNode, toNode)
            } else if (it.fromNode is UUID && it.toNode is Int) {
                val fromNode = nodeMap[it.fromNode]
                val toNode = graph.nodeById(it.toNode)
                Pair(fromNode, toNode)
            } else if (it.fromNode is Int && it.toNode is UUID) {
                val fromNode = graph.nodeById(it.fromNode)
                val toNode = nodeMap[it.toNode]
                Pair(fromNode, toNode)
            } else {
                throw Exception("Nooo")
            }

            val (_fromNode, _toNode) = fromAndToNode
            if (_fromNode is Node && _toNode is Node) {
                Edge.new {
                    fromNode = _fromNode
                    toNode = _toNode
                }
            }
        }
    }

    private fun deleteEdges(edges: List<EdgeParams>, graph: Graph) {
        graph.uniqueEdges().forEach {
            println(it.id.toString() + " " + it.fromNode.id + " " + it.toNode.id)
            val inParams = edges.find { edgeParams -> edgeParams.id == it.id.value }
            if (inParams == null) {
                it.delete()
            }
        }
    }

    private fun getNodeMap(nodes: List<NodeParams>, graph: Graph): Map<UUID, Node> {
        return nodes.mapIndexed { index, nodeParams ->
            nodeParams.clientId to graph.nodes.toList()[index]
        }.toMap()
    }
}

data class GraphParams(
        val id: Int? = null,
        val name: String,
        val nodes: List<NodeParams> = listOf(),
        val edges: List<EdgeParams> = listOf()
)

data class NodeParams(var id: Int? = null, val name: String, val clientId: UUID)
data class EdgeParams(val id: Int? = null, val fromNode: Any?, val toNode: Any?)
