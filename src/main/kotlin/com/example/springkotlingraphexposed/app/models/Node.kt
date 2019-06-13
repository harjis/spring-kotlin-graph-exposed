package com.example.springkotlingraphexposed.app.models

import com.example.springkotlingraphexposed.app.tables.Edges
import com.example.springkotlingraphexposed.app.tables.Nodes
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import java.lang.Exception

class Node(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Node>(Nodes)

    var name by Nodes.name
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
    var graph by Graph referencedOn Nodes.graph
    val fromEdges by Edge referrersOn Edges.fromNode
    val toEdges by Edge referrersOn Edges.toNode
}
