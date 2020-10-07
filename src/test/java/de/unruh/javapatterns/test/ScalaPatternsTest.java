package de.unruh.javapatterns.test;

import de.unruh.javapatterns.Capture;
import de.unruh.javapatterns.MatchException;
import org.junit.jupiter.api.Test;
import scala.Some;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.util.Arrays;
import java.util.List;

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

    @SuppressWarnings("Convert2MethodRef")
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


    @SuppressWarnings("deprecation")
    @Test
    void seq() throws MatchException {
        Capture<Integer> x = capture("x");
        Capture<Integer> y = capture("y");
        Capture<Integer> z = capture("z");

        int result = match(JavaConverters.asScala(List.of(1,2)).toList(),

                Seq(x, y, z), () -> 99,
                Seq(x, y), () -> {
                    assertEquals(1, x.v());
                    assertEquals(2, y.v());
                    return 100;
                });
        assertEquals(100, result);
    }


    @SuppressWarnings("deprecation")
    @Test
    void seqThese() throws MatchException {
        Capture<Seq<String>> arr = capture("arr");
        Capture<String> x = capture("x"),
                y = capture("y");

        match(JavaConverters.asScala(List.of("this", "is", "a", "test" )).toList(),
                Seq(these(x,y), arr),
                () -> {
                    assertEquals("this", x.v());
                    assertEquals("is", y.v());
                    assertEquals(2, arr.v().length());
                    assertEquals("a", arr.v().apply(0));
                    assertEquals("test", arr.v().apply(1));
                });
    }

}