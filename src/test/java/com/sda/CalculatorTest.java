package com.sda;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Calculator Test - Demonstrating Basic JUnit features")
public class CalculatorTest {

    private Calculator calculator;

    @BeforeAll
    static void setUpClass() {
        System.out.println("Called once before all test methods");
    }

    @AfterAll
    static void tearDownClass() {
        System.out.println("Called once after all test methods");
    }

    @BeforeEach
    void setUp() {
        System.out.println("Called before each test method");
        calculator = new Calculator(); // I will get a new fresh Calculator object for each test
    }

    @AfterEach
    void tearDown() {
        System.out.println("Called after each test method");
    }

    @Test
    @DisplayName("Simple addition test")
    void testAdd() {
        // given
        int a = 2;
        int b = 3;

        // when
        int actual = calculator.add(a, b);

        // then
        assertEquals(5, actual, "2 + 3 should equal 5");
    }

    @Test
    @DisplayName("Division by zero should throw exception")
    void testDivideByZero() {
        // given
        int numerator = 1;
        int denominator = 0;

        // when & then
        Exception exception = assertThrows(ArithmeticException.class, () -> calculator.divide(numerator, denominator));
        assertEquals("Division by zero is not allowed", exception.getMessage());
    }


    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource(
            {
                    "0, 1, 1",
                    "1, 2, 3",
                    "-1, 1, 0",
                    "49, 51, 100"
            }
    )
    @DisplayName("Parameterized test for addition")
    void testAddWithParameters(int first, int second, int expected) {
        // given - parameters provided by CsvSource

        // when
        int actual = calculator.add(first, second);

        // then
        assertEquals(expected, actual, first + " + " + second + " should equal " + expected);
    }


    @Test
    @DisplayName("Multiple assertions test")
    void testMultipleAssertions(){
        // version 1 (not suitable)
        // given
        int a = 2;
        int b = 2;

        // when
//        int actualAdd = calculator.add(a, b);
//        int actualSubtract = calculator.subtract(a, b);
//        int actualMultiply = calculator.multiply(a, b);
//        int actualDivide = calculator.divide(a, b);

        // then
//        assertEquals(4, actualAdd);
//        assertEquals(0, actualSubtract);
//        assertEquals(4, actualMultiply);
//        assertEquals(1, actualDivide);


        // version 2 (recommended)
        assertAll("Calculator operations",
                () -> assertEquals(4, calculator.add(a, b)),
                () -> assertEquals(0, calculator.subtract(a, b)),
                () -> assertEquals(4, calculator.multiply(a, b)),
                () -> assertEquals(1, calculator.divide(a, b))
        );

    }

}
