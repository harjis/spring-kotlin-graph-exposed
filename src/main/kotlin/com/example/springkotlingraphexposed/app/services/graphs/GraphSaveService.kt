package com.example.springkotlingraphexposed.app.services.graphs

import com.example.springkotlingraphexposed.app.models.Graph
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class GraphSaveService {
    fun save(params: GraphParams): Graph {
        return transaction {
            if (params.id == null) {
                create(params)
            } else {
                update(params)
            }
        }
    }

    private fun create(params: GraphParams): Graph {
        return Graph.new {
            name = params.name
        }
    }

    private fun update(params: GraphParams): Graph {
        if (params.id == null) throw Exception("No update without id")
        var graph = Graph.findById(params.id)
        if (graph == null) throw Exception("No graph found with id: ")
        graph.name = params.name
        return graph
    }
}

data class GraphParams(val id: Int? = null, val name: String)
