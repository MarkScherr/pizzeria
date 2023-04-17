package com.markthegreat.demo.services

import com.markthegreat.demo.models.Topping
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ToppingService(private val restTemplate: RestTemplate) {
    private val url = "https://madeupapi.com/toppings"

    fun getToppings(): List<Topping> {
        val response = restTemplate.getForEntity(url, Array<Topping>::class.java)
        return response.body?.toList() ?: emptyList()
    }

    fun getTotalCountPerTopping(toppings: List<Topping>): Map<String, Int> {
        return toppings.flatMap { it.toppings }
            .groupingBy { it }
            .eachCount()
    }

    fun getUniqueUserCountPerTopping(toppings: List<Topping>): Map<String, Int> {
        val toppingsWithUsers = toppings.flatMap { topping ->
            topping.toppings.map { Pair(it, topping.email) }
        }
        return toppingsWithUsers.distinctBy { it.first to it.second }
            .groupBy({ it.first }, { it.second })
            .mapValues { (_, users) -> users.size }
    }

    fun getMostPopularToppings(toppings: List<Topping>): List<String> {
        return toppings.flatMap { it.toppings }
            .groupingBy { it }
            .eachCount()
            .maxByOrNull { it.value }
            ?.let { listOf(it.key) }
            ?: emptyList()
    }

    fun getLeastPopularToppings(toppings: List<Topping>): List<String> {
        val toppingCounts = toppings.flatMap { it.toppings }
            .groupingBy { it }
            .eachCount()

        val leastCount = toppingCounts.values.minOrNull() ?: return emptyList()
        return toppingCounts.filterValues { it == leastCount }
            .keys
            .toList()
    }

    fun getTop10ToppingLists(toppings: List<Topping>): List<List<String>> {
        val toppingListCounts = mutableMapOf<List<String>, Int>()

        for (topping in toppings) {
            val sortedToppings = topping.toppings.sorted()
            val count = toppingListCounts.getOrDefault(sortedToppings, 0)
            toppingListCounts[sortedToppings] = count + 1
        }

        return toppingListCounts.entries
            .sortedByDescending { it.value }
            .take(10)
            .map { it.key }
    }
}