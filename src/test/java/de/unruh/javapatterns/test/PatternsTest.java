package de.unruh.javapatterns.test;

import de.unruh.javapatterns.Capture;
import de.unruh.javapatterns.MatchException;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static de.unruh.javapatterns.Pattern.capture;
import static de.unruh.javapatterns.Patterns.*;
import static de.unruh.javapatterns.Match.*;
import static org.junit.jupiter.api.Assertions.*;

class PatternsTest {

    @Test
    void is() throws MatchException {
        String result = match(123,
                Is(234), () -> "first",
                Is(123), () -> "second",
                Is(345), () -> "third");
        assertEquals("second", result);
    }

    @Test
    void isCapture() throws MatchException {
        Capture<String> x = capture("x");

        int result = match(new String[] { "1","1" },
                Array(x,Is(x)), () -> 1,
                Any, () -> 2);
        assertEquals(1, result);

        result = match(new String[] { "1","2" },
                Array(x,Is(x)), () -> 1,
                Any, () -> 2);
        assertEquals(2, result);
    }

    @Test
    void array() throws MatchException {
        Capture<Integer> x = capture("x");
        Capture<Integer> y = capture("y");
        Capture<Integer> z = capture("z");

        int result = match(new Integer[] { 1,2 },

                Array(x, y, z), () -> 99,
                Array(x, y), () -> {
                    assertEquals(1, x.v());
                    assertEquals(2, y.v());
                    return 100;
                });
        assertEquals(100, result);
    }

    @Test
    void isPredicate() throws MatchException {
        int result = match(-123,
                Is(v -> v > 0), () -> 1,
                Is(v -> v < 0), () -> 2);
        assertEquals(2, result);
    }

    @Test
    void isDelayed() throws MatchException {
        Capture<Integer> x = capture("x");

        int result = match(new Integer[]{2, 4},
                Array(x, Is(() -> x.v() * 2)), () -> 1,
                Any, () -> 2);
        assertEquals(1, result);

        result = match(new Integer[]{2, 5},
                Array(x, Is(() -> x.v() * 2)), () -> 1,
                Any, () -> 2);
        assertEquals(2, result);
    }

    @Test
    void any() throws MatchException {
        int result = match("hello",
                Any, () -> 1);
        assertEquals(1, result);
    }

    @Test
    void nullPattern() throws MatchException {
        int result = match("hello",
                Null, () -> 1,
                Any, () -> 2);
        assertEquals(2, result);

        result = match((String)null,
                Null, () -> 1,
                Any, () -> 2);
        assertEquals(1, result);
    }

    @Test
    void notNull() throws MatchException {
        int result = match("hello",
                NotNull(Any), () -> 1,
                Any, () -> 2);
        assertEquals(1, result);

        result = match((String)null,
                NotNull(Any), () -> 1,
                Any, () -> 2);
        assertEquals(2, result);
    }

    @Test
    void and() throws MatchException {
        Capture<String> x = capture("x");
        Capture<String> y = capture("y");

        String result = match("hello",
                And(NotNull(x), y), () -> x.v()+y.v());
        assertEquals("hellohello", result);
    }

    @SuppressWarnings("Convert2MethodRef")
    @Test
    void or() throws MatchException {
        Capture<String> x = capture("x");
        String result = match(null,
                Or(NotNull(x), x), () -> x.v());
        assertNull(result);
    }

    interface DemoOption<T> {}
    static class DemoNone<T> implements DemoOption<T> {}
    static class DemoSome<T> implements DemoOption<T> {
        final T value;
        DemoSome(T value) { this.value = value; } }

    @Test
    void instance() throws MatchException {
        Capture<DemoSome<String>> x = capture("x");
        //noinspection Convert2MethodRef
        match(new DemoSome<>("Test"),
                Instance(DemoNone.class, Any), () -> fail(),
                new Instance<DemoSome<String>>(x) {}, () ->
                        assertEquals("Test", x.v().value));
    }

    @Test
    void instance2() throws MatchException {
        Capture<String> x = capture("x");
        match(new DemoSome<>((Object)"Test"),
                Instance(String.class, x), () ->
                        assertEquals("Test", x.v()));
    }

    @Test
    void noMatch() throws MatchException {
        int result = match("Test",
                NoMatch(Is("Test")), () -> 1,
                NoMatch(Is("TEST")), () -> 2);
        assertEquals(2, result);
    }

    @Test
    void captureTwice() {
        Capture<String> x = capture("x");

        assertThrows(RuntimeException.class,
                () -> match(new String[] {"x","x"},
                        Array(x,x), () -> 1));
    }
}