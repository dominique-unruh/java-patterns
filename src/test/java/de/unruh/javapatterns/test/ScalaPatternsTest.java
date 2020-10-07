package de.unruh.javapatterns.test;

import de.unruh.javapatterns.Capture;
import de.unruh.javapatterns.MatchException;
import org.junit.jupiter.api.Test;
import scala.Some;

import static de.unruh.javapatterns.Match.match;
import static de.unruh.javapatterns.Pattern.capture;
import static de.unruh.javapatterns.Patterns.*;
import static de.unruh.javapatterns.ScalaPatterns.*;
import static org.junit.jupiter.api.Assertions.*;

class ScalaPatternsTest {

    @SuppressWarnings("Convert2MethodRef")
    @Test
    void some() throws MatchException {
        Capture<Integer> x = capture("x");

        match (Some.apply(123),

                Some(x), () -> assertEquals(123, x.v()),
                Any, () -> fail()
        );

        match (scala.Option.empty(),

                Some(x), () -> fail(),
                Any, () -> {}
        );
    }

    @Test
    void none() throws MatchException {
        match (Some.apply(123),

                None, () -> fail(),
                Any, () -> {}
        );

        match (scala.Option.empty(),

                None, () -> {},
                Any, () -> fail()
        );
    }
}