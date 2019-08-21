package com.example.springkotlingraphexposed.app.tables

import org.jetbrains.exposed.dao.UUIDTable

object Edges : UUIDTable() {
    val fromNode = reference("from_node", Nodes)
    val toNode = reference("to_node", Nodes)
    init {
        index(true, fromNode, toNode)
    }
}
