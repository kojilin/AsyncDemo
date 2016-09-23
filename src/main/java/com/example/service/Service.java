package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.example.model.Image;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.Rating;

public class Service {

    public CompletableFuture<List<Order>> listOrders(String userId) {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1L));
        orders.add(new Order(2L));
        orders.add(new Order(3L));
        return CompletableFuture.completedFuture(orders);
    }

    public CompletableFuture<List<Product>> listProducts(Order order) {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1012L, "A1"));
        products.add(new Product(1015L, "A2"));
        products.add(new Product(1021L, "B1"));
        return CompletableFuture.completedFuture(products);
    }

    public CompletableFuture<Rating> loadRating(Product p) {
        return CompletableFuture.completedFuture(new Rating(new Random().nextInt()));
    }

    public CompletableFuture<Image> loadImage(Product p) {
        return CompletableFuture.completedFuture(new Image(UUID.randomUUID().toString()));
    }

}
