package com.example.springkotlingraphexposed.app.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column

object Graphs : UUIDTable() {
    val name: Column<String> = varchar("name", 50)
}
