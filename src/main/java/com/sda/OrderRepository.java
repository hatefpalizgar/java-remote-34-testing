package com.sda;

import javax.swing.text.html.Option;
import java.util.Optional;

// Repository is an abstraction layer that
// the service uses to connect to database
public interface OrderRepository {

    void save(Order order);

    Optional<Order> findById(String orderId);

    void update(Order order);

    boolean exists(String orderId);
}
