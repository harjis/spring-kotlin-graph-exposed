package com.example.springkotlingraphexposed.app.models

import com.example.springkotlingraphexposed.app.entities.Graphs
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Graph(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Graph>(Graphs)

    var name by Graphs.name
}
