package de.unruh.javapatterns.test;

import de.unruh.javapatterns.Capture;
import de.unruh.javapatterns.MatchException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

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
    void isCapture() throws Exception {
        Capture<String> x = Capture("x");

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
    void notNull() throws Exception {
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
    void and() throws Exception {
        Capture<String> x = Capture("x");
        Capture<String> y = Capture("y");
        String result = match("hello",
                And(NotNull(x), y), () -> x.v()+y.v());
        assertEquals("hellohello", result);
    }

    @SuppressWarnings("Convert2MethodRef")
    @Test
    void or() throws Exception {
        Capture<String> x = Capture("x");
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
    void instance() throws Exception {
        Capture<DemoSome<String>> x = Capture("x");
        //noinspection Convert2MethodRef
        match(new DemoSome<>("Test"),
                Instance(DemoNone.class, Any), () -> fail(),
                new Instance<DemoSome<String>>(x) {}, () ->
                        assertEquals("Test", x.v().value));
    }

    @Test
    void pred() throws Exception {
        int result = match(-123,
                Pred(v -> v > 0), () -> 1,
                Pred(v -> v < 0), () -> 2);
        assertEquals(2, result);
    }

    @Test
    void noMatch() throws Exception {
        int result = match("Test",
                NoMatch(Is("Test")), () -> 1,
                NoMatch(Is("TEST")), () -> 2);
        assertEquals(2, result);
    }

    @Test
    void captureTwice() throws Exception {
        Capture<String> x = Capture("x");

        assertThrows(RuntimeException.class,
                () -> match(new String[] {"x","x"},
                        Array(x,x), () -> 1));
    }
}