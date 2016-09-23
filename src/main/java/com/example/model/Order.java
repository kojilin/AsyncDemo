package com.example.model;

public class Order {

    private final long orderId;

    public Order(long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "Order{" +
               "orderId=" + orderId +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        Order order = (Order) o;

        if (orderId != order.orderId) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (orderId ^ (orderId >>> 32));
    }
}
