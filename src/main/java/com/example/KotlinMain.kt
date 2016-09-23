package com.example

import com.example.service.ProductDetail
import com.example.service.Service
import kotlinx.coroutines.async
import java.util.concurrent.CompletableFuture

/**
 * list orders--> list products for each order --> load image for each product
 *                                             --> load rating for each product
 */
fun main(args: Array<String>) {
    val service = Service()
    val result = async<List<ProductDetail>> {
        val ordersFuture = await(service.listOrders("foo"))

        val productsFutures = ordersFuture.map { service.listProducts(it) }
        await(CompletableFuture.allOf(*productsFutures.toTypedArray()))
        val products = productsFutures.map { it.join() }.flatMap { it }.distinct()

        val ratingFutures = products.map { service.loadRating(it) }
        val imageFutures = products.map { service.loadImage(it) }

        await(CompletableFuture.allOf(*ratingFutures.toTypedArray()))
        await(CompletableFuture.allOf(*imageFutures.toTypedArray()))

        products.mapIndexed { i, product -> ProductDetail(product, ratingFutures[i].join(), imageFutures[i].join()) }

    }
    println(result.join())
}
