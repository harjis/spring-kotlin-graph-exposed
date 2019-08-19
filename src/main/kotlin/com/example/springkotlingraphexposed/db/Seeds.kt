package com.example.springkotlingraphexposed.db

import com.example.springkotlingraphexposed.app.models.Edge
import com.example.springkotlingraphexposed.app.models.Graph
import com.example.springkotlingraphexposed.app.models.Node
import com.example.springkotlingraphexposed.app.models.SomeOtherJson
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("dev")
class Seeds {
    @Bean
    fun initDatabase() {
        val graphCount = transaction {
            Graph.count()
        }
        if (graphCount > 0) {
            return
        }

        val graph = transaction {
            Graph.new {
                name = "Graph 1"
            }
        }

        val node = transaction {
            Node.new {
                name = "Node 1"
                this.graph = graph
                content = SomeOtherJson()
                x = 50F
                y = 200F
            }
        }

        val node2 = transaction {
            Node.new {
                name = "Node 2"
                this.graph = graph
                content = SomeOtherJson()
                x = 300F
                y = 400F
            }
        }

        transaction {
            Edge.new {
                fromNode = node
                toNode = node2
            }
        }

        val graph2 = transaction {
            Graph.new {
                name = "Graph 2"
            }
        }

        val node21 = transaction {
            Node.new {
                name = "Node 1"
                this.graph = graph2
                content = SomeOtherJson()
                x = 10F
                y = 10F
            }
        }

        val node22 = transaction {
            Node.new {
                name = "Node 2"
                this.graph = graph2
                content = SomeOtherJson()
                x = 400F
                y = 200F
            }
        }

        transaction {
            Edge.new {
                fromNode = node21
                toNode = node22
            }
        }
    }
}
