package com.sda;

public class Calculator {


    public int add(int x, int y) {
        return x + y;
    }

    public int subtract(int x, int y) {
        return x - y;
    }

    public int multiply(int x, int y) {
        return x * y;
    }

    public int divide(int x, int y) {
        if (y == 0) {
            throw new ArithmeticException("Division by zero is not allowed");
        }

        return x / y;
    }


}
