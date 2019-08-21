package com.example.springkotlingraphexposed.app.controllers

import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.models.Node
import com.example.springkotlingraphexposed.app.models.SomeOtherJson
import com.example.springkotlingraphexposed.app.models.render
import com.example.springkotlingraphexposed.app.views.nodes.NodeView
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/graphs/{graphId}/nodes"])
class NodesController {
    @GetMapping("")
    fun index(@PathVariable graphId: Int): List<NodeView> {
        return transaction {
            val graph = graph(graphId)
            graph.nodes.toList().map { it.render() }
        }
    }

    @PostMapping("")
    fun create(@PathVariable graphId: Int,
               @RequestBody nodeCreateRequest: NodeCreateRequest
    ): NodeView {
        return transaction {
            val graph = graph(graphId)
            Node.new {
                this.content = SomeOtherJson()
                this.graph = graph
                this.name = nodeCreateRequest.name
                this.type = nodeCreateRequest.type
            }.render()
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable graphId: Int,
               @PathVariable id: Int,
               @RequestBody nodeUpdateRequest: NodeUpdateRequest): NodeView {
        return transaction {
            val graph = graph(graphId)
            val node = graph.nodeById(id) ?: throw Exception("Not found")
            node.name = nodeUpdateRequest.name
            node.x = nodeUpdateRequest.x
            node.y = nodeUpdateRequest.y
            node.render()
        }
    }

    private fun graph(graphId: Int): Graph {
        return Graph.findById(graphId) ?: throw Exception("Not found")
    }
}

data class NodeCreateRequest(
        val name: String,
        val type: String
)

data class NodeUpdateRequest(
        val content: Any,
        val name: String,
        val toEdgeIds: List<Int>,
        val type: String,
        val x: Float,
        val y: Float
)
