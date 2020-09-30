package de.unruh.javapatterns;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Match {
    /** Make this class uninstantiable */
    @org.jetbrains.annotations.Contract(pure = true)
    private Match() {}

    @org.jetbrains.annotations.Contract(pure = true, value = "_, _ -> new")
    public static <In, Return, Exn extends Throwable> Case <In, Return, Exn>
    withCase(@NotNull Pattern<? super In> pattern, @NotNull MatchSupplier<? extends Return, Exn> action) {
        return new Case<>(pattern, action);
    }

    @org.jetbrains.annotations.Contract(pure = true, value = "_, _ -> new")
    public static <In, Exn extends Throwable> Case <In, Void, Exn>
    withCase(@NotNull Pattern<? super In> pattern, @NotNull MatchRunnable<Exn> action) {
        return new Case<>(pattern, () -> { action.run(); return null; } );
    }

    @SafeVarargs
    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Case<T, X, E>... cases) throws E, MatchException {
        MatchManager mgr = new MatchManager();
        for (Case<T, X, E> cas : cases) {
            PatternResult<X> result = cas.apply(mgr, value);
            if (result.nonEmpty())
                return result.get();
        }
        throw new MatchException(value);
    }

    public static <T, X, E extends Throwable> X match(@Nullable T value,
                                                      @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1) throws E, MatchException {
        return match(value, withCase(pattern1, action1));
    }

    public static <T, X, E extends Throwable> X match(@Nullable T value,
                                                      @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2));
    }

    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3));
    }

    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4));
    }


    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5));
    }


    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6));
    }

    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6,
                                                      @NotNull Pattern<? super T> pattern7, @NotNull MatchSupplier<? extends X, E> action7) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7));
    }

    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6,
                                                      @NotNull Pattern<? super T> pattern7, @NotNull MatchSupplier<? extends X, E> action7,
                                                      @NotNull Pattern<? super T> pattern8, @NotNull MatchSupplier<? extends X, E> action8) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8));
    }


    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6,
                                                      @NotNull Pattern<? super T> pattern7, @NotNull MatchSupplier<? extends X, E> action7,
                                                      @NotNull Pattern<? super T> pattern8, @NotNull MatchSupplier<? extends X, E> action8,
                                                      @NotNull Pattern<? super T> pattern9, @NotNull MatchSupplier<? extends X, E> action9) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9));
    }


    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6,
                                                      @NotNull Pattern<? super T> pattern7, @NotNull MatchSupplier<? extends X, E> action7,
                                                      @NotNull Pattern<? super T> pattern8, @NotNull MatchSupplier<? extends X, E> action8,
                                                      @NotNull Pattern<? super T> pattern9, @NotNull MatchSupplier<? extends X, E> action9,
                                                      @NotNull Pattern<? super T> pattern10, @NotNull MatchSupplier<? extends X, E> action10) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10));
    }

    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6,
                                                      @NotNull Pattern<? super T> pattern7, @NotNull MatchSupplier<? extends X, E> action7,
                                                      @NotNull Pattern<? super T> pattern8, @NotNull MatchSupplier<? extends X, E> action8,
                                                      @NotNull Pattern<? super T> pattern9, @NotNull MatchSupplier<? extends X, E> action9,
                                                      @NotNull Pattern<? super T> pattern10, @NotNull MatchSupplier<? extends X, E> action10,
                                                      @NotNull Pattern<? super T> pattern11, @NotNull MatchSupplier<? extends X, E> action11) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11));
    }

    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6,
                                                      @NotNull Pattern<? super T> pattern7, @NotNull MatchSupplier<? extends X, E> action7,
                                                      @NotNull Pattern<? super T> pattern8, @NotNull MatchSupplier<? extends X, E> action8,
                                                      @NotNull Pattern<? super T> pattern9, @NotNull MatchSupplier<? extends X, E> action9,
                                                      @NotNull Pattern<? super T> pattern10, @NotNull MatchSupplier<? extends X, E> action10,
                                                      @NotNull Pattern<? super T> pattern11, @NotNull MatchSupplier<? extends X, E> action11,
                                                      @NotNull Pattern<? super T> pattern12, @NotNull MatchSupplier<? extends X, E> action12) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12));
    }

    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6,
                                                      @NotNull Pattern<? super T> pattern7, @NotNull MatchSupplier<? extends X, E> action7,
                                                      @NotNull Pattern<? super T> pattern8, @NotNull MatchSupplier<? extends X, E> action8,
                                                      @NotNull Pattern<? super T> pattern9, @NotNull MatchSupplier<? extends X, E> action9,
                                                      @NotNull Pattern<? super T> pattern10, @NotNull MatchSupplier<? extends X, E> action10,
                                                      @NotNull Pattern<? super T> pattern11, @NotNull MatchSupplier<? extends X, E> action11,
                                                      @NotNull Pattern<? super T> pattern12, @NotNull MatchSupplier<? extends X, E> action12,
                                                      @NotNull Pattern<? super T> pattern13, @NotNull MatchSupplier<? extends X, E> action13) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13));
    }


    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6,
                                                      @NotNull Pattern<? super T> pattern7, @NotNull MatchSupplier<? extends X, E> action7,
                                                      @NotNull Pattern<? super T> pattern8, @NotNull MatchSupplier<? extends X, E> action8,
                                                      @NotNull Pattern<? super T> pattern9, @NotNull MatchSupplier<? extends X, E> action9,
                                                      @NotNull Pattern<? super T> pattern10, @NotNull MatchSupplier<? extends X, E> action10,
                                                      @NotNull Pattern<? super T> pattern11, @NotNull MatchSupplier<? extends X, E> action11,
                                                      @NotNull Pattern<? super T> pattern12, @NotNull MatchSupplier<? extends X, E> action12,
                                                      @NotNull Pattern<? super T> pattern13, @NotNull MatchSupplier<? extends X, E> action13,
                                                      @NotNull Pattern<? super T> pattern14, @NotNull MatchSupplier<? extends X, E> action14) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14));
    }


    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6,
                                                      @NotNull Pattern<? super T> pattern7, @NotNull MatchSupplier<? extends X, E> action7,
                                                      @NotNull Pattern<? super T> pattern8, @NotNull MatchSupplier<? extends X, E> action8,
                                                      @NotNull Pattern<? super T> pattern9, @NotNull MatchSupplier<? extends X, E> action9,
                                                      @NotNull Pattern<? super T> pattern10, @NotNull MatchSupplier<? extends X, E> action10,
                                                      @NotNull Pattern<? super T> pattern11, @NotNull MatchSupplier<? extends X, E> action11,
                                                      @NotNull Pattern<? super T> pattern12, @NotNull MatchSupplier<? extends X, E> action12,
                                                      @NotNull Pattern<? super T> pattern13, @NotNull MatchSupplier<? extends X, E> action13,
                                                      @NotNull Pattern<? super T> pattern14, @NotNull MatchSupplier<? extends X, E> action14,
                                                      @NotNull Pattern<? super T> pattern15, @NotNull MatchSupplier<? extends X, E> action15) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15));
    }

    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6,
                                                      @NotNull Pattern<? super T> pattern7, @NotNull MatchSupplier<? extends X, E> action7,
                                                      @NotNull Pattern<? super T> pattern8, @NotNull MatchSupplier<? extends X, E> action8,
                                                      @NotNull Pattern<? super T> pattern9, @NotNull MatchSupplier<? extends X, E> action9,
                                                      @NotNull Pattern<? super T> pattern10, @NotNull MatchSupplier<? extends X, E> action10,
                                                      @NotNull Pattern<? super T> pattern11, @NotNull MatchSupplier<? extends X, E> action11,
                                                      @NotNull Pattern<? super T> pattern12, @NotNull MatchSupplier<? extends X, E> action12,
                                                      @NotNull Pattern<? super T> pattern13, @NotNull MatchSupplier<? extends X, E> action13,
                                                      @NotNull Pattern<? super T> pattern14, @NotNull MatchSupplier<? extends X, E> action14,
                                                      @NotNull Pattern<? super T> pattern15, @NotNull MatchSupplier<? extends X, E> action15,
                                                      @NotNull Pattern<? super T> pattern16, @NotNull MatchSupplier<? extends X, E> action16) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16));
    }

    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6,
                                                      @NotNull Pattern<? super T> pattern7, @NotNull MatchSupplier<? extends X, E> action7,
                                                      @NotNull Pattern<? super T> pattern8, @NotNull MatchSupplier<? extends X, E> action8,
                                                      @NotNull Pattern<? super T> pattern9, @NotNull MatchSupplier<? extends X, E> action9,
                                                      @NotNull Pattern<? super T> pattern10, @NotNull MatchSupplier<? extends X, E> action10,
                                                      @NotNull Pattern<? super T> pattern11, @NotNull MatchSupplier<? extends X, E> action11,
                                                      @NotNull Pattern<? super T> pattern12, @NotNull MatchSupplier<? extends X, E> action12,
                                                      @NotNull Pattern<? super T> pattern13, @NotNull MatchSupplier<? extends X, E> action13,
                                                      @NotNull Pattern<? super T> pattern14, @NotNull MatchSupplier<? extends X, E> action14,
                                                      @NotNull Pattern<? super T> pattern15, @NotNull MatchSupplier<? extends X, E> action15,
                                                      @NotNull Pattern<? super T> pattern16, @NotNull MatchSupplier<? extends X, E> action16,
                                                      @NotNull Pattern<? super T> pattern17, @NotNull MatchSupplier<? extends X, E> action17) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17));
    }

    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6,
                                                      @NotNull Pattern<? super T> pattern7, @NotNull MatchSupplier<? extends X, E> action7,
                                                      @NotNull Pattern<? super T> pattern8, @NotNull MatchSupplier<? extends X, E> action8,
                                                      @NotNull Pattern<? super T> pattern9, @NotNull MatchSupplier<? extends X, E> action9,
                                                      @NotNull Pattern<? super T> pattern10, @NotNull MatchSupplier<? extends X, E> action10,
                                                      @NotNull Pattern<? super T> pattern11, @NotNull MatchSupplier<? extends X, E> action11,
                                                      @NotNull Pattern<? super T> pattern12, @NotNull MatchSupplier<? extends X, E> action12,
                                                      @NotNull Pattern<? super T> pattern13, @NotNull MatchSupplier<? extends X, E> action13,
                                                      @NotNull Pattern<? super T> pattern14, @NotNull MatchSupplier<? extends X, E> action14,
                                                      @NotNull Pattern<? super T> pattern15, @NotNull MatchSupplier<? extends X, E> action15,
                                                      @NotNull Pattern<? super T> pattern16, @NotNull MatchSupplier<? extends X, E> action16,
                                                      @NotNull Pattern<? super T> pattern17, @NotNull MatchSupplier<? extends X, E> action17,
                                                      @NotNull Pattern<? super T> pattern18, @NotNull MatchSupplier<? extends X, E> action18) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18));
    }


    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6,
                                                      @NotNull Pattern<? super T> pattern7, @NotNull MatchSupplier<? extends X, E> action7,
                                                      @NotNull Pattern<? super T> pattern8, @NotNull MatchSupplier<? extends X, E> action8,
                                                      @NotNull Pattern<? super T> pattern9, @NotNull MatchSupplier<? extends X, E> action9,
                                                      @NotNull Pattern<? super T> pattern10, @NotNull MatchSupplier<? extends X, E> action10,
                                                      @NotNull Pattern<? super T> pattern11, @NotNull MatchSupplier<? extends X, E> action11,
                                                      @NotNull Pattern<? super T> pattern12, @NotNull MatchSupplier<? extends X, E> action12,
                                                      @NotNull Pattern<? super T> pattern13, @NotNull MatchSupplier<? extends X, E> action13,
                                                      @NotNull Pattern<? super T> pattern14, @NotNull MatchSupplier<? extends X, E> action14,
                                                      @NotNull Pattern<? super T> pattern15, @NotNull MatchSupplier<? extends X, E> action15,
                                                      @NotNull Pattern<? super T> pattern16, @NotNull MatchSupplier<? extends X, E> action16,
                                                      @NotNull Pattern<? super T> pattern17, @NotNull MatchSupplier<? extends X, E> action17,
                                                      @NotNull Pattern<? super T> pattern18, @NotNull MatchSupplier<? extends X, E> action18,
                                                      @NotNull Pattern<? super T> pattern19, @NotNull MatchSupplier<? extends X, E> action19) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19));
    }

    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6,
                                                      @NotNull Pattern<? super T> pattern7, @NotNull MatchSupplier<? extends X, E> action7,
                                                      @NotNull Pattern<? super T> pattern8, @NotNull MatchSupplier<? extends X, E> action8,
                                                      @NotNull Pattern<? super T> pattern9, @NotNull MatchSupplier<? extends X, E> action9,
                                                      @NotNull Pattern<? super T> pattern10, @NotNull MatchSupplier<? extends X, E> action10,
                                                      @NotNull Pattern<? super T> pattern11, @NotNull MatchSupplier<? extends X, E> action11,
                                                      @NotNull Pattern<? super T> pattern12, @NotNull MatchSupplier<? extends X, E> action12,
                                                      @NotNull Pattern<? super T> pattern13, @NotNull MatchSupplier<? extends X, E> action13,
                                                      @NotNull Pattern<? super T> pattern14, @NotNull MatchSupplier<? extends X, E> action14,
                                                      @NotNull Pattern<? super T> pattern15, @NotNull MatchSupplier<? extends X, E> action15,
                                                      @NotNull Pattern<? super T> pattern16, @NotNull MatchSupplier<? extends X, E> action16,
                                                      @NotNull Pattern<? super T> pattern17, @NotNull MatchSupplier<? extends X, E> action17,
                                                      @NotNull Pattern<? super T> pattern18, @NotNull MatchSupplier<? extends X, E> action18,
                                                      @NotNull Pattern<? super T> pattern19, @NotNull MatchSupplier<? extends X, E> action19,
                                                      @NotNull Pattern<? super T> pattern20, @NotNull MatchSupplier<? extends X, E> action20) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19), withCase(pattern20, action20));
    }


    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6,
                                                      @NotNull Pattern<? super T> pattern7, @NotNull MatchSupplier<? extends X, E> action7,
                                                      @NotNull Pattern<? super T> pattern8, @NotNull MatchSupplier<? extends X, E> action8,
                                                      @NotNull Pattern<? super T> pattern9, @NotNull MatchSupplier<? extends X, E> action9,
                                                      @NotNull Pattern<? super T> pattern10, @NotNull MatchSupplier<? extends X, E> action10,
                                                      @NotNull Pattern<? super T> pattern11, @NotNull MatchSupplier<? extends X, E> action11,
                                                      @NotNull Pattern<? super T> pattern12, @NotNull MatchSupplier<? extends X, E> action12,
                                                      @NotNull Pattern<? super T> pattern13, @NotNull MatchSupplier<? extends X, E> action13,
                                                      @NotNull Pattern<? super T> pattern14, @NotNull MatchSupplier<? extends X, E> action14,
                                                      @NotNull Pattern<? super T> pattern15, @NotNull MatchSupplier<? extends X, E> action15,
                                                      @NotNull Pattern<? super T> pattern16, @NotNull MatchSupplier<? extends X, E> action16,
                                                      @NotNull Pattern<? super T> pattern17, @NotNull MatchSupplier<? extends X, E> action17,
                                                      @NotNull Pattern<? super T> pattern18, @NotNull MatchSupplier<? extends X, E> action18,
                                                      @NotNull Pattern<? super T> pattern19, @NotNull MatchSupplier<? extends X, E> action19,
                                                      @NotNull Pattern<? super T> pattern20, @NotNull MatchSupplier<? extends X, E> action20,
                                                      @NotNull Pattern<? super T> pattern21, @NotNull MatchSupplier<? extends X, E> action21) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19), withCase(pattern20, action20), withCase(pattern21, action21));
    }

    public static <T, X, E extends Throwable> X match(@Nullable T value, @NotNull Pattern<? super T> pattern1, @NotNull MatchSupplier<? extends X, E> action1,
                                                      @NotNull Pattern<? super T> pattern2, @NotNull MatchSupplier<? extends X, E> action2,
                                                      @NotNull Pattern<? super T> pattern3, @NotNull MatchSupplier<? extends X, E> action3,
                                                      @NotNull Pattern<? super T> pattern4, @NotNull MatchSupplier<? extends X, E> action4,
                                                      @NotNull Pattern<? super T> pattern5, @NotNull MatchSupplier<? extends X, E> action5,
                                                      @NotNull Pattern<? super T> pattern6, @NotNull MatchSupplier<? extends X, E> action6,
                                                      @NotNull Pattern<? super T> pattern7, @NotNull MatchSupplier<? extends X, E> action7,
                                                      @NotNull Pattern<? super T> pattern8, @NotNull MatchSupplier<? extends X, E> action8,
                                                      @NotNull Pattern<? super T> pattern9, @NotNull MatchSupplier<? extends X, E> action9,
                                                      @NotNull Pattern<? super T> pattern10, @NotNull MatchSupplier<? extends X, E> action10,
                                                      @NotNull Pattern<? super T> pattern11, @NotNull MatchSupplier<? extends X, E> action11,
                                                      @NotNull Pattern<? super T> pattern12, @NotNull MatchSupplier<? extends X, E> action12,
                                                      @NotNull Pattern<? super T> pattern13, @NotNull MatchSupplier<? extends X, E> action13,
                                                      @NotNull Pattern<? super T> pattern14, @NotNull MatchSupplier<? extends X, E> action14,
                                                      @NotNull Pattern<? super T> pattern15, @NotNull MatchSupplier<? extends X, E> action15,
                                                      @NotNull Pattern<? super T> pattern16, @NotNull MatchSupplier<? extends X, E> action16,
                                                      @NotNull Pattern<? super T> pattern17, @NotNull MatchSupplier<? extends X, E> action17,
                                                      @NotNull Pattern<? super T> pattern18, @NotNull MatchSupplier<? extends X, E> action18,
                                                      @NotNull Pattern<? super T> pattern19, @NotNull MatchSupplier<? extends X, E> action19,
                                                      @NotNull Pattern<? super T> pattern20, @NotNull MatchSupplier<? extends X, E> action20,
                                                      @NotNull Pattern<? super T> pattern21, @NotNull MatchSupplier<? extends X, E> action21,
                                                      @NotNull Pattern<? super T> pattern22, @NotNull MatchSupplier<? extends X, E> action22) throws E, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19), withCase(pattern20, action20), withCase(pattern21, action21),
                withCase(pattern22, action22));
    }














    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1) throws E, MatchException {
        match(value, withCase(pattern1, action1));
    }

    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2));
    }

    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3));
    }

    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4));
    }


    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5));
    }


    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6));
    }

    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6,
                                                      @NotNull Pattern<? super T> pattern7, MatchRunnable<E> action7) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7));
    }

    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6,
                                                      @NotNull Pattern<? super T> pattern7, MatchRunnable<E> action7,
                                                      @NotNull Pattern<? super T> pattern8, MatchRunnable<E> action8) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8));
    }


    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6,
                                                      @NotNull Pattern<? super T> pattern7, MatchRunnable<E> action7,
                                                      @NotNull Pattern<? super T> pattern8, MatchRunnable<E> action8,
                                                      @NotNull Pattern<? super T> pattern9, MatchRunnable<E> action9) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9));
    }


    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6,
                                                      @NotNull Pattern<? super T> pattern7, MatchRunnable<E> action7,
                                                      @NotNull Pattern<? super T> pattern8, MatchRunnable<E> action8,
                                                      @NotNull Pattern<? super T> pattern9, MatchRunnable<E> action9,
                                                      @NotNull Pattern<? super T> pattern10, MatchRunnable<E> action10) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10));
    }

    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6,
                                                      @NotNull Pattern<? super T> pattern7, MatchRunnable<E> action7,
                                                      @NotNull Pattern<? super T> pattern8, MatchRunnable<E> action8,
                                                      @NotNull Pattern<? super T> pattern9, MatchRunnable<E> action9,
                                                      @NotNull Pattern<? super T> pattern10, MatchRunnable<E> action10,
                                                      @NotNull Pattern<? super T> pattern11, MatchRunnable<E> action11) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11));
    }

    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6,
                                                      @NotNull Pattern<? super T> pattern7, MatchRunnable<E> action7,
                                                      @NotNull Pattern<? super T> pattern8, MatchRunnable<E> action8,
                                                      @NotNull Pattern<? super T> pattern9, MatchRunnable<E> action9,
                                                      @NotNull Pattern<? super T> pattern10, MatchRunnable<E> action10,
                                                      @NotNull Pattern<? super T> pattern11, MatchRunnable<E> action11,
                                                      @NotNull Pattern<? super T> pattern12, MatchRunnable<E> action12) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12));
    }

    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6,
                                                      @NotNull Pattern<? super T> pattern7, MatchRunnable<E> action7,
                                                      @NotNull Pattern<? super T> pattern8, MatchRunnable<E> action8,
                                                      @NotNull Pattern<? super T> pattern9, MatchRunnable<E> action9,
                                                      @NotNull Pattern<? super T> pattern10, MatchRunnable<E> action10,
                                                      @NotNull Pattern<? super T> pattern11, MatchRunnable<E> action11,
                                                      @NotNull Pattern<? super T> pattern12, MatchRunnable<E> action12,
                                                      @NotNull Pattern<? super T> pattern13, MatchRunnable<E> action13) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13));
    }


    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6,
                                                      @NotNull Pattern<? super T> pattern7, MatchRunnable<E> action7,
                                                      @NotNull Pattern<? super T> pattern8, MatchRunnable<E> action8,
                                                      @NotNull Pattern<? super T> pattern9, MatchRunnable<E> action9,
                                                      @NotNull Pattern<? super T> pattern10, MatchRunnable<E> action10,
                                                      @NotNull Pattern<? super T> pattern11, MatchRunnable<E> action11,
                                                      @NotNull Pattern<? super T> pattern12, MatchRunnable<E> action12,
                                                      @NotNull Pattern<? super T> pattern13, MatchRunnable<E> action13,
                                                      @NotNull Pattern<? super T> pattern14, MatchRunnable<E> action14) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14));
    }


    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6,
                                                      @NotNull Pattern<? super T> pattern7, MatchRunnable<E> action7,
                                                      @NotNull Pattern<? super T> pattern8, MatchRunnable<E> action8,
                                                      @NotNull Pattern<? super T> pattern9, MatchRunnable<E> action9,
                                                      @NotNull Pattern<? super T> pattern10, MatchRunnable<E> action10,
                                                      @NotNull Pattern<? super T> pattern11, MatchRunnable<E> action11,
                                                      @NotNull Pattern<? super T> pattern12, MatchRunnable<E> action12,
                                                      @NotNull Pattern<? super T> pattern13, MatchRunnable<E> action13,
                                                      @NotNull Pattern<? super T> pattern14, MatchRunnable<E> action14,
                                                      @NotNull Pattern<? super T> pattern15, MatchRunnable<E> action15) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15));
    }

    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6,
                                                      @NotNull Pattern<? super T> pattern7, MatchRunnable<E> action7,
                                                      @NotNull Pattern<? super T> pattern8, MatchRunnable<E> action8,
                                                      @NotNull Pattern<? super T> pattern9, MatchRunnable<E> action9,
                                                      @NotNull Pattern<? super T> pattern10, MatchRunnable<E> action10,
                                                      @NotNull Pattern<? super T> pattern11, MatchRunnable<E> action11,
                                                      @NotNull Pattern<? super T> pattern12, MatchRunnable<E> action12,
                                                      @NotNull Pattern<? super T> pattern13, MatchRunnable<E> action13,
                                                      @NotNull Pattern<? super T> pattern14, MatchRunnable<E> action14,
                                                      @NotNull Pattern<? super T> pattern15, MatchRunnable<E> action15,
                                                      @NotNull Pattern<? super T> pattern16, MatchRunnable<E> action16) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16));
    }

    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6,
                                                      @NotNull Pattern<? super T> pattern7, MatchRunnable<E> action7,
                                                      @NotNull Pattern<? super T> pattern8, MatchRunnable<E> action8,
                                                      @NotNull Pattern<? super T> pattern9, MatchRunnable<E> action9,
                                                      @NotNull Pattern<? super T> pattern10, MatchRunnable<E> action10,
                                                      @NotNull Pattern<? super T> pattern11, MatchRunnable<E> action11,
                                                      @NotNull Pattern<? super T> pattern12, MatchRunnable<E> action12,
                                                      @NotNull Pattern<? super T> pattern13, MatchRunnable<E> action13,
                                                      @NotNull Pattern<? super T> pattern14, MatchRunnable<E> action14,
                                                      @NotNull Pattern<? super T> pattern15, MatchRunnable<E> action15,
                                                      @NotNull Pattern<? super T> pattern16, MatchRunnable<E> action16,
                                                      @NotNull Pattern<? super T> pattern17, MatchRunnable<E> action17) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17));
    }

    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6,
                                                      @NotNull Pattern<? super T> pattern7, MatchRunnable<E> action7,
                                                      @NotNull Pattern<? super T> pattern8, MatchRunnable<E> action8,
                                                      @NotNull Pattern<? super T> pattern9, MatchRunnable<E> action9,
                                                      @NotNull Pattern<? super T> pattern10, MatchRunnable<E> action10,
                                                      @NotNull Pattern<? super T> pattern11, MatchRunnable<E> action11,
                                                      @NotNull Pattern<? super T> pattern12, MatchRunnable<E> action12,
                                                      @NotNull Pattern<? super T> pattern13, MatchRunnable<E> action13,
                                                      @NotNull Pattern<? super T> pattern14, MatchRunnable<E> action14,
                                                      @NotNull Pattern<? super T> pattern15, MatchRunnable<E> action15,
                                                      @NotNull Pattern<? super T> pattern16, MatchRunnable<E> action16,
                                                      @NotNull Pattern<? super T> pattern17, MatchRunnable<E> action17,
                                                      @NotNull Pattern<? super T> pattern18, MatchRunnable<E> action18) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18));
    }


    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6,
                                                      @NotNull Pattern<? super T> pattern7, MatchRunnable<E> action7,
                                                      @NotNull Pattern<? super T> pattern8, MatchRunnable<E> action8,
                                                      @NotNull Pattern<? super T> pattern9, MatchRunnable<E> action9,
                                                      @NotNull Pattern<? super T> pattern10, MatchRunnable<E> action10,
                                                      @NotNull Pattern<? super T> pattern11, MatchRunnable<E> action11,
                                                      @NotNull Pattern<? super T> pattern12, MatchRunnable<E> action12,
                                                      @NotNull Pattern<? super T> pattern13, MatchRunnable<E> action13,
                                                      @NotNull Pattern<? super T> pattern14, MatchRunnable<E> action14,
                                                      @NotNull Pattern<? super T> pattern15, MatchRunnable<E> action15,
                                                      @NotNull Pattern<? super T> pattern16, MatchRunnable<E> action16,
                                                      @NotNull Pattern<? super T> pattern17, MatchRunnable<E> action17,
                                                      @NotNull Pattern<? super T> pattern18, MatchRunnable<E> action18,
                                                      @NotNull Pattern<? super T> pattern19, MatchRunnable<E> action19) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19));
    }

    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6,
                                                      @NotNull Pattern<? super T> pattern7, MatchRunnable<E> action7,
                                                      @NotNull Pattern<? super T> pattern8, MatchRunnable<E> action8,
                                                      @NotNull Pattern<? super T> pattern9, MatchRunnable<E> action9,
                                                      @NotNull Pattern<? super T> pattern10, MatchRunnable<E> action10,
                                                      @NotNull Pattern<? super T> pattern11, MatchRunnable<E> action11,
                                                      @NotNull Pattern<? super T> pattern12, MatchRunnable<E> action12,
                                                      @NotNull Pattern<? super T> pattern13, MatchRunnable<E> action13,
                                                      @NotNull Pattern<? super T> pattern14, MatchRunnable<E> action14,
                                                      @NotNull Pattern<? super T> pattern15, MatchRunnable<E> action15,
                                                      @NotNull Pattern<? super T> pattern16, MatchRunnable<E> action16,
                                                      @NotNull Pattern<? super T> pattern17, MatchRunnable<E> action17,
                                                      @NotNull Pattern<? super T> pattern18, MatchRunnable<E> action18,
                                                      @NotNull Pattern<? super T> pattern19, MatchRunnable<E> action19,
                                                      @NotNull Pattern<? super T> pattern20, MatchRunnable<E> action20) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19), withCase(pattern20, action20));
    }


    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6,
                                                      @NotNull Pattern<? super T> pattern7, MatchRunnable<E> action7,
                                                      @NotNull Pattern<? super T> pattern8, MatchRunnable<E> action8,
                                                      @NotNull Pattern<? super T> pattern9, MatchRunnable<E> action9,
                                                      @NotNull Pattern<? super T> pattern10, MatchRunnable<E> action10,
                                                      @NotNull Pattern<? super T> pattern11, MatchRunnable<E> action11,
                                                      @NotNull Pattern<? super T> pattern12, MatchRunnable<E> action12,
                                                      @NotNull Pattern<? super T> pattern13, MatchRunnable<E> action13,
                                                      @NotNull Pattern<? super T> pattern14, MatchRunnable<E> action14,
                                                      @NotNull Pattern<? super T> pattern15, MatchRunnable<E> action15,
                                                      @NotNull Pattern<? super T> pattern16, MatchRunnable<E> action16,
                                                      @NotNull Pattern<? super T> pattern17, MatchRunnable<E> action17,
                                                      @NotNull Pattern<? super T> pattern18, MatchRunnable<E> action18,
                                                      @NotNull Pattern<? super T> pattern19, MatchRunnable<E> action19,
                                                      @NotNull Pattern<? super T> pattern20, MatchRunnable<E> action20,
                                                      @NotNull Pattern<? super T> pattern21, MatchRunnable<E> action21) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19), withCase(pattern20, action20), withCase(pattern21, action21));
    }

    public static <T, E extends Throwable> void match(@Nullable T value, @NotNull Pattern<? super T> pattern1, MatchRunnable<E> action1,
                                                      @NotNull Pattern<? super T> pattern2, MatchRunnable<E> action2,
                                                      @NotNull Pattern<? super T> pattern3, MatchRunnable<E> action3,
                                                      @NotNull Pattern<? super T> pattern4, MatchRunnable<E> action4,
                                                      @NotNull Pattern<? super T> pattern5, MatchRunnable<E> action5,
                                                      @NotNull Pattern<? super T> pattern6, MatchRunnable<E> action6,
                                                      @NotNull Pattern<? super T> pattern7, MatchRunnable<E> action7,
                                                      @NotNull Pattern<? super T> pattern8, MatchRunnable<E> action8,
                                                      @NotNull Pattern<? super T> pattern9, MatchRunnable<E> action9,
                                                      @NotNull Pattern<? super T> pattern10, MatchRunnable<E> action10,
                                                      @NotNull Pattern<? super T> pattern11, MatchRunnable<E> action11,
                                                      @NotNull Pattern<? super T> pattern12, MatchRunnable<E> action12,
                                                      @NotNull Pattern<? super T> pattern13, MatchRunnable<E> action13,
                                                      @NotNull Pattern<? super T> pattern14, MatchRunnable<E> action14,
                                                      @NotNull Pattern<? super T> pattern15, MatchRunnable<E> action15,
                                                      @NotNull Pattern<? super T> pattern16, MatchRunnable<E> action16,
                                                      @NotNull Pattern<? super T> pattern17, MatchRunnable<E> action17,
                                                      @NotNull Pattern<? super T> pattern18, MatchRunnable<E> action18,
                                                      @NotNull Pattern<? super T> pattern19, MatchRunnable<E> action19,
                                                      @NotNull Pattern<? super T> pattern20, MatchRunnable<E> action20,
                                                      @NotNull Pattern<? super T> pattern21, MatchRunnable<E> action21,
                                                      @NotNull Pattern<? super T> pattern22, MatchRunnable<E> action22) throws E, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19), withCase(pattern20, action20), withCase(pattern21, action21),
                withCase(pattern22, action22));
    }

}
