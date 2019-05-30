package com.example.springkotlingraphexposed.app.tables

import org.jetbrains.exposed.dao.IntIdTable

object Edges : IntIdTable() {
    val fromNode = reference("from_node", Nodes)
    val toNode = reference("to_node", Nodes)
}
