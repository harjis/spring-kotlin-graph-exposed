package com.example.springkotlingraphexposed.app.controllers

import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.views.graphs.GraphResponse
import com.example.springkotlingraphexposed.app.models.render
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception

@RestController
@RequestMapping(path = ["/graphs"])
class GraphsController {
    @GetMapping("")
    fun index(): List<GraphResponse> {
        return transaction {
            Graph.all().toList().map { it.render() }
        }
    }

    @GetMapping("/{id}")
    fun show(@PathVariable id: Int): GraphResponse {
        return transaction {
            Graph.findById(id)?.render() ?: throw Exception("Not found")
        }
    }
}
