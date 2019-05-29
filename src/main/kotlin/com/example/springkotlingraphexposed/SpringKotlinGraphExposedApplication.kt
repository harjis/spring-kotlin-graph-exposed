package com.example.springkotlingraphexposed

import com.example.springkotlingraphexposed.app.entities.Graphs
import com.example.springkotlingraphexposed.app.entities.Nodes
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringKotlinGraphExposedApplication

fun main(args: Array<String>) {
    Database.connect(
            "jdbc:postgresql://localhost:5432/spring-kotlin-graph-exposed_development",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "docker"
    )
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Graphs, Nodes)
    }

    runApplication<SpringKotlinGraphExposedApplication>(*args)
}
