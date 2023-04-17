package com.markthegreat.demo.controllers

import com.markthegreat.demo.models.Topping
import com.markthegreat.demo.services.ToppingService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class ToppingControllerTest {
    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var toppingService: ToppingService

    @InjectMocks
    private lateinit var toppingController: ToppingController

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockMvc = MockMvcBuilders.standaloneSetup(toppingController).build()
    }

    @Test
    fun testGetTotalCountPerTopping() {
        val toppings = listOf(Topping("test1@test.com", listOf("pepperoni", "mushroom")),
            Topping("test2@test.com", listOf("pepperoni", "bacon", "olives"))
        )
        whenever(toppingService.getToppings()).thenReturn(toppings)
        whenever(toppingService.getTotalCountPerTopping(toppings)).thenReturn(mapOf("pepperoni" to 2, "mushroom" to 1, "bacon" to 1, "olives" to 1))

        mockMvc.perform(MockMvcRequestBuilders.get("/toppings/totalCount"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json("{\"pepperoni\":2,\"mushroom\":1,\"bacon\":1,\"olives\":1}"))
    }

    @Test
    fun testGetUniqueUserCountPerTopping() {
        val toppings = listOf(Topping("test1@test.com", listOf("pepperoni", "mushroom")),
            Topping("test2@test.com", listOf("pepperoni", "bacon", "olives")),
            Topping("test3@test.com", listOf("pepperoni", "olives", "pineapple")))
        whenever(toppingService.getToppings()).thenReturn(toppings)
        whenever(toppingService.getUniqueUserCountPerTopping(toppings)).thenReturn(mapOf("pepperoni" to 3, "mushroom" to 1, "bacon" to 1, "olives" to 2, "pineapple" to 1))

        mockMvc.perform(MockMvcRequestBuilders.get("/toppings/uniqueCount"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json("{\"pepperoni\":3,\"mushroom\":1,\"bacon\":1,\"olives\":2,\"pineapple\":1}"))
    }

    @Test
    fun testGetMostPopularToppings() {
        val toppings = listOf(Topping("test1@test.com", listOf("pepperoni", "mushroom")),
            Topping("test2@test.com", listOf("pepperoni", "bacon", "olives")),
            Topping("test3@test.com", listOf("pepperoni", "olives", "pineapple")))
        whenever(toppingService.getToppings()).thenReturn(toppings)
        whenever(toppingService.getMostPopularToppings(toppings)).thenReturn(listOf("pepperoni", "olives"))

        mockMvc.perform(MockMvcRequestBuilders.get("/toppings/mostPopular"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json("[\"pepperoni\",\"olives\"]"))
    }

    @Test
    fun `getLeastPopularToppings should return least popular toppings`() {
        val toppings = listOf(Topping("test1@test.com", listOf("pepperoni", "mushroom", "onion")))
        val leastPopularToppings = listOf("anchovies", "olives")
        `when`(toppingService.getToppings()).thenReturn(toppings)
        `when`(toppingService.getLeastPopularToppings(toppings)).thenReturn(leastPopularToppings)
        mockMvc.perform(MockMvcRequestBuilders.get("/toppings/leastPopular"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json("[\"anchovies\",\"olives\"]"))
    }

    @Test
    fun `getTop10ToppingCombinations should return top 10 topping combinations`() {
        val toppings = listOf(Topping("test1@test.com", listOf("pepperoni", "mushroom", "onion")))
        val top10ToppingCombinations = listOf(listOf("pepperoni", "mushroom"), listOf("pepperoni", "onion"))
        `when`(toppingService.getToppings()).thenReturn(toppings)
        `when`(toppingService.getTop10ToppingLists(toppings)).thenReturn(top10ToppingCombinations)
        mockMvc.perform(MockMvcRequestBuilders.get("/toppings/top10ToppingCombinations"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json("[[\"pepperoni\",\"mushroom\"],[\"pepperoni\",\"onion\"]]"))
    }
}