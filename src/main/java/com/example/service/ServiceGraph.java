package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.example.model.Image;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.Rating;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.spotify.futures.CompletableFuturesExtra;

import dagger.Module;
import dagger.Provides;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;
import dagger.producers.Production;
import dagger.producers.ProductionComponent;

@ProducerModule
public class ServiceGraph {

    private final Service service;
    private final String userId;

    public ServiceGraph(Service service, String userId) {
        this.service = service;
        this.userId = userId;
    }

    @Produces
    ListenableFuture<List<Order>> orders() {
        return CompletableFuturesExtra.toListenableFuture(service.listOrders(userId));
    }

    @Produces
    ListenableFuture<List<List<Product>>> products(List<Order> orders) {
        List<ListenableFuture<List<Product>>> collect = orders.stream().map(order -> {
            return CompletableFuturesExtra.toListenableFuture(service.listProducts(order));
        }).collect(Collectors.toList());
        return Futures.allAsList(collect);
    }

    @Produces
    List<Product> distinct(List<List<Product>> all) {
        return all.stream().flatMap(List::stream).distinct().collect(Collectors.toList());
    }

    @Produces
    ListenableFuture<List<Rating>> ratings(List<Product> products) {
        return Futures.allAsList(products.stream().map(p -> {
            return CompletableFuturesExtra.toListenableFuture(service.loadRating(p));
        }).collect(Collectors.toList()));
    }

    @Produces
    ListenableFuture<List<Image>> images(List<Product> products) {
        return Futures.allAsList(products.stream().map(p -> {
            return CompletableFuturesExtra.toListenableFuture(service.loadImage(p));
        }).collect(Collectors.toList()));
    }

    @Produces
    List<ProductDetail> convertAll(List<Product> products,
                                   List<Rating> ratings,
                                   List<Image> images) {
        List<ProductDetail> details = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            details.add(new ProductDetail(products.get(i), ratings.get(i), images.get(i)));
        }
        return details;
    }

    @ProductionComponent(modules = { ServiceGraph.class, ExecutorModule.class })
    public interface Component {
        ListenableFuture<List<ProductDetail>> listProductDetails();
    }

}

@Module
final class ExecutorModule {
    @Provides
    @Production
    static Executor executor() {
        return Executors.newCachedThreadPool();
    }
}
