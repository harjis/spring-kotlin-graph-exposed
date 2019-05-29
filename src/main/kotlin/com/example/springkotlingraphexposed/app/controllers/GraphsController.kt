package com.example.springkotlingraphexposed.app.controllers

import com.example.springkotlingraphexposed.app.entities.Graphs
import com.example.springkotlingraphexposed.app.models.Graph
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/graphs"])
class GraphsController {
    @GetMapping("")
    fun index(): List<Graph> {
        val graphs = transaction {
            Graphs.selectAll().map { Graph(it[Graphs.id]) }
        }
        println(graphs)
        return graphs
    }
}
