## Exercise 1 & 2: Person Class and Boundary Testing

Create a `Person` class with:
- An `age` field
- A method `boolean isTeenager()` that returns `true` if age is between 11-19 (exclusive), `false` otherwise

Write tests to verify:
1. Boundary values:
    - Age 10 (lower boundary) → should return false
    - Age 11 (lower boundary + 1) → should return true
    - Age 19 (upper boundary - 1) → should return true
    - Age 20 (upper boundary) → should return false

2. Multiple scenarios using parameterized tests:
```java
@CsvSource({
    "9, false",   // Below lower boundary
    "10, false",  // Lower boundary
    "11, true",   // Lower boundary + 1
    "15, true",   // Middle value
    "19, true",   // Upper boundary - 1
    "20, false",  // Upper boundary
    "21, false"   // Above upper boundary
})
```


## Exercise 3 & 4: Temperature Converter

Create a `FahrenheitCelsiusConverter` class with methods:
1. `double toCelsius(int fahrenheit)`
2. `double toFahrenheit(int celsius)`

Write tests for common temperature conversions:
1. Freezing point:
    - 32°F = 0°C
    - 0°C = 32°F

2. Boiling point:
    - 212°F = 100°C
    - 100°C = 212°F

3. Room temperature:
    - 68°F ≈ 20°C
    - 20°C ≈ 68°F

Use `isCloseTo()` for floating-point comparisons:
```java
assertThat(converter.toCelsius(32))
    .isCloseTo(0.0, within(0.001));
```

## Exercise 5: Mocking the Converter

Create three types of mock tests:

1. Basic mocking:
```java
FahrenheitCelsiusConverter mockConverter = mock(FahrenheitCelsiusConverter.class);
when(mockConverter.toCelsius(32)).thenReturn(0.0);
verify(mockConverter).toCelsius(32);
```

2. Annotation-based mocking with debug output:
```java
@Mock
FahrenheitCelsiusConverter converter;

when(converter.toCelsius(anyInt())).thenAnswer(inv -> {
    System.out.println("Converting " + inv.getArgument(0) + "°F to Celsius");
    return 0.0;
});
```

3. Spying with debug output:
```java
@Spy
FahrenheitCelsiusConverter spyConverter;

doAnswer(inv -> {
    System.out.println("Called toCelsius with " + inv.getArgument(0) + "°F");
    return inv.callRealMethod();
}).when(spyConverter).toCelsius(anyInt());
```

## Evaluation Criteria

Your solution will be evaluated based on:
1. Correct implementation of requirements
2. Test coverage of edge cases
3. Use of appropriate testing techniques
4. Code organization and readability
5. Proper use of mocking frameworks
