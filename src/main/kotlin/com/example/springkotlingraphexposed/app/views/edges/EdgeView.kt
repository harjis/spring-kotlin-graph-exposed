package com.example.springkotlingraphexposed.app.views.edges

data class EdgeView(
        val from_node_id: Int,
        val id: Int,
        val name: String? = null,
        val to_node_id: Int
)
