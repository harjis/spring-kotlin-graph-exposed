package com.example.springkotlingraphexposed.app.models

import com.example.springkotlingraphexposed.app.tables.Edges
import com.example.springkotlingraphexposed.app.tables.Nodes
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Node (id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Node>(Nodes)

    var name by Nodes.name
    var graph by Graph referencedOn Nodes.graph
    val fromEdges by Edge referrersOn Edges.fromNode
    val toEdges by Edge referrersOn Edges.toNode
}
