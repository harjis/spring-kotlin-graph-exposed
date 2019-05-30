package com.example.springkotlingraphexposed.app.models

import com.example.springkotlingraphexposed.app.tables.Edges
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Edge(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Edge>(Edges)

    var fromNode by Node referencedOn Edges.fromNode
    var toNode by Node referencedOn Edges.toNode
}
