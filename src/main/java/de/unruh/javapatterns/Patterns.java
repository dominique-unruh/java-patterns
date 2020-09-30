package de.unruh.javapatterns;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.StringJoiner;
import java.util.function.Predicate;

// DOCUMENT, mention (somewhere): can access captures already in match, can fail match in action
// TODO split into several classes, and one to inherit all importable functions
public class Patterns {
    public static <T, X, E extends Throwable> Case <T, X, E> 
    withCase(Pattern<? super T> pattern, MatchAction<? extends X, E> action) {
        return new Case<>(pattern, action);
    }

    @SafeVarargs
    public static <T, X, E extends Throwable> X match(T value, Case<T, X, E>... cases) throws E, MatchException {
        MatchManager mgr = new MatchManager();
        for (Case<T, X, E> cas : cases) {
            PatternResult<X> result = cas.apply(mgr, value);
            if (result.nonEmpty())
                return result.get();
        }
        throw new MatchException(value);
    }

    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1) throws E, MatchException {
        return match(value, withCase(pattern1, action1));
    }

    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2));
    }

    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3));
    }

    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4));
    }


    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5));
    }


    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6));
    }

    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6,
                                 Pattern<? super T> pattern7, MatchAction<? extends X, E> action7) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7));
    }

    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6,
                                 Pattern<? super T> pattern7, MatchAction<? extends X, E> action7,
                                 Pattern<? super T> pattern8, MatchAction<? extends X, E> action8) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8));
    }


    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6,
                                 Pattern<? super T> pattern7, MatchAction<? extends X, E> action7,
                                 Pattern<? super T> pattern8, MatchAction<? extends X, E> action8,
                                 Pattern<? super T> pattern9, MatchAction<? extends X, E> action9) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9));
    }


    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6,
                                 Pattern<? super T> pattern7, MatchAction<? extends X, E> action7,
                                 Pattern<? super T> pattern8, MatchAction<? extends X, E> action8,
                                 Pattern<? super T> pattern9, MatchAction<? extends X, E> action9,
                                 Pattern<? super T> pattern10, MatchAction<? extends X, E> action10) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10));
    }

    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6,
                                 Pattern<? super T> pattern7, MatchAction<? extends X, E> action7,
                                 Pattern<? super T> pattern8, MatchAction<? extends X, E> action8,
                                 Pattern<? super T> pattern9, MatchAction<? extends X, E> action9,
                                 Pattern<? super T> pattern10, MatchAction<? extends X, E> action10,
                                 Pattern<? super T> pattern11, MatchAction<? extends X, E> action11) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11));
    }

    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6,
                                 Pattern<? super T> pattern7, MatchAction<? extends X, E> action7,
                                 Pattern<? super T> pattern8, MatchAction<? extends X, E> action8,
                                 Pattern<? super T> pattern9, MatchAction<? extends X, E> action9,
                                 Pattern<? super T> pattern10, MatchAction<? extends X, E> action10,
                                 Pattern<? super T> pattern11, MatchAction<? extends X, E> action11,
                                 Pattern<? super T> pattern12, MatchAction<? extends X, E> action12) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12));
    }

    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6,
                                 Pattern<? super T> pattern7, MatchAction<? extends X, E> action7,
                                 Pattern<? super T> pattern8, MatchAction<? extends X, E> action8,
                                 Pattern<? super T> pattern9, MatchAction<? extends X, E> action9,
                                 Pattern<? super T> pattern10, MatchAction<? extends X, E> action10,
                                 Pattern<? super T> pattern11, MatchAction<? extends X, E> action11,
                                 Pattern<? super T> pattern12, MatchAction<? extends X, E> action12,
                                 Pattern<? super T> pattern13, MatchAction<? extends X, E> action13) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13));
    }


    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6,
                                 Pattern<? super T> pattern7, MatchAction<? extends X, E> action7,
                                 Pattern<? super T> pattern8, MatchAction<? extends X, E> action8,
                                 Pattern<? super T> pattern9, MatchAction<? extends X, E> action9,
                                 Pattern<? super T> pattern10, MatchAction<? extends X, E> action10,
                                 Pattern<? super T> pattern11, MatchAction<? extends X, E> action11,
                                 Pattern<? super T> pattern12, MatchAction<? extends X, E> action12,
                                 Pattern<? super T> pattern13, MatchAction<? extends X, E> action13,
                                 Pattern<? super T> pattern14, MatchAction<? extends X, E> action14) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14));
    }


    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6,
                                 Pattern<? super T> pattern7, MatchAction<? extends X, E> action7,
                                 Pattern<? super T> pattern8, MatchAction<? extends X, E> action8,
                                 Pattern<? super T> pattern9, MatchAction<? extends X, E> action9,
                                 Pattern<? super T> pattern10, MatchAction<? extends X, E> action10,
                                 Pattern<? super T> pattern11, MatchAction<? extends X, E> action11,
                                 Pattern<? super T> pattern12, MatchAction<? extends X, E> action12,
                                 Pattern<? super T> pattern13, MatchAction<? extends X, E> action13,
                                 Pattern<? super T> pattern14, MatchAction<? extends X, E> action14,
                                 Pattern<? super T> pattern15, MatchAction<? extends X, E> action15) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15));
    }

    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6,
                                 Pattern<? super T> pattern7, MatchAction<? extends X, E> action7,
                                 Pattern<? super T> pattern8, MatchAction<? extends X, E> action8,
                                 Pattern<? super T> pattern9, MatchAction<? extends X, E> action9,
                                 Pattern<? super T> pattern10, MatchAction<? extends X, E> action10,
                                 Pattern<? super T> pattern11, MatchAction<? extends X, E> action11,
                                 Pattern<? super T> pattern12, MatchAction<? extends X, E> action12,
                                 Pattern<? super T> pattern13, MatchAction<? extends X, E> action13,
                                 Pattern<? super T> pattern14, MatchAction<? extends X, E> action14,
                                 Pattern<? super T> pattern15, MatchAction<? extends X, E> action15,
                                 Pattern<? super T> pattern16, MatchAction<? extends X, E> action16) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16));
    }

    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6,
                                 Pattern<? super T> pattern7, MatchAction<? extends X, E> action7,
                                 Pattern<? super T> pattern8, MatchAction<? extends X, E> action8,
                                 Pattern<? super T> pattern9, MatchAction<? extends X, E> action9,
                                 Pattern<? super T> pattern10, MatchAction<? extends X, E> action10,
                                 Pattern<? super T> pattern11, MatchAction<? extends X, E> action11,
                                 Pattern<? super T> pattern12, MatchAction<? extends X, E> action12,
                                 Pattern<? super T> pattern13, MatchAction<? extends X, E> action13,
                                 Pattern<? super T> pattern14, MatchAction<? extends X, E> action14,
                                 Pattern<? super T> pattern15, MatchAction<? extends X, E> action15,
                                 Pattern<? super T> pattern16, MatchAction<? extends X, E> action16,
                                 Pattern<? super T> pattern17, MatchAction<? extends X, E> action17) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17));
    }

    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6,
                                 Pattern<? super T> pattern7, MatchAction<? extends X, E> action7,
                                 Pattern<? super T> pattern8, MatchAction<? extends X, E> action8,
                                 Pattern<? super T> pattern9, MatchAction<? extends X, E> action9,
                                 Pattern<? super T> pattern10, MatchAction<? extends X, E> action10,
                                 Pattern<? super T> pattern11, MatchAction<? extends X, E> action11,
                                 Pattern<? super T> pattern12, MatchAction<? extends X, E> action12,
                                 Pattern<? super T> pattern13, MatchAction<? extends X, E> action13,
                                 Pattern<? super T> pattern14, MatchAction<? extends X, E> action14,
                                 Pattern<? super T> pattern15, MatchAction<? extends X, E> action15,
                                 Pattern<? super T> pattern16, MatchAction<? extends X, E> action16,
                                 Pattern<? super T> pattern17, MatchAction<? extends X, E> action17,
                                 Pattern<? super T> pattern18, MatchAction<? extends X, E> action18) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18));
    }


    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6,
                                 Pattern<? super T> pattern7, MatchAction<? extends X, E> action7,
                                 Pattern<? super T> pattern8, MatchAction<? extends X, E> action8,
                                 Pattern<? super T> pattern9, MatchAction<? extends X, E> action9,
                                 Pattern<? super T> pattern10, MatchAction<? extends X, E> action10,
                                 Pattern<? super T> pattern11, MatchAction<? extends X, E> action11,
                                 Pattern<? super T> pattern12, MatchAction<? extends X, E> action12,
                                 Pattern<? super T> pattern13, MatchAction<? extends X, E> action13,
                                 Pattern<? super T> pattern14, MatchAction<? extends X, E> action14,
                                 Pattern<? super T> pattern15, MatchAction<? extends X, E> action15,
                                 Pattern<? super T> pattern16, MatchAction<? extends X, E> action16,
                                 Pattern<? super T> pattern17, MatchAction<? extends X, E> action17,
                                 Pattern<? super T> pattern18, MatchAction<? extends X, E> action18,
                                 Pattern<? super T> pattern19, MatchAction<? extends X, E> action19) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19));
    }

    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6,
                                 Pattern<? super T> pattern7, MatchAction<? extends X, E> action7,
                                 Pattern<? super T> pattern8, MatchAction<? extends X, E> action8,
                                 Pattern<? super T> pattern9, MatchAction<? extends X, E> action9,
                                 Pattern<? super T> pattern10, MatchAction<? extends X, E> action10,
                                 Pattern<? super T> pattern11, MatchAction<? extends X, E> action11,
                                 Pattern<? super T> pattern12, MatchAction<? extends X, E> action12,
                                 Pattern<? super T> pattern13, MatchAction<? extends X, E> action13,
                                 Pattern<? super T> pattern14, MatchAction<? extends X, E> action14,
                                 Pattern<? super T> pattern15, MatchAction<? extends X, E> action15,
                                 Pattern<? super T> pattern16, MatchAction<? extends X, E> action16,
                                 Pattern<? super T> pattern17, MatchAction<? extends X, E> action17,
                                 Pattern<? super T> pattern18, MatchAction<? extends X, E> action18,
                                 Pattern<? super T> pattern19, MatchAction<? extends X, E> action19,
                                 Pattern<? super T> pattern20, MatchAction<? extends X, E> action20) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19), withCase(pattern20, action20));
    }


    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6,
                                 Pattern<? super T> pattern7, MatchAction<? extends X, E> action7,
                                 Pattern<? super T> pattern8, MatchAction<? extends X, E> action8,
                                 Pattern<? super T> pattern9, MatchAction<? extends X, E> action9,
                                 Pattern<? super T> pattern10, MatchAction<? extends X, E> action10,
                                 Pattern<? super T> pattern11, MatchAction<? extends X, E> action11,
                                 Pattern<? super T> pattern12, MatchAction<? extends X, E> action12,
                                 Pattern<? super T> pattern13, MatchAction<? extends X, E> action13,
                                 Pattern<? super T> pattern14, MatchAction<? extends X, E> action14,
                                 Pattern<? super T> pattern15, MatchAction<? extends X, E> action15,
                                 Pattern<? super T> pattern16, MatchAction<? extends X, E> action16,
                                 Pattern<? super T> pattern17, MatchAction<? extends X, E> action17,
                                 Pattern<? super T> pattern18, MatchAction<? extends X, E> action18,
                                 Pattern<? super T> pattern19, MatchAction<? extends X, E> action19,
                                 Pattern<? super T> pattern20, MatchAction<? extends X, E> action20,
                                 Pattern<? super T> pattern21, MatchAction<? extends X, E> action21) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19), withCase(pattern20, action20), withCase(pattern21, action21));
    }

    public static <T, X, E extends Throwable> X match(T value, Pattern<? super T> pattern1, MatchAction<? extends X, E> action1,
                                 Pattern<? super T> pattern2, MatchAction<? extends X, E> action2,
                                 Pattern<? super T> pattern3, MatchAction<? extends X, E> action3,
                                 Pattern<? super T> pattern4, MatchAction<? extends X, E> action4,
                                 Pattern<? super T> pattern5, MatchAction<? extends X, E> action5,
                                 Pattern<? super T> pattern6, MatchAction<? extends X, E> action6,
                                 Pattern<? super T> pattern7, MatchAction<? extends X, E> action7,
                                 Pattern<? super T> pattern8, MatchAction<? extends X, E> action8,
                                 Pattern<? super T> pattern9, MatchAction<? extends X, E> action9,
                                 Pattern<? super T> pattern10, MatchAction<? extends X, E> action10,
                                 Pattern<? super T> pattern11, MatchAction<? extends X, E> action11,
                                 Pattern<? super T> pattern12, MatchAction<? extends X, E> action12,
                                 Pattern<? super T> pattern13, MatchAction<? extends X, E> action13,
                                 Pattern<? super T> pattern14, MatchAction<? extends X, E> action14,
                                 Pattern<? super T> pattern15, MatchAction<? extends X, E> action15,
                                 Pattern<? super T> pattern16, MatchAction<? extends X, E> action16,
                                 Pattern<? super T> pattern17, MatchAction<? extends X, E> action17,
                                 Pattern<? super T> pattern18, MatchAction<? extends X, E> action18,
                                 Pattern<? super T> pattern19, MatchAction<? extends X, E> action19,
                                 Pattern<? super T> pattern20, MatchAction<? extends X, E> action20,
                                 Pattern<? super T> pattern21, MatchAction<? extends X, E> action21,
                                 Pattern<? super T> pattern22, MatchAction<? extends X, E> action22) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19), withCase(pattern20, action20), withCase(pattern21, action21),
                withCase(pattern22, action22));
    }


    public static <T> Pattern<T> Is(T expected) {
        return new Pattern<>() {
            @Override
            public void apply(MatchManager mgr, T value) throws PatternMatchReject {
                if (!expected.equals(value)) reject();
            }

            @Override
            public String toString() {
                return "=" + expected;
            }
        };
    }

    public static <T> Pattern<T> Is(PatternSupplier<T> expected) {
        return new Pattern<>() {
            @Override
            public void apply(MatchManager mgr, T value) throws PatternMatchReject {
                if (!expected.get().equals(value)) reject();
            }

            @Override
            public String toString() {
                return "=" + expected;
            }
        };
    }

    public static <T> Pattern<T> Is(Capture<T> expected) {
        return Is(expected::v);
    }

    public static final Pattern<Object> Any = new Pattern<>() {
        @Override
        public void apply(MatchManager mgr, Object value) {
        }

        @Override
        public String toString() {
            return "_";
        }
    };

    public static final Pattern<Object> Null = new Pattern<>() {
        @Override
        public void apply(MatchManager mgr, Object value) throws PatternMatchReject {
            if (value != null) reject();
        }

        @Override
        public String toString() {
            return "null";
        }
    };

    public static <T> Pattern<T> NotNull(Pattern<? super T> pattern) {
        return new Pattern<>() {
            @Override
            public void apply(MatchManager mgr, T value) throws PatternMatchReject {
                if (value == null) reject();
                pattern.apply(mgr, value);
            }

            @Override
            public String toString() {
                return "null";
            }
        };
    }

    @SafeVarargs
    public static <T> Pattern<T> And(Pattern<? super T>... patterns) {
        return new Pattern<>() {
            @Override
            public void apply(MatchManager mgr, T value) throws PatternMatchReject {
                for (Pattern<? super T> pattern : patterns)
                    pattern.apply(mgr, value);
            }

            @Override
            public String toString() {
                StringJoiner joiner = new StringJoiner(", ");
                for (Pattern<?> pattern : patterns)
                    joiner.add(pattern.toString());
                return "And(" + joiner + ")";
            }
        };
    }

    @SafeVarargs
    public static <T> Pattern<T> Or(Pattern<? super T>... patterns) {
        return new Pattern<T>() {
            @Override
            public void apply(MatchManager mgr, T value) throws PatternMatchReject {
                if (patterns.length == 0) reject();
                for (int i=0; i<patterns.length-1; i++) {
                    Pattern<? super T> pattern = patterns[i];
                    if (mgr.excursion(() -> pattern.apply(mgr, value))) return;
                }
                patterns[patterns.length-1].apply(mgr, value);
            }

            @Override
            public String toString() {
                StringJoiner joiner = new StringJoiner(", ");
                for (Pattern<?> pattern : patterns)
                    joiner.add(pattern.toString());
                return "Or(" + joiner + ")";
            }
        };
    }

    public static <U> Pattern<Object> Instance(Class<U> clazz, Pattern<? super U> pattern) {
        return new Pattern<>() {
            @Override
            public void apply(MatchManager mgr, Object value) throws PatternMatchReject {
                U castValue = null;
                try {
                    // TODO: use clazz.isInstance to avoid creation of ClassCastException (stack trace construction)
                    castValue = clazz.cast(value);
                } catch (ClassCastException e) {
                    reject();
                }
                pattern.apply(mgr,castValue);
            }

            @Override
            public String toString() {
                return "Instance("+clazz.getSimpleName()+","+pattern+")";
            }
        };
    }

    public abstract static class Instance<U> extends Pattern<U> {
        private final Pattern<Object> instancePattern;
        private final Pattern<? super U> pattern;
        private final Type typeU;
        @SuppressWarnings("unchecked")
        public Instance(Pattern<? super U> pattern) {
            // Based on https://stackoverflow.com/a/64138964/2646248, https://www.baeldung.com/java-super-type-tokens
            if (getClass().getSuperclass() != Instance.class)
                throw new InvalidPatternMatch("A subclass of a subclass of "+Instance.class+" was created. This is not the intended use.");
            Type superclass = getClass().getGenericSuperclass();
            typeU = ((ParameterizedType) superclass).getActualTypeArguments()[0];
            Class<U> clazz;
            if (typeU instanceof ParameterizedType)
                clazz = (Class<U>) ((ParameterizedType) typeU).getRawType();
            else if (typeU instanceof Class)
                clazz = (Class<U>) typeU;
            else
                throw new InvalidPatternMatch("Type parameter of "+Instance.class.getName()+" must be a class, not "+typeU+" "+typeU.getClass());
            instancePattern = Instance(clazz, pattern);
            this.pattern = pattern;
        };

        @Override
        public void apply(MatchManager mgr, U value) throws PatternMatchReject {
            instancePattern.apply(mgr, value);
        }

        @Override
        public String toString() {
            return "("+pattern+" : "+typeU+")";
        }
    }

    public static <T> Pattern<T> Pred(Predicate<? super T> predicate) {
        return new Pattern<T>() {
            @Override
            public void apply(MatchManager mgr, T value) throws PatternMatchReject {
                if (!predicate.test(value)) reject();
            }

            @Override
            public String toString() {
                return "Pred(...)";
            }
        };
    }

    public static <T> Pattern<T> NoMatch(Pattern<? super T> pattern) {
        return new Pattern<T>() {
            @Override
            public void apply(MatchManager mgr, T value) throws PatternMatchReject {
                boolean matched = mgr.excursion(() -> pattern.apply(mgr, value));
                if (matched) reject();
            }

            @Override
            public String toString() {
                return "NoMatch("+pattern+")";
            }
        };
    }

    @SafeVarargs
    public static <T> Pattern<T[]> Array(Pattern<? super T> ... patterns) {
        return new Pattern<T[]>() {
            @Override
            public void apply(MatchManager mgr, T[] value) throws PatternMatchReject {
                if (value == null) reject();
                if (value.length != patterns.length) reject();
                for (int i=0; i<patterns.length; i++)
                    patterns[i].apply(mgr, value[i]);
            }

            @Override
            public String toString() {
                StringJoiner joiner = new StringJoiner(", ");
                for (Pattern<?> pattern : patterns)
                    joiner.add(pattern.toString());
                return "Array(" + joiner + ")";
            }
        };
    }
}