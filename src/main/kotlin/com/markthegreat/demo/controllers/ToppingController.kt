package com.markthegreat.demo.controllers

import com.markthegreat.demo.services.ToppingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/toppings")
class ToppingController(val toppingService: ToppingService) {

    @GetMapping("/totalCount")
    fun getTotalCountPerTopping(): Map<String, Int> {
        val toppings = toppingService.getToppings()
        return toppingService.getTotalCountPerTopping(toppings)
    }

    @GetMapping("/uniqueCount")
    fun getUniqueUserCountPerTopping(): Map<String, Int> {
        val toppings = toppingService.getToppings()
        return toppingService.getUniqueUserCountPerTopping(toppings)
    }

    @GetMapping("/mostPopular")
    fun getMostPopularToppings(): List<String> {
        val toppings = toppingService.getToppings()
        return toppingService.getMostPopularToppings(toppings)
    }

    @GetMapping("/leastPopular")
    fun getLeastPopularToppings(): List<String> {
        val toppings = toppingService.getToppings()
        return toppingService.getLeastPopularToppings(toppings)
    }

    @GetMapping("/top10ToppingCombinations")
    fun getTop10ToppingCombinations():  List<List<String>> {
        val toppings = toppingService.getToppings()
        return toppingService.getTop10ToppingLists(toppings)
    }
}