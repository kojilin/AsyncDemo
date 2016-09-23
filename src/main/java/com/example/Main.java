package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.example.model.Image;
import com.example.model.Product;
import com.example.model.Rating;
import com.example.service.ProductDetail;
import com.example.service.Service;

public class Main {

    /**
     * list orders--> list products for each order --> load image for each product
     *                                             --> load rating for each product
     */
    public static void main(String[] args) {
        Service service = new Service();

        CompletableFuture<List<CompletableFuture<List<Product>>>> listProductsOfOrdersFuture =
                service.listOrders("foo")
                       .thenApply(orders -> {
                           return orders.stream().map(order -> {
                               return service.listProducts(order);
                           }).collect(Collectors.toList());
                       });

        CompletableFuture<List<Product>> productFutures = listProductsOfOrdersFuture.thenCompose(futures -> {
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0])).thenApply(
                    aVoid -> {
                        return futures.stream().map(CompletableFuture::join).flatMap(
                                products -> products.stream()).distinct().collect(Collectors.toList());
                    });
        });

        CompletableFuture<List<Rating>> ratingFutures = productFutures.thenApply(products -> {
            return products.stream().map(service::loadRating).collect(Collectors.toList());
        }).thenCompose(futures -> {
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0])).thenApply(
                    aVoid -> {
                        return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
                    });
        });

        CompletableFuture<List<Image>> imageFutures = productFutures.thenApply(products -> {
            return products.stream().map(service::loadImage).collect(Collectors.toList());
        }).thenCompose(futures -> {
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0])).thenApply(
                    aVoid -> {
                        return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
                    });
        });

        CompletableFuture<List<ProductDetail>> result =
                CompletableFuture.allOf(imageFutures, ratingFutures)
                                 .thenApply(aVoid -> {
                                     List<ProductDetail> details = new ArrayList<>();
                                     for (int i = 0; i < productFutures.join().size(); i++) {
                                         details.add(new ProductDetail(productFutures.join().get(i),
                                                                       ratingFutures.join().get(i),
                                                                       imageFutures.join().get(i)
                                         ));
                                     }
                                     return details;
                                 });
        System.out.println(result.join());

    }

}
