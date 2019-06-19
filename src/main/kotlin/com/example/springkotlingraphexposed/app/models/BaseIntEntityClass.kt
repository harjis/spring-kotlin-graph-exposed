package com.example.springkotlingraphexposed.app.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

abstract class BaseIntEntityClass<E : IntEntity>(table: IntIdTable) : IntEntityClass<E>(table) {
    val errors: MutableSet<Pair<String, String>> = mutableSetOf()

    abstract fun validate(): MutableSet<Pair<String, String>>

    fun newWithValidate(init: E.() -> Unit): E? {
        errors.addAll(validate())
        return if (errors.count() == 0) {
            super.new(init)
        } else {
            // What I would like to do here is create an instance of the Entity and not persist it
            println("ERRORS!")
            return null
        }
    }
}
