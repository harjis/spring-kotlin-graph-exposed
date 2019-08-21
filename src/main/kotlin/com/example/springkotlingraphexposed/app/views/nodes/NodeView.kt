package com.example.springkotlingraphexposed.app.views.nodes

import com.example.springkotlingraphexposed.app.models.ContentJson

data class NodeView(
        val content: ContentJson,
        val graphId: Int,
        val id: Int,
        val name: String,
        val toEdgeIds: List<Int>,
        val type: String,
        val x: Float,
        val y: Float
)
