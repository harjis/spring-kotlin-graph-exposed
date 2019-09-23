package com.example.springkotlingraphexposed.app.models

import com.example.springkotlingraphexposed.app.tables.Edges
import com.example.springkotlingraphexposed.app.tables.Nodes
import com.example.springkotlingraphexposed.app.views.nodes.NodeView
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import java.util.*

class Node(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Node>(Nodes)

    var nodeRef by Node optionalReferencedOn Nodes.nodeRef
    var graph by Graph referencedOn Nodes.graph
    val fromEdges by Edge referrersOn Edges.fromNode
    val toEdges by Edge referrersOn Edges.toNode

    var type by Nodes.type
    var name by Nodes.name
    var x by Nodes.x
    var y by Nodes.y
    private var _content by Nodes.content
    var content: ContentJson
        get() {
            val contentJson: JsonNode = jacksonObjectMapper().readValue(_content)
            val type = contentJson.get("type").asText()
            return when (type) {
                "aggregate" -> jacksonObjectMapper().readValue(_content, getContentColumnMap().get(type))
                "some_other" -> jacksonObjectMapper().readValue(_content, getContentColumnMap().get(type))
                else -> throw Exception("Content json is invalid")
            }
        }
        set(value) {
            _content = if (isValidContent(value.type)) {
                jacksonObjectMapper().writeValueAsString(value)
            } else {
                throw Exception("Content json is invalid")
            }
        }

    fun toEdgeIds(): List<UUID> {
        return toEdges.map { it.id.value }
    }

    fun ancestors(): List<Node> {
        return this.toEdges.map {
            if (it.fromNode.type == "NodeRefNode") {
                it.fromNode.nodeRef ?: throw Exception("Wat")
            } else {
                it.fromNode
            }
        }
    }
}

fun Node.render() = NodeView(
        content = this.content,
        graphId = this.graph.id.value,
        id = this.id.value,
        name = this.name,
        toEdgeIds = this.toEdgeIds(),
        type = this.type,
        x = this.x,
        y = this.y
)

val OutputType = "OutputNode"
val InputType = "InputNode"
val NodeRefNode = "NodeRefNode"
