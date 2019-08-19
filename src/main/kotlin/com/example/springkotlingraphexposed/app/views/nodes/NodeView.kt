package com.example.springkotlingraphexposed.app.views.nodes

import com.example.springkotlingraphexposed.app.models.ContentJson

data class NodeView(
        val content: ContentJson,
        val graph_id: Int,
        val id: Int,
        val name: String,
        val to_edge_ids: List<Int>,
        val type: String = "InputNode",
        val x: Float = 0F,
        val y: Float = 0F
)
