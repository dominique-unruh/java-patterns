package de.unruh.javapatterns.test;

import de.unruh.javapatterns.Capture;
import de.unruh.javapatterns.MatchException;
import org.junit.jupiter.api.Test;
import scala.*;
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

    @SuppressWarnings("Convert2MethodRef")
    @Test
    void tuple1() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        int result = match(new Tuple1<Integer>(1),
                Tuple(x1), () -> x1.v());
        assertEquals(1, result);
    }

    @Test
    void tuple2() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        String result = match(new Tuple2<Integer,Integer>(1,2),
                Tuple(x1,x2), () -> ""+x1.v()+x2.v());
        assertEquals("12", result);
    }

    @Test
    void tuple3() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        String result = match(new Tuple3<>(1,2,3),
                Tuple(x1,x2,x3), () -> ""+x1.v()+x2.v()+x3.v());
        assertEquals("123", result);
    }

    @Test
    void tuple4() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        String result = match(new Tuple4<>(1,2,3,4),
                Tuple(x1,x2,x3,x4), () -> ""+x1.v()+x2.v()+x3.v()+x4.v());
        assertEquals("1234", result);
    }

    @Test
    void tuple5() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        String result = match(new Tuple5<>(1,2,3,4,5),
                Tuple(x1,x2,x3,x4,x5), () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v());
        assertEquals("12345", result);
    }

    @Test
    void tuple6() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        String result = match(new Tuple6<>(1,2,3,4,5,6),
                Tuple(x1,x2,x3,x4,x5,x6), () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v());
        assertEquals("123456", result);
    }

    @Test
    void tuple7() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        Capture<Integer> x7 = capture("x7");
        String result = match(new Tuple7<>(1,2,3,4,5,6,7),
                Tuple(x1,x2,x3,x4,x5,x6,x7), () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v()+x7.v());
        assertEquals("1234567", result);
    }

    @Test
    void tuple8() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        Capture<Integer> x7 = capture("x7");
        Capture<Integer> x8 = capture("x8");
        String result = match(new Tuple8<>(1,2,3,4,5,6,7,8),
                Tuple(x1,x2,x3,x4,x5,x6,x7,x8), () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v()+x7.v()+x8.v());
        assertEquals("12345678", result);
    }

    @Test
    void tuple9() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        Capture<Integer> x7 = capture("x7");
        Capture<Integer> x8 = capture("x8");
        Capture<Integer> x9 = capture("x9");
        String result = match(new Tuple9<>(1,2,3,4,5,6,7,8,9),
                Tuple(x1,x2,x3,x4,x5,x6,x7,x8,x9), () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v()+x7.v()+x8.v()+x9.v());
        assertEquals("123456789", result);
    }

    @Test
    void tuple10() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        Capture<Integer> x7 = capture("x7");
        Capture<Integer> x8 = capture("x8");
        Capture<Integer> x9 = capture("x9");
        Capture<Integer> x10 = capture("x10");
        String result = match(new Tuple10<>(1,2,3,4,5,6,7,8,9,10),
                Tuple(x1,x2,x3,x4,x5,x6,x7,x8,x9,x10),
                () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v()+x7.v()+x8.v()+x9.v()+x10.v());
        assertEquals("12345678910", result);
    }

    @Test
    void tuple11() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        Capture<Integer> x7 = capture("x7");
        Capture<Integer> x8 = capture("x8");
        Capture<Integer> x9 = capture("x9");
        Capture<Integer> x10 = capture("x10");
        Capture<Integer> x11 = capture("x11");
        String result = match(new Tuple11<>(1,2,3,4,5,6,7,8,9,10,11),
                Tuple(x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11),
                () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v()+x7.v()+x8.v()+x9.v()+x10.v()+x11.v());
        assertEquals("1234567891011", result);
    }

    @Test
    void tuple12() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        Capture<Integer> x7 = capture("x7");
        Capture<Integer> x8 = capture("x8");
        Capture<Integer> x9 = capture("x9");
        Capture<Integer> x10 = capture("x10");
        Capture<Integer> x11 = capture("x11");
        Capture<Integer> x12 = capture("x12");
        String result = match(new Tuple12<>(1,2,3,4,5,6,7,8,9,10,11,12),
                Tuple(x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12),
                () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v()+x7.v()+x8.v()+x9.v()+x10.v()+x11.v()+x12.v());
        assertEquals("123456789101112", result);
    }

    @Test
    void tuple13() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        Capture<Integer> x7 = capture("x7");
        Capture<Integer> x8 = capture("x8");
        Capture<Integer> x9 = capture("x9");
        Capture<Integer> x10 = capture("x10");
        Capture<Integer> x11 = capture("x11");
        Capture<Integer> x12 = capture("x12");
        Capture<Integer> x13 = capture("x13");
        String result = match(new Tuple13<>(1,2,3,4,5,6,7,8,9,10,11,12,13),
                Tuple(x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13),
                () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v()+x7.v()+x8.v()+x9.v()+x10.v()+x11.v()+x12.v()+x13.v());
        assertEquals("12345678910111213", result);
    }

    @Test
    void tuple14() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        Capture<Integer> x7 = capture("x7");
        Capture<Integer> x8 = capture("x8");
        Capture<Integer> x9 = capture("x9");
        Capture<Integer> x10 = capture("x10");
        Capture<Integer> x11 = capture("x11");
        Capture<Integer> x12 = capture("x12");
        Capture<Integer> x13 = capture("x13");
        Capture<Integer> x14 = capture("x14");
        String result = match(new Tuple14<>(1,2,3,4,5,6,7,8,9,10,11,12,13,14),
                Tuple(x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14),
                () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v()+x7.v()+x8.v()+x9.v()+x10.v()+x11.v()+x12.v()
                        +x13.v()+x14.v());
        assertEquals("1234567891011121314", result);
    }

    @Test
    void tuple15() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        Capture<Integer> x7 = capture("x7");
        Capture<Integer> x8 = capture("x8");
        Capture<Integer> x9 = capture("x9");
        Capture<Integer> x10 = capture("x10");
        Capture<Integer> x11 = capture("x11");
        Capture<Integer> x12 = capture("x12");
        Capture<Integer> x13 = capture("x13");
        Capture<Integer> x14 = capture("x14");
        Capture<Integer> x15 = capture("x15");
        String result = match(new Tuple15<>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15),
                Tuple(x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15),
                () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v()+x7.v()+x8.v()+x9.v()+x10.v()+x11.v()+x12.v()
                        +x13.v()+x14.v()+x15.v());
        assertEquals("123456789101112131415", result);
    }

    @Test
    void tuple16() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        Capture<Integer> x7 = capture("x7");
        Capture<Integer> x8 = capture("x8");
        Capture<Integer> x9 = capture("x9");
        Capture<Integer> x10 = capture("x10");
        Capture<Integer> x11 = capture("x11");
        Capture<Integer> x12 = capture("x12");
        Capture<Integer> x13 = capture("x13");
        Capture<Integer> x14 = capture("x14");
        Capture<Integer> x15 = capture("x15");
        Capture<Integer> x16 = capture("x16");
        String result = match(new Tuple16<>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16),
                Tuple(x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16),
                () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v()+x7.v()+x8.v()+x9.v()+x10.v()+x11.v()+x12.v()
                        +x13.v()+x14.v()+x15.v()+x16.v());
        assertEquals("12345678910111213141516", result);
    }

    @Test
    void tuple17() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        Capture<Integer> x7 = capture("x7");
        Capture<Integer> x8 = capture("x8");
        Capture<Integer> x9 = capture("x9");
        Capture<Integer> x10 = capture("x10");
        Capture<Integer> x11 = capture("x11");
        Capture<Integer> x12 = capture("x12");
        Capture<Integer> x13 = capture("x13");
        Capture<Integer> x14 = capture("x14");
        Capture<Integer> x15 = capture("x15");
        Capture<Integer> x16 = capture("x16");
        Capture<Integer> x17 = capture("x17");
        String result = match(new Tuple17<>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17),
                Tuple(x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16,x17),
                () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v()+x7.v()+x8.v()+x9.v()+x10.v()+x11.v()+x12.v()
                        +x13.v()+x14.v()+x15.v()+x16.v()+x17.v());
        assertEquals("1234567891011121314151617", result);
    }

    @Test
    void tuple18() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        Capture<Integer> x7 = capture("x7");
        Capture<Integer> x8 = capture("x8");
        Capture<Integer> x9 = capture("x9");
        Capture<Integer> x10 = capture("x10");
        Capture<Integer> x11 = capture("x11");
        Capture<Integer> x12 = capture("x12");
        Capture<Integer> x13 = capture("x13");
        Capture<Integer> x14 = capture("x14");
        Capture<Integer> x15 = capture("x15");
        Capture<Integer> x16 = capture("x16");
        Capture<Integer> x17 = capture("x17");
        Capture<Integer> x18 = capture("x18");
        String result = match(new Tuple18<>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18),
                Tuple(x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16,x17,x18),
                () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v()+x7.v()+x8.v()+x9.v()+x10.v()+x11.v()+x12.v()
                        +x13.v()+x14.v()+x15.v()+x16.v()+x17.v()+x18.v());
        assertEquals("123456789101112131415161718", result);
    }

    @Test
    void tuple19() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        Capture<Integer> x7 = capture("x7");
        Capture<Integer> x8 = capture("x8");
        Capture<Integer> x9 = capture("x9");
        Capture<Integer> x10 = capture("x10");
        Capture<Integer> x11 = capture("x11");
        Capture<Integer> x12 = capture("x12");
        Capture<Integer> x13 = capture("x13");
        Capture<Integer> x14 = capture("x14");
        Capture<Integer> x15 = capture("x15");
        Capture<Integer> x16 = capture("x16");
        Capture<Integer> x17 = capture("x17");
        Capture<Integer> x18 = capture("x18");
        Capture<Integer> x19 = capture("x19");
        String result = match(new Tuple19<>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19),
                Tuple(x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16,x17,x18,x19),
                () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v()+x7.v()+x8.v()+x9.v()+x10.v()+x11.v()+x12.v()
                        +x13.v()+x14.v()+x15.v()+x16.v()+x17.v()+x18.v()+x19.v());
        assertEquals("12345678910111213141516171819", result);
    }

    @Test
    void tuple20() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        Capture<Integer> x7 = capture("x7");
        Capture<Integer> x8 = capture("x8");
        Capture<Integer> x9 = capture("x9");
        Capture<Integer> x10 = capture("x10");
        Capture<Integer> x11 = capture("x11");
        Capture<Integer> x12 = capture("x12");
        Capture<Integer> x13 = capture("x13");
        Capture<Integer> x14 = capture("x14");
        Capture<Integer> x15 = capture("x15");
        Capture<Integer> x16 = capture("x16");
        Capture<Integer> x17 = capture("x17");
        Capture<Integer> x18 = capture("x18");
        Capture<Integer> x19 = capture("x19");
        Capture<Integer> x20 = capture("x20");
        String result = match(new Tuple20<>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20),
                Tuple(x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16,x17,x18,x19,x20),
                () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v()+x7.v()+x8.v()+x9.v()+x10.v()+x11.v()+x12.v()
                        +x13.v()+x14.v()+x15.v()+x16.v()+x17.v()+x18.v()+x19.v()+x20.v());
        assertEquals("1234567891011121314151617181920", result);
    }

    @Test
    void tuple21() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        Capture<Integer> x7 = capture("x7");
        Capture<Integer> x8 = capture("x8");
        Capture<Integer> x9 = capture("x9");
        Capture<Integer> x10 = capture("x10");
        Capture<Integer> x11 = capture("x11");
        Capture<Integer> x12 = capture("x12");
        Capture<Integer> x13 = capture("x13");
        Capture<Integer> x14 = capture("x14");
        Capture<Integer> x15 = capture("x15");
        Capture<Integer> x16 = capture("x16");
        Capture<Integer> x17 = capture("x17");
        Capture<Integer> x18 = capture("x18");
        Capture<Integer> x19 = capture("x19");
        Capture<Integer> x20 = capture("x20");
        Capture<Integer> x21 = capture("x21");
        String result = match(new Tuple21<>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21),
                Tuple(x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16,x17,x18,x19,x20,x21),
                () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v()+x7.v()+x8.v()+x9.v()+x10.v()+x11.v()+x12.v()
                        +x13.v()+x14.v()+x15.v()+x16.v()+x17.v()+x18.v()+x19.v()+x20.v()+x21.v());
        assertEquals("123456789101112131415161718192021", result);
    }

    @Test
    void tuple22() throws MatchException {
        Capture<Integer> x1 = capture("x1");
        Capture<Integer> x2 = capture("x2");
        Capture<Integer> x3 = capture("x3");
        Capture<Integer> x4 = capture("x4");
        Capture<Integer> x5 = capture("x5");
        Capture<Integer> x6 = capture("x6");
        Capture<Integer> x7 = capture("x7");
        Capture<Integer> x8 = capture("x8");
        Capture<Integer> x9 = capture("x9");
        Capture<Integer> x10 = capture("x10");
        Capture<Integer> x11 = capture("x11");
        Capture<Integer> x12 = capture("x12");
        Capture<Integer> x13 = capture("x13");
        Capture<Integer> x14 = capture("x14");
        Capture<Integer> x15 = capture("x15");
        Capture<Integer> x16 = capture("x16");
        Capture<Integer> x17 = capture("x17");
        Capture<Integer> x18 = capture("x18");
        Capture<Integer> x19 = capture("x19");
        Capture<Integer> x20 = capture("x20");
        Capture<Integer> x21 = capture("x21");
        Capture<Integer> x22 = capture("x22");
        String result = match(new Tuple22<>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22),
                Tuple(x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16,x17,x18,x19,x20,x21,x22),
                () -> ""+x1.v()+x2.v()+x3.v()+x4.v()+x5.v()+x6.v()+x7.v()+x8.v()+x9.v()+x10.v()+x11.v()+x12.v()
                        +x13.v()+x14.v()+x15.v()+x16.v()+x17.v()+x18.v()+x19.v()+x20.v()+x21.v()+x22.v());
        assertEquals("12345678910111213141516171819202122", result);
    }
}
