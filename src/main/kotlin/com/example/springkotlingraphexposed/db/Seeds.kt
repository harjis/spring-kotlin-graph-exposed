//package com.example.springkotlingraphexposed.db
//
//import com.example.springkotlingraphexposed.app.models.Edge
//import com.example.springkotlingraphexposed.app.models.Graph
//import com.example.springkotlingraphexposed.app.models.Node
//import org.jetbrains.exposed.sql.transactions.transaction
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.Profile
//
//@Configuration
//@Profile("dev")
//class Seeds {
//    @Bean
//    fun initDatabase() {
//        val graph1 = transaction {
//            Graph.new {
//                name = "Graph 1"
//            }
//        }
//
//        val node = transaction {
//            Node.new {
//                name = "Node 1"
//                graph = graph1
//            }
//        }
//
//        val node2 = transaction {
//            Node.new {
//                name = "Node 2"
//                graph = graph1
//            }
//        }
//
//        transaction {
//            Edge.new {
//                fromNode = node
//                toNode = node2
//            }
//        }
//    }
//}
