package com.sda.exercises;

public class Person {
    private final int age;

    public Person(int age) {
        this.age = age;
    }

    public boolean isTeenager() {
        return age > 10 && age < 20;
    }
}
