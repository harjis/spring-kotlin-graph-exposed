package com.example.springkotlingraphexposed.models

import com.example.springkotlingraphexposed.WithTestDatabase
import com.example.springkotlingraphexposed.app.models.*
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.lang.Exception

@SpringBootTest
@ActiveProfiles("test")
class NodeTest : WithTestDatabase() {
    @Test
    fun canSaveNodeWithJsonContent() {
        withDb {
            val content1 = AggregatesJson(groupBy = "product_id", aggregates = listOf(
                    Aggregate("price", "min"),
                    Aggregate("amount", "max")
            ))

            val graph1 = transaction {
                Graph.new { name = "Graph 1" }
            }
            val node = transaction {
                Node.new {
                    type = InputType
                    graph = graph1
                    name = "Node 1"
                    content = content1
                }
            }
            assertThat(node.content).isInstanceOf(AggregatesJson::class.java)
        }
    }

    @Test
    fun canNotSaveInvalidJson() {
        assertThrows<Exception> {
            withDb {
                val content1 = InvalidJson()

                val graph1 = transaction {
                    Graph.new { name = "Graph 1" }
                }
                val node = transaction {
                    Node.new {
                        type = InputType
                        graph = graph1
                        name = "Node 1"
                        content = content1
                    }
                }
            }
        }
    }
}

data class InvalidJson(override val type: String = "invalid_json") : ContentJson
