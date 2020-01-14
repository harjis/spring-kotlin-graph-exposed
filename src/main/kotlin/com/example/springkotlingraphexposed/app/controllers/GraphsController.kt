package com.example.springkotlingraphexposed.app.controllers

import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.models.render
import com.example.springkotlingraphexposed.app.services.graphs.GraphRequest
import com.example.springkotlingraphexposed.app.services.graphs.GraphSaveService
import com.example.springkotlingraphexposed.app.services.nodes.EagerLoader
import com.example.springkotlingraphexposed.app.services.nodes.NodeParams
import com.example.springkotlingraphexposed.app.views.graphs.GraphView
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(path = ["api/graphs"])
class GraphsController(private val graphSaveService: GraphSaveService) {
    @GetMapping("")
    fun index(): List<GraphView> {
        return Graph.all().toList().map { it.render() }
    }

    @GetMapping("/{id}")
    fun show(@PathVariable id: UUID): GraphView {
        return transaction {
            Graph.findById(id)?.render() ?: throw Exception("Not found")
        }
    }

    @PostMapping("/{id}/save_all")
    fun saveAll(@PathVariable id: UUID, @RequestBody request: GraphRequest): Boolean {
        graphSaveService.save(request)
        return true
    }

    @GetMapping("/{id}/tree")
    fun tree(@PathVariable id: UUID): NodeParams {
        return transaction {
            addLogger(StdOutSqlLogger)
            EagerLoader().eagerLoad(id)
        }
    }
}
