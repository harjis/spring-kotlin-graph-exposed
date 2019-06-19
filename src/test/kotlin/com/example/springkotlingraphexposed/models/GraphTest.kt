package com.example.springkotlingraphexposed.models

import com.example.springkotlingraphexposed.WithTestDatabase
import com.example.springkotlingraphexposed.app.models.Graph
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class GraphTest : WithTestDatabase(){
    @Test
    fun doesSomething(){
        withDb {
            val graph1 = Graph.newWithValidate {
                name = "Graph 1"
            }

            assertThat(Graph.all().count()).isEqualTo(1)
        }
    }
}
