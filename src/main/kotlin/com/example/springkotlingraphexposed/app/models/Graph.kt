package com.example.springkotlingraphexposed.app.models

import com.example.springkotlingraphexposed.app.tables.Graphs
import com.example.springkotlingraphexposed.app.tables.Nodes
import com.example.springkotlingraphexposed.app.views.graphs.GraphView
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import java.util.*

class Graph(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Graph>(Graphs)

    val nodes by Node referrersOn Nodes.graph

    var name by Graphs.name

    fun nodeById(nodeId: UUID): Node? {
        return this.nodes.find { it.id.value == nodeId }
    }

    fun edgeById(edgeId: UUID): Edge? {
        return uniqueEdges().find { it.id.value == edgeId }
    }

    fun uniqueEdges(): List<Edge> {
        return this.nodes.flatMap { it.fromEdges.union(it.toEdges) }.distinctBy { it.id }
    }

    fun rootNode(): Node {
        return this.nodes.find { it.type == "OutputNode" } ?: throw Exception("Noo!")
    }
}

fun Graph.render() = GraphView(
        id = this.id.value,
        name = this.name
)
