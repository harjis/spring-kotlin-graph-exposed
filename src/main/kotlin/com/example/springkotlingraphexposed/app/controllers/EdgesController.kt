package com.example.springkotlingraphexposed.app.controllers

import com.example.springkotlingraphexposed.app.models.Edge
import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.models.Node
import com.example.springkotlingraphexposed.app.models.render
import com.example.springkotlingraphexposed.app.views.edges.EdgeView
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(path = ["api/graphs/{graphId}/edges"])
class EdgesController {
    @GetMapping("")
    fun index(@PathVariable graphId: UUID): List<EdgeView> {
        return transaction {
            val graph = graph(graphId)
            graph.uniqueEdges().map { it.render() }
        }
    }

    @PostMapping("")
    fun create(@RequestBody edgeCreateRequest: EdgeCreateRequest): EdgeView {
        return transaction {
            val fromNode = Node.findById(edgeCreateRequest.fromNodeId) ?: throw Exception("Not found")
            val toNode = Node.findById(edgeCreateRequest.toNodeId) ?: throw Exception("Not found")
            Edge.new {
                this.fromNode = fromNode
                this.toNode = toNode
            }.render()
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable graphId: UUID, @PathVariable id: UUID) {
        return transaction {
            val edge = graph(graphId).edgeById(id)
            edge?.delete()
        }
    }

    private fun graph(graphId: UUID): Graph {
        return Graph.findById(graphId) ?: throw Exception("Not found")
    }
}

data class EdgeCreateRequest(val fromNodeId: UUID, val toNodeId: UUID)
