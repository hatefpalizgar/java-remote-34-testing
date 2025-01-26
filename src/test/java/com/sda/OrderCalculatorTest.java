package com.sda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("OrderCalculator Test - Demonstrating AssertJ Features")
class OrderCalculatorTest {

    // Subject Under Test (SUT)
    private OrderCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new OrderCalculator();
    }

    @Test
    @DisplayName("Basic calculation with AssertJ")
    void testBasicCalculation() {
        // arrange (given)
        double itemPrice = 100.0;
        int count = 2;
        double tax = 0.1;
        double discount = 20.0;

        // act (when)
        double actual = calculator.calculateTotal(itemPrice, count, tax, discount);

        // assert (then) using JUnit
        // assertEquals(200.0, actual);

        // assert (then) using AssertJ
        assertThat(actual)
                .as("Total price calculation")
                .isEqualTo(200.0);
    }

    @Test
    @DisplayName("Multiple assertions with AssertJ")
    void testMultipleScenarios() {
        // given
        double itemPrice = 100.0;
        int count = 2;
        double tax = 0.1;
        double discount = 20.0;

        // when
        double actual = calculator.calculateTotal(itemPrice, count, tax, discount);

        // then
        assertThat(actual)
                .isPositive()
                .isGreaterThan(100)
                .isLessThan(500);
    }

    @Test
    @DisplayName("Exception testing with AssertJ")
    void testNegativeValues() {
        // given
        double itemPrice = -100.0;
        int count = 1;
        double tax = 0.1;
        double discount = 0.0;

        // when & then
        assertThatThrownBy(() -> calculator.calculateTotal(itemPrice, count, tax, discount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Negative values are not allowed");
    }

    @Test
    @DisplayName("Edge cases with AssertJ")
    void testEdgeCases() {
        // edge case 1: all zero inputs
        // given & when & then
        assertThat(calculator.calculateTotal(0, 0, 0, 0))
                .as("Zero values should result in zero total")
                .isZero();

        // edge case 2: very big discount
        assertThat(calculator.calculateTotal(100, 1, 0.1, 1000))
                .as("Discount larger than total price should result in zero total")
                .isZero();
    }

}
