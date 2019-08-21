package com.example.springkotlingraphexposed.app.controllers

import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.views.graphs.GraphView
import com.example.springkotlingraphexposed.app.models.render
import com.example.springkotlingraphexposed.app.services.graphs.GraphRequest
import com.example.springkotlingraphexposed.app.services.graphs.GraphSaveService
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception
import java.util.*

@RestController
@RequestMapping(path = ["/graphs"])
class GraphsController(private val graphSaveService: GraphSaveService) {
    @GetMapping("")
    fun index(): List<GraphView> {
        return transaction {
            Graph.all().toList().map { it.render() }
        }
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
    fun tree(@PathVariable id: UUID) {
        transaction {
            val graph = Graph.findById(id) ?: throw Exception("Not found")
            val root = graph.rootNode()
            println(root.name)
            val ancestors = root.ancestors()
            ancestors.forEach {
                println(it.name)
            }
        }
    }
}
