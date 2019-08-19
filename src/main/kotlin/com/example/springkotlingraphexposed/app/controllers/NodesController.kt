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
               @RequestParam name: String,
               @RequestParam type: String
    ): NodeView {
        return transaction {
            val graph = graph(graphId)
            Node.new {
                this.name = name
                this.graph = graph
                this.content = SomeOtherJson()
            }.render()
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable graphId: Int,
               @PathVariable id: Int,
               @RequestParam(required = false) name: String,
               @RequestParam(required = false) type: String): Boolean {
        return transaction {
            val graph = graph(graphId)
            val node = graph.nodeById(id)
            if (node != null) {
                if (name != null){
                    node.name = name
                }
            }
            true
        }
    }

    private fun graph(graphId: Int): Graph {
        return Graph.findById(graphId) ?: throw Exception("Not found")
    }
}
