package com.example.springkotlingraphexposed

import com.example.springkotlingraphexposed.app.tables.Edges
import com.example.springkotlingraphexposed.app.tables.Graphs
import com.example.springkotlingraphexposed.app.tables.Nodes
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

// TODO This probably is really inefficient but it will do for now
abstract class WithTestDatabase {
    fun withDb(statement: Transaction.() -> Unit) {
        Database.connect(
                "jdbc:postgresql://localhost:5432/spring-kotlin-graph-exposed_test",
                driver = "org.postgresql.Driver",
                user = "postgres",
                password = "docker"
        )
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Graphs, Nodes, Edges)
            statement()
        }

        transaction {
            SchemaUtils.drop(Graphs, Nodes, Edges)
        }
    }
}
