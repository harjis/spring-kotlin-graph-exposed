package com.example.springkotlingraphexposed.db

import com.example.springkotlingraphexposed.app.models.Graph
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("dev")
class Seeds {
    @Bean
    fun initDatabase() {
        transaction {
            val graph = Graph.new {
                name = "Graph 1"
            }
        }
    }
}
