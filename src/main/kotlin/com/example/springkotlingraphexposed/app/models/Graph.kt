package com.example.springkotlingraphexposed.app.models

import com.example.springkotlingraphexposed.app.entities.Graphs
import com.example.springkotlingraphexposed.app.entities.Nodes
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Graph(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Graph>(Graphs)

    var name by Graphs.name
    val nodes by Node referrersOn Nodes.graph
}

data class GraphResponse(val name: String)

fun Graph.render() = GraphResponse(
        name = this.name
)
