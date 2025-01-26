package com.sda;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("OrderCalculatorTestV2 Test - Demonstrating TDD")
class OrderCalculatorV2Test {

    @Nested
    @DisplayName("First TDD Cycle - Basic Functionality")
    class FirstTDDCycle {

        @Test
        @DisplayName("RED - calculate total with tax and discount above threshold")
        void shouldCalculateTotal_withTax_andDiscount_aboveThreshold() {
            // given
            OrderCalculatorV2 calculator = new OrderCalculatorV2();

            // when
            double total = calculator.calculateTotal(100, 2, 0.1, 50, 150);

            // then
            assertThat(total)
                    .as("Total should be calculated with tax applied before discount")
                    .isEqualTo(165) // wrong calculation
                    .isNotEqualTo(170); // correct calculation
        }
    }

    @Nested
    @DisplayName("Second TDD Cycle - Edge Cases")
    class SecondTDDCycle {

        @Test
        @DisplayName("Should not apply discount below threshold")
        void shouldNotApplyDiscountBelowThreshold() {
            // given
            OrderCalculatorV2 calculator = new OrderCalculatorV2();

            // when
            double total = calculator.calculateTotal(100, 1, 0.1, 50, 150);

            // then
            assertThat(total)
                    .as("Discount should not be applied when total is below threshold")
                    .isCloseTo(110, within(0.001));
        }

        @Test
        @DisplayName("Should handle zero values")
        void shouldHandleZeroValues() {
            // given
            OrderCalculatorV2 calculator = new OrderCalculatorV2();

            // when
            double total = calculator.calculateTotal(0, 0, 0, 0, 0);

            // then
            assertThat(total)
                    .as("Zero values should result in zero total")
                    .isZero();
        }
    }


    @Nested
    @DisplayName("Third TDD Cycle - Input Validation")
    class ThirdTDDCycle {

        @Test
        @DisplayName("Should reject negative values")
        void shouldRejectNegativeValues() {
            // given
            OrderCalculatorV2 calculator = new OrderCalculatorV2();

            // when & then
            assertThatThrownBy(() -> calculator.calculateTotal(-100, 1, 0.1, 50, 150))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Negative values are not allowed");
        }

        @Test
        @DisplayName("Should handle extreme values")
        void shouldHandleExtremeValues() {
            // given
            OrderCalculatorV2 calculator = new OrderCalculatorV2();

            // when
            double total = calculator.calculateTotal(Double.MAX_VALUE, 1, 0.1, 50, 150);

            // then
            assertThat(total)
                    .as("Should handle large numbers without overflow")
                    .isPositive()
                    .isFinite();
        }
    }

    @Nested
    @DisplayName("Fourth TDD Cycle - Bug Fix")
    class FourthTDDCycle {

        @Test
        @DisplayName("GREEN - Should apply discount after tax")
        void shouldApplyDiscountAfterTax() {
            // given
            OrderCalculatorV2 calculator = new OrderCalculatorV2();

            // when
            double total = calculator.calculateTotal(100, 2, 0.1, 50, 150);

            // then
            assertThat(total)
                    .as("Total should be calculated with tax before discount")
                    .isEqualTo(170);
        }
    }

}
