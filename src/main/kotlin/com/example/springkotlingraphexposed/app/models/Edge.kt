package com.example.springkotlingraphexposed.app.models

import com.example.springkotlingraphexposed.app.tables.Edges
import com.example.springkotlingraphexposed.app.views.edges.EdgeView
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class Edge(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Edge>(Edges)

    var fromNode by Node referencedOn Edges.fromNode
    var toNode by Node referencedOn Edges.toNode
}

fun Edge.render() = EdgeView(
        id = this.id.value,
        fromNodeId = this.fromNode.id.value,
        toNodeId = this.toNode.id.value
)
