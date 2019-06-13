package com.example.springkotlingraphexposed.app.tables

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object Nodes : IntIdTable(){
    val name: Column<String> = varchar("name", 50)
    val content: Column<String> = text("content")
    val graph = reference("graph", Graphs)
}
