package com.sda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Test - Demonstrating Mockito")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderCalculatorV2 calculator;

    private OrderService orderService; // SUT

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, calculator);
    }

    @Test
    @DisplayName("Should create new order successfully")
    void shouldCreateNewOrder() {
        // given

        // when

        // then
    }
}
