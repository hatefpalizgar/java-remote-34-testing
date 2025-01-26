package com.sda;

public class OrderService {
    private final OrderRepository repository;
    private final OrderCalculatorV2 calculator;

    public OrderService(OrderRepository repository, OrderCalculatorV2 calculator) {
        this.repository = repository;
        this.calculator = calculator;
    }

    public Order createOrder(String orderId, double itemPrice, int itemCount, double taxRate) {
        if (repository.exists(orderId)) {
            throw new IllegalArgumentException("Order already exists");
        }

        double total = calculator.calculateTotal(itemPrice, itemCount, taxRate, 0, 0);

        Order order = new Order(orderId, total);

        repository.save(order);
        return order;
    }

}
