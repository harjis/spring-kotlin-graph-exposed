package com.example.springkotlingraphexposed.app.tables

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column

object Graphs : UUIDTable() {
    val name: Column<String> = varchar("name", 50)
}
