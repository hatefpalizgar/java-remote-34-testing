package com.sda.exercises;

public class FahrenheitCelsiusConverter {


    // Formula: (°F - 32) × 5/9 = °C
    public double toCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5.0 / 9.0;
    }


    // Formula: (°C × 9/5) + 32 = °F
    public double toFahrenheit(double celsius) {
        return (celsius * 9.0 / 5.0) + 32;
    }
}
