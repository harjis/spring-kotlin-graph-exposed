package com.example.springkotlingraphexposed.app.views.nodes

import com.example.springkotlingraphexposed.app.models.ContentJson
import java.util.*

data class NodeView(
        val content: ContentJson,
        val graphId: UUID,
        val id: UUID,
        val name: String,
        val toEdgeIds: List<UUID>,
        val type: String,
        val x: Float,
        val y: Float
)
