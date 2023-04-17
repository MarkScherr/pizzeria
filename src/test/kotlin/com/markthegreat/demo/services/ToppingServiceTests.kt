package com.markthegreat.demo.services

import com.markthegreat.demo.models.Topping
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class ToppingServiceTests() {

    private val restTemplate = mock(RestTemplate::class.java)
    private val service = ToppingService(restTemplate)

    @Test
    fun testGetToppings() {
        val expectedToppings = listOf(Topping("jane@example.com", listOf("pepperoni", "mushrooms")))
        `when`(restTemplate.getForEntity("https://madeupapi.com/toppings", Array<Topping>::class.java))
            .thenReturn(ResponseEntity.ok(expectedToppings.toTypedArray()))
        val actualToppings = service.getToppings()
        assertEquals(expectedToppings, actualToppings)
    }

    @Test
    fun testGetTotalCountPerTopping() {
        // Given
        val toppings = listOf(Topping("jane@example.com", listOf("pepperoni", "mushrooms")))

        // When
        val actualCount = service.getTotalCountPerTopping(toppings)

        // Then
        assertEquals(mapOf("pepperoni" to 1, "mushrooms" to 1), actualCount)
    }

    @Test
    fun testGetUniqueUserCountPerTopping() {
        // Given
        val toppings = listOf(
            Topping("jane@example.com", listOf("pepperoni", "mushrooms")),
            Topping("john@example.com", listOf("pepperoni", "olives"))
        )
        val actualCount = service.getUniqueUserCountPerTopping(toppings)
        assertEquals(mapOf("pepperoni" to 2, "mushrooms" to 1, "olives" to 1), actualCount)
    }

    @Test
    fun testGetMostPopularToppings() {
        val toppings = listOf(
            Topping("jane@example.com", listOf("pepperoni", "mushrooms")),
            Topping("john@example.com", listOf("pepperoni", "olives"))
        )
        val actualToppings = service.getMostPopularToppings(toppings)
        assertEquals(listOf("pepperoni"), actualToppings)
    }

    @Test
    fun testGetLeastPopularToppings() {
        val toppings = listOf(
            Topping("jane@example.com", listOf("pepperoni", "mushrooms")),
            Topping("john@example.com", listOf("pepperoni", "olives")),
            Topping("jane@example.com", listOf("anchovies"))
        )
        val actualToppings = service.getLeastPopularToppings(toppings)
        assertEquals(listOf("mushrooms", "olives", "anchovies"), actualToppings)
    }
    @Test
    fun testGetTop10ToppingLists() {
        val toppings = listOf(
            Topping("email1", listOf("pepperoni", "mushrooms", "onions")),
            Topping("email2", listOf("pepperoni", "mushrooms", "onions")),
            Topping("email3", listOf("sausage", "peppers", "onions")),
            Topping("email4", listOf("sausage", "peppers", "onions")),
            Topping("email5", listOf("sausage", "onions")),
            Topping("email6", listOf("sausage", "onions")),
            Topping("email7", listOf("ham", "pineapple", "jalapenos")),
            Topping("email8", listOf("ham", "pineapple", "jalapenos")),
            Topping("email9", listOf("pepperoni", "sausage", "bacon")),
            Topping("email10", listOf("pepperoni", "sausage", "bacon")),
            Topping("email11", listOf("mushrooms", "onions", "olives")),
            Topping("email12", listOf("mushrooms", "onions", "olives")),
            Topping("email13", listOf("pepperoni", "mushrooms", "olives")),
            Topping("email14", listOf("pepperoni", "mushrooms", "olives")),
            Topping("email15", listOf("pepperoni", "liver", "bacon")),
            Topping("email16", listOf("pepperoni", "liver", "bacon")),
            Topping("email17", listOf("mushrooms", "onions")),
            Topping("email18", listOf("mushrooms", "onions")),
            Topping("email19", listOf("mushrooms", "olives")),
            Topping("email20", listOf("mushrooms", "olives")),
            Topping("email21", listOf("pepperoni", "sausage", "bacon", "pineapple")),
            Topping("email22", listOf("pepperoni", "sausage", "bacon", "pineapple",  "onions")),
            Topping("email23", listOf("pepperoni", "sausage", "bacon", "pineapple",  "onions",  "mushrooms"))
        )
        val expectedResult = mutableSetOf<List<String>>()
        expectedResult.add(listOf("pepperoni", "mushrooms", "onions").sorted())
        expectedResult.add(listOf("sausage", "peppers", "onions").sorted())
        expectedResult.add(listOf("sausage", "onions").sorted())
        expectedResult.add(listOf("ham", "pineapple", "jalapenos").sorted())
        expectedResult.add(listOf("pepperoni", "sausage", "bacon").sorted())
        expectedResult.add(listOf("mushrooms", "onions", "olives").sorted())
        expectedResult.add(listOf("pepperoni", "mushrooms", "olives").sorted())
        expectedResult.add(listOf("pepperoni", "liver", "bacon").sorted())
        expectedResult.add(listOf("mushrooms", "onions").sorted())
        expectedResult.add(listOf("mushrooms", "olives").sorted())

        val top10Lists = service.getTop10ToppingLists(toppings)
        assertEquals(10, top10Lists.size)
        for (list in top10Lists) {
            if (list.sorted() in expectedResult) {
                expectedResult.remove(list)
            }
        }

        assertTrue(expectedResult.isEmpty())
    }
}
