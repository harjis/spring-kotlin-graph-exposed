package com.example.springkotlingraphexposed.app.tables

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object Nodes : IntIdTable() {
    val type: Column<String> = varchar("type", 50)
    val name: Column<String> = varchar("name", 50)
    val content: Column<String> = text("content")
    val graph = reference("graph", Graphs)
    val x: Column<Float> = float("x").default(0F)
    val y: Column<Float> = float("y").default(0F)
}
