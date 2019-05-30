package com.example.springkotlingraphexposed.app.views.graphs

import com.example.springkotlingraphexposed.app.views.nodes.NodeResponse

data class GraphResponse(val name: String, val nodes: List<NodeResponse>)
