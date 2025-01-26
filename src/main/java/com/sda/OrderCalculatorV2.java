package com.sda;

public class OrderCalculatorV2 {
    public double calculateTotal(double itemPrice, int count, double taxRate, double discount, double discountThreshold) {
        if (itemPrice < 0 || count < 0 || taxRate < 0 || discount < 0 || discountThreshold < 0) {
            throw new IllegalArgumentException("Negative values are not allowed");
        }

        double totalPrice = itemPrice * count;

        // Bug: discount is applied before tax

        // Apply the discount only if the order's totalPrice is greater than the threshold
        if (totalPrice > discountThreshold) {
            totalPrice -= discount;
        }

        totalPrice *= (1 + taxRate);

        return Math.max(Math.round(totalPrice), 0);
    }
}
