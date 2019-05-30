package com.example.springkotlingraphexposed.app.models

import com.example.springkotlingraphexposed.app.tables.Graphs
import com.example.springkotlingraphexposed.app.tables.Nodes
import com.example.springkotlingraphexposed.app.views.graphs.GraphResponse
import com.example.springkotlingraphexposed.app.views.nodes.NodeResponse
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Graph(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Graph>(Graphs)

    var name by Graphs.name
    val nodes by Node referrersOn Nodes.graph
}

fun Graph.render() = GraphResponse(
        name = this.name,
        nodes = this.nodes.map { NodeResponse(it.name) }
)
