package com.example.springkotlingraphexposed.app.services.graphs

import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.models.Node
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.lang.Exception

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

        return savedGraph
    }

    private fun update(params: GraphParams): Graph {
        if (params.id == null) throw Exception("No update without id")
        var graph = Graph.findById(params.id)
        if (graph == null) throw Exception("No graph found with id: ")
        graph.name = params.name
        upsertNodes(params.nodes, graph)
        return graph
    }

    private fun upsertNodes(nodes: List<NodeParams>, savedGraph: Graph) {
        nodes.forEach {
            if(it.id == null) {
                Node.new {
                    name = it.name
                    graph = savedGraph
                }
            } else {
                val node = savedGraph.nodes.find { node -> node.id.value == it.id }
                if (node is Node) {
                    node.name = it.name
                }
            }
        }
    }
}

data class GraphParams(val id: Int? = null, val name: String, val nodes: List<NodeParams> = listOf())
data class NodeParams(val id: Int? = null, val name: String)
