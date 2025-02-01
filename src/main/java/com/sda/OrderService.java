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

    public void confirmOrder(String orderId) {
        // find the order in database
        Order order = repository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // if order status is cancelled, then throw exception
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot confirm cancelled order");
        }

        // confirm the order
        order.setStatus(OrderStatus.CONFIRMED);

        // update the data in database
        repository.update(order);
    }

    public void cancelOrder(String orderId) {
        Order order = repository
                .findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new IllegalArgumentException("Cannot cancel confirmed order");
        }

        order.setStatus(OrderStatus.CANCELLED);
        repository.update(order);
    }

    // a private method is usually not tested
    // since:
    // 1. it's private and planned not to be
    // exposed to other parts of the code
    // 2. a private method is called within
    // a public method, so if you test the public method
    // you're automatically testing the private one
    private void someWork() {
        // ...
    }

}
