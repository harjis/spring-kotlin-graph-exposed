package com.example.springkotlingraphexposed

import com.example.springkotlingraphexposed.app.tables.Edges
import com.example.springkotlingraphexposed.app.tables.Graphs
import com.example.springkotlingraphexposed.app.tables.Nodes
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

// TODO This probably is really inefficient but it will do for now
abstract class WithTestDatabase {
    fun withDb(statement: Transaction.() -> Unit) {
        val db = Database.connect(
                "jdbc:postgresql://localhost:5432/spring-kotlin-graph-exposed_test",
                driver = "org.postgresql.Driver",
                user = "postgres",
                password = "docker"
        )
        val connection = db.connector()
        val transactionInIsolation = connection.metaData.defaultTransactionIsolation
        transaction(transactionInIsolation, 1, db = db) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Graphs, Nodes, Edges)
            statement()
        }

        transaction {
            SchemaUtils.drop(Graphs, Nodes, Edges)
        }
    }
}
