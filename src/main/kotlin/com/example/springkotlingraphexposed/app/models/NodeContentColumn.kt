package com.example.springkotlingraphexposed.app.models

interface ContentJson {
    val type: String
}

data class Aggregate(val column: String, val function: String)
data class AggregatesJson(
        override val type: String = "aggregate",
        val groupBy: String,
        val aggregates: List<Aggregate>
) : ContentJson

data class SomeOtherJson(
        override val type: String = "some_other"
) : ContentJson

fun getContentColumnMap(): Map<String, Class<out ContentJson>> {
    return mapOf(
            "aggregate" to AggregatesJson::class.java,
            "some_other" to SomeOtherJson::class.java
    )
}

fun isValidContent(type: String): Boolean {
    return getContentColumnMap().keys.contains(type)
}
