package com.sda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.Or;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Test - Demonstrating Mockito")
class OrderServiceTest {

    // Mocking has benefits:
    // 1. You don't need real objects
    // 2. It's cheap to create
    // 3. They'll be excluded from your unit tests (tests become lighter)
    @Mock
    private OrderRepository repository;

    @Mock
    private OrderCalculatorV2 calculator;

    private OrderService orderService; // SUT

    @BeforeEach
    void setUp() {
        orderService = new OrderService(repository, calculator);
    }

    @Nested
    @DisplayName("Create Order Test")
    class CreateOrderTests {

        @Test
        @DisplayName("Should create new order successfully")
        void shouldCreateNewOrder() {
            // we have to tell calculator mock object how to behave when 'calculateTotal' is called upon it
            when(calculator.calculateTotal(100, 2, 0.1, 0, 0))
                    .thenReturn(220.0);

            // given & when
            Order order = orderService.createOrder("order1", 100, 2, 0.1);

            // then
            assertThat(order)
                    .extracting(Order::getOrderId)
                    .matches(orderId -> orderId.equals("order1"));

            assertThat(order)
                    .extracting(Order::getAmount, Order::getStatus)
                    .containsExactly(220.0, OrderStatus.CREATED);

            // now, I need to verify if the order is saved into the database (i.e. verify save() is called)
            verify(repository).save(order); // verifies the exact same order object created
            verify(repository).save(any(Order.class)); // verifies any order object
        }

        @Test
        @DisplayName("Should throw exception for duplicate order")
        void shouldThrowExceptionForDuplicateOrder() {
            // given
            when(repository.exists("order1")).thenReturn(true);

            // when & then
            assertThatThrownBy(
                    () -> orderService.createOrder("order1", 100, 2, 0.1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Order already exists");
        }
    }


    @Nested
    @DisplayName("Confirm Order Tests")
    class ConfirmOrderTest {

        @Test
        @DisplayName("Should confirm pending order")
            // happy path test
        void shouldConfirmPendingOrder() {
            // given
            Order order = new Order("order1", 100.0);
            when(repository.findById("order1")).thenReturn(Optional.of(order));

            // when
            orderService.confirmOrder("order1");

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
            verify(repository).update(order);
        }

        @Test
        @DisplayName("Should not confirm cancelled order")
        void shouldNotConfirmCancelledOrder() {
            // given
            Order order = new Order("order1", 100.0);
            order.setStatus(OrderStatus.CANCELLED);
            when(repository.findById("order1")).thenReturn(Optional.of(order));

            // when & then
            assertThatThrownBy(() -> orderService.confirmOrder("order1"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot confirm cancelled order");
            assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
            verify(repository, never()).update(order);
        }
    }

    @Nested
    @DisplayName("Cancel Order Tests")
    class CancelOrderTest {

        @Test
        @DisplayName("Should cancel created order")
            // happy path
        void shouldCancelCreatedOrder() {
            // given
            Order order = new Order("order1", 100.0);
            when(repository.findById("order1")).thenReturn(Optional.of(order));

            // when
            orderService.cancelOrder("order1");

            // then (version 1)
            assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
            verify(repository).update(order);

            // then (version 2)
            verify(repository).update(argThat((updatedOrder) ->
                    updatedOrder.getStatus() == OrderStatus.CANCELLED));
        }

        // TODO: complete this
        @Test
        @DisplayName("Should throw exception for non-existent order")
        void ShouldThrowExceptionForNonExistentOrder() {

        }
    }
}
