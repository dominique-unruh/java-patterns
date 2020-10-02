package de.unruh.javapatterns.test;

import de.unruh.javapatterns.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static de.unruh.javapatterns.Match.match;
import static de.unruh.javapatterns.Pattern.capture;
import static org.junit.jupiter.api.Assertions.*;

class PatternDocumentationTest {
    static class Pair<X,Y> {
        public final X first;
        public final Y second;

        Pair(X first, Y second) {
            this.first = first;
            this.second = second;
        }
    }

    static <X, Y> Pattern<Pair<X, Y>> Pair(Pattern<? super X> firstPattern, Pattern<? super Y> secondPattern) {
        return new Pattern<>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Pair<X, Y> value) throws PatternMatchReject {
                if (value == null) reject();
                firstPattern.apply(mgr, value.first);
                secondPattern.apply(mgr, value.second);
            }

            @Override
            public String toString() {
                return "Pair(" + firstPattern + "," + secondPattern + ")";
            }
        };
    }

    @Test
    void example1() throws MatchException {
        Capture<String> x = capture("x");
        Capture<Integer> y = capture("y");

        Pair<String, Integer> pair = new Pair<>("String", 123);

        match(pair,

                Pair(x,y),
                () -> { assertEquals("String", x.v());
                        assertEquals(123, y.v()); });
    }

    static Pattern<String> FullName(Pattern<String> firstNamePattern, Pattern<String> lastNamePattern) {
        return new Pattern<>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable String value) throws PatternMatchReject {
                if (value == null) reject();
                String[] parts = value.split(" ");
                if (parts.length != 2) reject();
                firstNamePattern.apply(mgr, parts[0]);
                lastNamePattern.apply(mgr, parts[1]);
            }

            @Override
            public String toString() {
                return "FullName(" + firstNamePattern + "," + lastNamePattern + ")";
            }
        };
    }

    @Test
    void example2() throws MatchException {
        Capture<String> x = capture("x");
        Capture<String> y = capture("y");

        match("John Doe",

                FullName(x, y),
                () -> {
                    assertEquals("John", x.v());
                    assertEquals("Doe", y.v());
                });
    }
}