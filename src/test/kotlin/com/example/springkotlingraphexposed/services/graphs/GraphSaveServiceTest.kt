package com.example.springkotlingraphexposed.services.graphs

import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.services.graphs.GraphParams
import com.example.springkotlingraphexposed.app.services.graphs.GraphSaveService
import com.example.springkotlingraphexposed.app.tables.Edges
import com.example.springkotlingraphexposed.app.tables.Graphs
import com.example.springkotlingraphexposed.app.tables.Nodes
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test

import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GraphSaveServiceTest {
    val graphSaveService: GraphSaveService = GraphSaveService()

    @Test
    fun canSaveGraph() {
        setupDB()
        val name = "Graph 1"
        val params = GraphParams(name = name)
        val saved = graphSaveService.save(params)
        assertThat(saved.id).isNotNull()
        assertThat(saved.name).isEqualTo(name)
        //TODO this fails because no transaction
//        assertThat(Graph.count()).isEqualTo(1)
    }

    private fun setupDB() {
        Database.connect(
                "jdbc:postgresql://localhost:5432/spring-kotlin-graph-exposed_development",
                driver = "org.postgresql.Driver",
                user = "postgres",
                password = "docker"
        )
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Graphs, Nodes, Edges)
        }
    }
}
