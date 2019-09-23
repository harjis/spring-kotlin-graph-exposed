package com.example.springkotlingraphexposed.app.services.nodes

import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.models.Node
import java.util.*

class EagerLoader {
    fun eagerLoad(graphId: UUID): NodeParams {
        val graph = Graph.findById(graphId) ?: throw Exception("Not found")
        val rootNode = graph.rootNode()
        return eagerLoadNode(rootNode)
    }

    private fun eagerLoadNode(node: Node): NodeParams {
        return NodeParams(name = node.name, ancestors = node.ancestors().map { eagerLoadNode(it) })
    }
}

data class NodeParams(val name: String, val ancestors: List<NodeParams>)
