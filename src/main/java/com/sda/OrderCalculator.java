package com.sda;

public class OrderCalculator {

    public double calculateTotal(double itemPrice, int itemCount, double taxRate, double discount) {
        if (itemPrice < 0 || itemCount < 0 || taxRate < 0 || discount < 0) {
            throw new IllegalArgumentException("Negative values are not allowed");
        }

        double totalPrice = itemPrice * itemCount;

        // applying tax
        totalPrice *= (1 + taxRate);

        // applying discount before tax
        totalPrice -= discount;

        // we used round() since double is not a good choice for monetary values in Java,
        // instead we'd better use BigDecimal
        return Math.max(Math.round(totalPrice), 0);
    }
}
