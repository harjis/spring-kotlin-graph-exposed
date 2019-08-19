package com.example.springkotlingraphexposed.app.models

import com.example.springkotlingraphexposed.app.tables.Graphs
import com.example.springkotlingraphexposed.app.tables.Nodes
import com.example.springkotlingraphexposed.app.views.graphs.GraphView
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder

class Graph(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Graph>(Graphs)

    var name by Graphs.name
    val nodes by Node referrersOn Nodes.graph

    fun nodesByInsertOrder(): SizedIterable<Node> {
        return this.nodes.orderBy(Nodes.id to SortOrder.ASC)
    }

    fun nodeById(nodeId: Int): Node? {
        return this.nodes.find { it.id.value == nodeId }
    }

    fun edgeById(edgeId: Int): Edge? {
        return uniqueEdges().find { it.id.value == edgeId }
    }

    fun uniqueEdges(): List<Edge> {
        return this.nodes.flatMap { it.fromEdges.union(it.toEdges) }.distinctBy { it.id }
    }
}

fun Graph.render() = GraphView(
        id = this.id.value,
        name = this.name
)
