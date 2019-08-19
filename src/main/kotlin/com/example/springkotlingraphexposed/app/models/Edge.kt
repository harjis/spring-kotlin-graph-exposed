package com.example.springkotlingraphexposed.app.models

import com.example.springkotlingraphexposed.app.tables.Edges
import com.example.springkotlingraphexposed.app.views.edges.EdgeView
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Edge(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Edge>(Edges)

    var fromNode by Node referencedOn Edges.fromNode
    var toNode by Node referencedOn Edges.toNode
}

fun Edge.render() = EdgeView(
        id = this.id.value,
        from_node_id = this.fromNode.id.value,
        to_node_id = this.toNode.id.value
)
