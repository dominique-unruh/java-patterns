package de.unruh.javapatterns;

import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

// DOCUMENT, mention (somewhere): can access captures already in match, can fail match in action
// TODO: Can we handle exceptions better? (Avoid "throws Exception" clause)
// TODO split into several classes, and one to inherit all importable functions
public class Patterns {
    public static <T, X> Case<T, X> withCase(Pattern<? super T> pattern, Callable<? extends X> action) {
        return new Case<>(pattern, action);
    }

    @SafeVarargs
    public static <T, X> X match(T value, Case<T, X>... cases) throws Exception {
        MatchManager mgr = new MatchManager();
        for (Case<T, X> cas : cases) {
            PatternResult<X> result = cas.apply(mgr, value);
            if (result.nonEmpty())
                return result.get();
        }
        throw new MatchException(value);
    }

    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1) throws Exception {
        return match(value, withCase(pattern1, action1));
    }

    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2));
    }

    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3));
    }

    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4));
    }


    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5));
    }


    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6));
    }

    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6,
                                 Pattern<? super T> pattern7, Callable<? extends X> action7) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7));
    }

    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6,
                                 Pattern<? super T> pattern7, Callable<? extends X> action7,
                                 Pattern<? super T> pattern8, Callable<? extends X> action8) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8));
    }


    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6,
                                 Pattern<? super T> pattern7, Callable<? extends X> action7,
                                 Pattern<? super T> pattern8, Callable<? extends X> action8,
                                 Pattern<? super T> pattern9, Callable<? extends X> action9) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9));
    }


    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6,
                                 Pattern<? super T> pattern7, Callable<? extends X> action7,
                                 Pattern<? super T> pattern8, Callable<? extends X> action8,
                                 Pattern<? super T> pattern9, Callable<? extends X> action9,
                                 Pattern<? super T> pattern10, Callable<? extends X> action10) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10));
    }

    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6,
                                 Pattern<? super T> pattern7, Callable<? extends X> action7,
                                 Pattern<? super T> pattern8, Callable<? extends X> action8,
                                 Pattern<? super T> pattern9, Callable<? extends X> action9,
                                 Pattern<? super T> pattern10, Callable<? extends X> action10,
                                 Pattern<? super T> pattern11, Callable<? extends X> action11) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11));
    }

    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6,
                                 Pattern<? super T> pattern7, Callable<? extends X> action7,
                                 Pattern<? super T> pattern8, Callable<? extends X> action8,
                                 Pattern<? super T> pattern9, Callable<? extends X> action9,
                                 Pattern<? super T> pattern10, Callable<? extends X> action10,
                                 Pattern<? super T> pattern11, Callable<? extends X> action11,
                                 Pattern<? super T> pattern12, Callable<? extends X> action12) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12));
    }

    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6,
                                 Pattern<? super T> pattern7, Callable<? extends X> action7,
                                 Pattern<? super T> pattern8, Callable<? extends X> action8,
                                 Pattern<? super T> pattern9, Callable<? extends X> action9,
                                 Pattern<? super T> pattern10, Callable<? extends X> action10,
                                 Pattern<? super T> pattern11, Callable<? extends X> action11,
                                 Pattern<? super T> pattern12, Callable<? extends X> action12,
                                 Pattern<? super T> pattern13, Callable<? extends X> action13) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13));
    }


    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6,
                                 Pattern<? super T> pattern7, Callable<? extends X> action7,
                                 Pattern<? super T> pattern8, Callable<? extends X> action8,
                                 Pattern<? super T> pattern9, Callable<? extends X> action9,
                                 Pattern<? super T> pattern10, Callable<? extends X> action10,
                                 Pattern<? super T> pattern11, Callable<? extends X> action11,
                                 Pattern<? super T> pattern12, Callable<? extends X> action12,
                                 Pattern<? super T> pattern13, Callable<? extends X> action13,
                                 Pattern<? super T> pattern14, Callable<? extends X> action14) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14));
    }


    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6,
                                 Pattern<? super T> pattern7, Callable<? extends X> action7,
                                 Pattern<? super T> pattern8, Callable<? extends X> action8,
                                 Pattern<? super T> pattern9, Callable<? extends X> action9,
                                 Pattern<? super T> pattern10, Callable<? extends X> action10,
                                 Pattern<? super T> pattern11, Callable<? extends X> action11,
                                 Pattern<? super T> pattern12, Callable<? extends X> action12,
                                 Pattern<? super T> pattern13, Callable<? extends X> action13,
                                 Pattern<? super T> pattern14, Callable<? extends X> action14,
                                 Pattern<? super T> pattern15, Callable<? extends X> action15) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15));
    }

    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6,
                                 Pattern<? super T> pattern7, Callable<? extends X> action7,
                                 Pattern<? super T> pattern8, Callable<? extends X> action8,
                                 Pattern<? super T> pattern9, Callable<? extends X> action9,
                                 Pattern<? super T> pattern10, Callable<? extends X> action10,
                                 Pattern<? super T> pattern11, Callable<? extends X> action11,
                                 Pattern<? super T> pattern12, Callable<? extends X> action12,
                                 Pattern<? super T> pattern13, Callable<? extends X> action13,
                                 Pattern<? super T> pattern14, Callable<? extends X> action14,
                                 Pattern<? super T> pattern15, Callable<? extends X> action15,
                                 Pattern<? super T> pattern16, Callable<? extends X> action16) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16));
    }

    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6,
                                 Pattern<? super T> pattern7, Callable<? extends X> action7,
                                 Pattern<? super T> pattern8, Callable<? extends X> action8,
                                 Pattern<? super T> pattern9, Callable<? extends X> action9,
                                 Pattern<? super T> pattern10, Callable<? extends X> action10,
                                 Pattern<? super T> pattern11, Callable<? extends X> action11,
                                 Pattern<? super T> pattern12, Callable<? extends X> action12,
                                 Pattern<? super T> pattern13, Callable<? extends X> action13,
                                 Pattern<? super T> pattern14, Callable<? extends X> action14,
                                 Pattern<? super T> pattern15, Callable<? extends X> action15,
                                 Pattern<? super T> pattern16, Callable<? extends X> action16,
                                 Pattern<? super T> pattern17, Callable<? extends X> action17) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17));
    }

    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6,
                                 Pattern<? super T> pattern7, Callable<? extends X> action7,
                                 Pattern<? super T> pattern8, Callable<? extends X> action8,
                                 Pattern<? super T> pattern9, Callable<? extends X> action9,
                                 Pattern<? super T> pattern10, Callable<? extends X> action10,
                                 Pattern<? super T> pattern11, Callable<? extends X> action11,
                                 Pattern<? super T> pattern12, Callable<? extends X> action12,
                                 Pattern<? super T> pattern13, Callable<? extends X> action13,
                                 Pattern<? super T> pattern14, Callable<? extends X> action14,
                                 Pattern<? super T> pattern15, Callable<? extends X> action15,
                                 Pattern<? super T> pattern16, Callable<? extends X> action16,
                                 Pattern<? super T> pattern17, Callable<? extends X> action17,
                                 Pattern<? super T> pattern18, Callable<? extends X> action18) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18));
    }


    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6,
                                 Pattern<? super T> pattern7, Callable<? extends X> action7,
                                 Pattern<? super T> pattern8, Callable<? extends X> action8,
                                 Pattern<? super T> pattern9, Callable<? extends X> action9,
                                 Pattern<? super T> pattern10, Callable<? extends X> action10,
                                 Pattern<? super T> pattern11, Callable<? extends X> action11,
                                 Pattern<? super T> pattern12, Callable<? extends X> action12,
                                 Pattern<? super T> pattern13, Callable<? extends X> action13,
                                 Pattern<? super T> pattern14, Callable<? extends X> action14,
                                 Pattern<? super T> pattern15, Callable<? extends X> action15,
                                 Pattern<? super T> pattern16, Callable<? extends X> action16,
                                 Pattern<? super T> pattern17, Callable<? extends X> action17,
                                 Pattern<? super T> pattern18, Callable<? extends X> action18,
                                 Pattern<? super T> pattern19, Callable<? extends X> action19) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19));
    }

    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6,
                                 Pattern<? super T> pattern7, Callable<? extends X> action7,
                                 Pattern<? super T> pattern8, Callable<? extends X> action8,
                                 Pattern<? super T> pattern9, Callable<? extends X> action9,
                                 Pattern<? super T> pattern10, Callable<? extends X> action10,
                                 Pattern<? super T> pattern11, Callable<? extends X> action11,
                                 Pattern<? super T> pattern12, Callable<? extends X> action12,
                                 Pattern<? super T> pattern13, Callable<? extends X> action13,
                                 Pattern<? super T> pattern14, Callable<? extends X> action14,
                                 Pattern<? super T> pattern15, Callable<? extends X> action15,
                                 Pattern<? super T> pattern16, Callable<? extends X> action16,
                                 Pattern<? super T> pattern17, Callable<? extends X> action17,
                                 Pattern<? super T> pattern18, Callable<? extends X> action18,
                                 Pattern<? super T> pattern19, Callable<? extends X> action19,
                                 Pattern<? super T> pattern20, Callable<? extends X> action20) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19), withCase(pattern20, action20));
    }


    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6,
                                 Pattern<? super T> pattern7, Callable<? extends X> action7,
                                 Pattern<? super T> pattern8, Callable<? extends X> action8,
                                 Pattern<? super T> pattern9, Callable<? extends X> action9,
                                 Pattern<? super T> pattern10, Callable<? extends X> action10,
                                 Pattern<? super T> pattern11, Callable<? extends X> action11,
                                 Pattern<? super T> pattern12, Callable<? extends X> action12,
                                 Pattern<? super T> pattern13, Callable<? extends X> action13,
                                 Pattern<? super T> pattern14, Callable<? extends X> action14,
                                 Pattern<? super T> pattern15, Callable<? extends X> action15,
                                 Pattern<? super T> pattern16, Callable<? extends X> action16,
                                 Pattern<? super T> pattern17, Callable<? extends X> action17,
                                 Pattern<? super T> pattern18, Callable<? extends X> action18,
                                 Pattern<? super T> pattern19, Callable<? extends X> action19,
                                 Pattern<? super T> pattern20, Callable<? extends X> action20,
                                 Pattern<? super T> pattern21, Callable<? extends X> action21) throws Exception {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19), withCase(pattern20, action20), withCase(pattern21, action21));
    }

    public static <T, X> X match(T value, Pattern<? super T> pattern1, Callable<? extends X> action1,
                                 Pattern<? super T> pattern2, Callable<? extends X> action2,
                                 Pattern<? super T> pattern3, Callable<? extends X> action3,
                                 Pattern<? super T> pattern4, Callable<? extends X> action4,
                                 Pattern<? super T> pattern5, Callable<? extends X> action5,
                                 Pattern<? super T> pattern6, Callable<? extends X> action6,
                                 Pattern<? super T> pattern7, Callable<? extends X> action7,
                                 Pattern<? super T> pattern8, Callable<? extends X> action8,
                                 Pattern<? super T> pattern9, Callable<? extends X> action9,
                                 Pattern<? super T> pattern10, Callable<? extends X> action10,
                                 Pattern<? super T> pattern11, Callable<? extends X> action11,
                                 Pattern<? super T> pattern12, Callable<? extends X> action12,
                                 Pattern<? super T> pattern13, Callable<? extends X> action13,
                                 Pattern<? super T> pattern14, Callable<? extends X> action14,
                                 Pattern<? super T> pattern15, Callable<? extends X> action15,
                                 Pattern<? super T> pattern16, Callable<? extends X> action16,
                                 Pattern<? super T> pattern17, Callable<? extends X> action17,
                                 Pattern<? super T> pattern18, Callable<? extends X> action18,
                                 Pattern<? super T> pattern19, Callable<? extends X> action19,
                                 Pattern<? super T> pattern20, Callable<? extends X> action20,
                                 Pattern<? super T> pattern21, Callable<? extends X> action21,
                                 Pattern<? super T> pattern22, Callable<? extends X> action22) throws Exception {
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