package com.example.springkotlingraphexposed.app.views.edges

data class EdgeView(
        val fromNodeId: Int,
        val id: Int,
        val name: String? = null,
        val toNodeId: Int
)
