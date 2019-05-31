package com.example.springkotlingraphexposed.app.models

import com.example.springkotlingraphexposed.app.tables.Graphs
import com.example.springkotlingraphexposed.app.tables.Nodes
import com.example.springkotlingraphexposed.app.views.graphs.GraphView
import com.example.springkotlingraphexposed.app.views.nodes.NodeView
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Graph(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Graph>(Graphs)

    var name by Graphs.name
    val nodes by Node referrersOn Nodes.graph

    fun nodeById(nodeId: Int): Node? {
        return this.nodes.find { it.id.value == nodeId }
    }

    fun uniqueEdges(): List<Edge> {
        return this.nodes.flatMap { it.fromEdges.union(it.toEdges) }.distinctBy { it.id }
    }
}

fun Graph.render() = GraphView(
        name = this.name,
        nodes = this.nodes.map { NodeView(it.name) }
)
