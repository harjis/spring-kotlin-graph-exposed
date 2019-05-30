package com.example.springkotlingraphexposed.app.tables

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object Graphs : IntIdTable() {
    val name: Column<String> = varchar("name", 50)
}
