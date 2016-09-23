package com.example.service;

import com.example.model.Image;
import com.example.model.Product;
import com.example.model.Rating;

public class ProductDetail {
    Product product;
    Rating rating;
    Image image;

    public ProductDetail(Product product, Rating rating, Image image) {
        this.product = product;
        this.rating = rating;
        this.image = image;
    }

    @Override
    public String toString() {
        return "ProductDetail{" +
               "product=" + product +
               ", rating=" + rating +
               ", image=" + image +
               '}';
    }
}
