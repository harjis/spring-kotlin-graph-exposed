package com.example.springkotlingraphexposed.app.controllers

import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.models.GraphResponse
import com.example.springkotlingraphexposed.app.models.render
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/graphs"])
class GraphsController {
    @GetMapping("")
    fun index(): List<GraphResponse> {
        return transaction {
            Graph.all().toList().map { it.render() }
        }
    }
}
