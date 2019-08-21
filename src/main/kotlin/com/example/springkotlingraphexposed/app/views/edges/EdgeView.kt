package com.example.springkotlingraphexposed.app.views.edges

import java.util.*

data class EdgeView(
        val fromNodeId: UUID,
        val id: UUID,
        val toNodeId: UUID
)
