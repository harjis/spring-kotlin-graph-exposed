package com.example.springkotlingraphexposed.app.entities

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object Graphs : IntIdTable() {
    val name: Column<String> = varchar("name", 50)
}
