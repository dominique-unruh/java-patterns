package de.unruh.javapatterns;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Functions for invoking pattern matches.
 * This is an uninstantiable class containing only static members.<p>
 *
 * It contains functions for applying patterns to a given value and executing code when of them matches.<p>
 *
 * The general pattern for executing a pattern match is:
 * <pre>
 * result = {@link #match}(value, {@link #withCase}(pattern1, action1), {@link #withCase}(pattern2, action2), ...)
 * </pre>
 * Here {@code value} is the value to pattern match, and {@code pattern1, ...} are the patterns to try and apply.
 * For the first pattern that matches, the corresponding action is executed (with access to the values captured
 * by the pattern, see {@link Capture}), and {@code match} returns the return value of that action. An action is a lambda expression of the
 * form {@code () -> ...}, returning either some value or {@code void}. If the action invokes {@link Pattern#reject()},
 * matching continues with the next pattern. If no pattern matches, {@code match} throws a {@link MatchException}.<p>
 *
 * The patterns are of type {@link Pattern}, see there for explanations how to specify patterns.<p>
 *
 * For better readability, the match can also be written as
 * <pre>
 * result = match(value, pattern1, action1, pattern2, action2, ...)
 * </pre>
 * with at most 22 pattern/action pairs. (This is enabled via a large number of helper functions in this class,
 * all called {@code match}. These helper functions are elided from this API documentation to keep things readable.)
 */
public final class Match {
    /** Make this class uninstantiable */
    @Contract(pure = true)
    private Match() {}

    /** Constructs a {@link Case} object from a pattern and lambda expression.
     * Used as an argument to {@link #match(Object, Case[])}.
     *
     * @param pattern Pattern to apply in a match
     * @param action Action to perform when the pattern matches. Usually given as a lambda expression.
     *               If the lambda expression returns {@code void}, use {@link #withCase(Pattern, MatchRunnable)}.
     * @param <In> Type of the value to be pattern matched.
     * @param <Exn> Exceptions that the action might throw ({@link PatternMatchReject} does not need to be
     *             declared here even if {@link Pattern#reject()} is used.)
     * @param <Return> Return type of the action */
    @Contract(pure = true, value = "_, _ -> new")
    public static <In, Return, Exn extends Throwable> Case <In, Return, Exn>
    withCase(@NotNull Pattern<? super In> pattern, @NotNull MatchSupplier<? extends Return, Exn> action) {
        return new Case<>(pattern, action);
    }

    /** Constructs a {@link Case} object from a pattern and lambda expression returning {@code void}.
     * @param pattern Pattern to apply in a match
     * @param action Action to perform when the pattern matches. Usually given as a lambda expression.
     *               Must return {@code void}, use {@link #withCase(Pattern, MatchSupplier)} otherwise.
     * @param <In> Type of the value to be pattern matched.
     * @param <Exn> Exceptions that the action might throw ({@link PatternMatchReject} does not need to be
     *             declared here even if {@link Pattern#reject()} is used.)
     */
    @Contract(pure = true, value = "_, _ -> new")
    public static <In, Exn extends Throwable> Case <In, Void, Exn>
    withCase(@NotNull Pattern<? super In> pattern, @NotNull MatchRunnable<Exn> action) {
        return new Case<>(pattern, () -> { action.run(); return null; } );
    }

    /** Performs a pattern match.
     * Applies each of the cases in sequence to @{code value}, and returns the return value of the first
     * successful case.
     * @param value The value to be pattern matched
     * @param cases Cases to try. Each case consists of a pattern and an action that is executed in case of
     *              a successful match. See {@link Case}.
     * @param <In> Type of the value to be pattern matched.
     * @param <Exn> Exceptions that the action might throw ({@link PatternMatchReject} does not need to be
     *             declared here even if {@link Pattern#reject()} is used.)
     * @param <Return> Return type of the action
     * @throws Exn if the action throws it
     * @throws MatchException if none of the cases match
     */
    @SafeVarargs
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Case<In, Return, Exn>... cases) throws Exn, MatchException {
        MatchManager mgr = new MatchManager();
        for (Case<In, Return, Exn> cas : cases) {
            PatternResult<Return> result = cas.apply(mgr, value);
            if (!result.isEmpty())
                return result.get();
        }
        throw new MatchException(value);
    }

    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value,
                                                      @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1));
    }

    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value,
                                                      @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2));
    }

    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3));
    }

    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4));
    }


    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5));
    }


    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6));
    }

    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, @NotNull MatchSupplier<? extends Return, Exn> action7) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7));
    }

    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, @NotNull MatchSupplier<? extends Return, Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, @NotNull MatchSupplier<? extends Return, Exn> action8) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8));
    }


    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, @NotNull MatchSupplier<? extends Return, Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, @NotNull MatchSupplier<? extends Return, Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, @NotNull MatchSupplier<? extends Return, Exn> action9) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9));
    }


    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, @NotNull MatchSupplier<? extends Return, Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, @NotNull MatchSupplier<? extends Return, Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, @NotNull MatchSupplier<? extends Return, Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, @NotNull MatchSupplier<? extends Return, Exn> action10) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10));
    }

    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, @NotNull MatchSupplier<? extends Return, Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, @NotNull MatchSupplier<? extends Return, Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, @NotNull MatchSupplier<? extends Return, Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, @NotNull MatchSupplier<? extends Return, Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, @NotNull MatchSupplier<? extends Return, Exn> action11) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11));
    }

    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, @NotNull MatchSupplier<? extends Return, Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, @NotNull MatchSupplier<? extends Return, Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, @NotNull MatchSupplier<? extends Return, Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, @NotNull MatchSupplier<? extends Return, Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, @NotNull MatchSupplier<? extends Return, Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, @NotNull MatchSupplier<? extends Return, Exn> action12) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12));
    }

    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, @NotNull MatchSupplier<? extends Return, Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, @NotNull MatchSupplier<? extends Return, Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, @NotNull MatchSupplier<? extends Return, Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, @NotNull MatchSupplier<? extends Return, Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, @NotNull MatchSupplier<? extends Return, Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, @NotNull MatchSupplier<? extends Return, Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, @NotNull MatchSupplier<? extends Return, Exn> action13) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13));
    }


    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, @NotNull MatchSupplier<? extends Return, Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, @NotNull MatchSupplier<? extends Return, Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, @NotNull MatchSupplier<? extends Return, Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, @NotNull MatchSupplier<? extends Return, Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, @NotNull MatchSupplier<? extends Return, Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, @NotNull MatchSupplier<? extends Return, Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, @NotNull MatchSupplier<? extends Return, Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, @NotNull MatchSupplier<? extends Return, Exn> action14) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14));
    }


    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, @NotNull MatchSupplier<? extends Return, Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, @NotNull MatchSupplier<? extends Return, Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, @NotNull MatchSupplier<? extends Return, Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, @NotNull MatchSupplier<? extends Return, Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, @NotNull MatchSupplier<? extends Return, Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, @NotNull MatchSupplier<? extends Return, Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, @NotNull MatchSupplier<? extends Return, Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, @NotNull MatchSupplier<? extends Return, Exn> action14,
                                                      @NotNull Pattern<? super In> pattern15, @NotNull MatchSupplier<? extends Return, Exn> action15) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15));
    }

    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, @NotNull MatchSupplier<? extends Return, Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, @NotNull MatchSupplier<? extends Return, Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, @NotNull MatchSupplier<? extends Return, Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, @NotNull MatchSupplier<? extends Return, Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, @NotNull MatchSupplier<? extends Return, Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, @NotNull MatchSupplier<? extends Return, Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, @NotNull MatchSupplier<? extends Return, Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, @NotNull MatchSupplier<? extends Return, Exn> action14,
                                                      @NotNull Pattern<? super In> pattern15, @NotNull MatchSupplier<? extends Return, Exn> action15,
                                                      @NotNull Pattern<? super In> pattern16, @NotNull MatchSupplier<? extends Return, Exn> action16) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16));
    }

    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, @NotNull MatchSupplier<? extends Return, Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, @NotNull MatchSupplier<? extends Return, Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, @NotNull MatchSupplier<? extends Return, Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, @NotNull MatchSupplier<? extends Return, Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, @NotNull MatchSupplier<? extends Return, Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, @NotNull MatchSupplier<? extends Return, Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, @NotNull MatchSupplier<? extends Return, Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, @NotNull MatchSupplier<? extends Return, Exn> action14,
                                                      @NotNull Pattern<? super In> pattern15, @NotNull MatchSupplier<? extends Return, Exn> action15,
                                                      @NotNull Pattern<? super In> pattern16, @NotNull MatchSupplier<? extends Return, Exn> action16,
                                                      @NotNull Pattern<? super In> pattern17, @NotNull MatchSupplier<? extends Return, Exn> action17) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17));
    }

    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, @NotNull MatchSupplier<? extends Return, Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, @NotNull MatchSupplier<? extends Return, Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, @NotNull MatchSupplier<? extends Return, Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, @NotNull MatchSupplier<? extends Return, Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, @NotNull MatchSupplier<? extends Return, Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, @NotNull MatchSupplier<? extends Return, Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, @NotNull MatchSupplier<? extends Return, Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, @NotNull MatchSupplier<? extends Return, Exn> action14,
                                                      @NotNull Pattern<? super In> pattern15, @NotNull MatchSupplier<? extends Return, Exn> action15,
                                                      @NotNull Pattern<? super In> pattern16, @NotNull MatchSupplier<? extends Return, Exn> action16,
                                                      @NotNull Pattern<? super In> pattern17, @NotNull MatchSupplier<? extends Return, Exn> action17,
                                                      @NotNull Pattern<? super In> pattern18, @NotNull MatchSupplier<? extends Return, Exn> action18) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18));
    }


    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, @NotNull MatchSupplier<? extends Return, Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, @NotNull MatchSupplier<? extends Return, Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, @NotNull MatchSupplier<? extends Return, Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, @NotNull MatchSupplier<? extends Return, Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, @NotNull MatchSupplier<? extends Return, Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, @NotNull MatchSupplier<? extends Return, Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, @NotNull MatchSupplier<? extends Return, Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, @NotNull MatchSupplier<? extends Return, Exn> action14,
                                                      @NotNull Pattern<? super In> pattern15, @NotNull MatchSupplier<? extends Return, Exn> action15,
                                                      @NotNull Pattern<? super In> pattern16, @NotNull MatchSupplier<? extends Return, Exn> action16,
                                                      @NotNull Pattern<? super In> pattern17, @NotNull MatchSupplier<? extends Return, Exn> action17,
                                                      @NotNull Pattern<? super In> pattern18, @NotNull MatchSupplier<? extends Return, Exn> action18,
                                                      @NotNull Pattern<? super In> pattern19, @NotNull MatchSupplier<? extends Return, Exn> action19) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19));
    }

    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, @NotNull MatchSupplier<? extends Return, Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, @NotNull MatchSupplier<? extends Return, Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, @NotNull MatchSupplier<? extends Return, Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, @NotNull MatchSupplier<? extends Return, Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, @NotNull MatchSupplier<? extends Return, Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, @NotNull MatchSupplier<? extends Return, Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, @NotNull MatchSupplier<? extends Return, Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, @NotNull MatchSupplier<? extends Return, Exn> action14,
                                                      @NotNull Pattern<? super In> pattern15, @NotNull MatchSupplier<? extends Return, Exn> action15,
                                                      @NotNull Pattern<? super In> pattern16, @NotNull MatchSupplier<? extends Return, Exn> action16,
                                                      @NotNull Pattern<? super In> pattern17, @NotNull MatchSupplier<? extends Return, Exn> action17,
                                                      @NotNull Pattern<? super In> pattern18, @NotNull MatchSupplier<? extends Return, Exn> action18,
                                                      @NotNull Pattern<? super In> pattern19, @NotNull MatchSupplier<? extends Return, Exn> action19,
                                                      @NotNull Pattern<? super In> pattern20, @NotNull MatchSupplier<? extends Return, Exn> action20) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19), withCase(pattern20, action20));
    }


    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, @NotNull MatchSupplier<? extends Return, Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, @NotNull MatchSupplier<? extends Return, Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, @NotNull MatchSupplier<? extends Return, Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, @NotNull MatchSupplier<? extends Return, Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, @NotNull MatchSupplier<? extends Return, Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, @NotNull MatchSupplier<? extends Return, Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, @NotNull MatchSupplier<? extends Return, Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, @NotNull MatchSupplier<? extends Return, Exn> action14,
                                                      @NotNull Pattern<? super In> pattern15, @NotNull MatchSupplier<? extends Return, Exn> action15,
                                                      @NotNull Pattern<? super In> pattern16, @NotNull MatchSupplier<? extends Return, Exn> action16,
                                                      @NotNull Pattern<? super In> pattern17, @NotNull MatchSupplier<? extends Return, Exn> action17,
                                                      @NotNull Pattern<? super In> pattern18, @NotNull MatchSupplier<? extends Return, Exn> action18,
                                                      @NotNull Pattern<? super In> pattern19, @NotNull MatchSupplier<? extends Return, Exn> action19,
                                                      @NotNull Pattern<? super In> pattern20, @NotNull MatchSupplier<? extends Return, Exn> action20,
                                                      @NotNull Pattern<? super In> pattern21, @NotNull MatchSupplier<? extends Return, Exn> action21) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19), withCase(pattern20, action20), withCase(pattern21, action21));
    }

    /** @hidden */
    public static <In, Return, Exn extends Throwable> Return match(@Nullable In value, @NotNull Pattern<? super In> pattern1, @NotNull MatchSupplier<? extends Return, Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, @NotNull MatchSupplier<? extends Return, Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, @NotNull MatchSupplier<? extends Return, Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, @NotNull MatchSupplier<? extends Return, Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, @NotNull MatchSupplier<? extends Return, Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, @NotNull MatchSupplier<? extends Return, Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, @NotNull MatchSupplier<? extends Return, Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, @NotNull MatchSupplier<? extends Return, Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, @NotNull MatchSupplier<? extends Return, Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, @NotNull MatchSupplier<? extends Return, Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, @NotNull MatchSupplier<? extends Return, Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, @NotNull MatchSupplier<? extends Return, Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, @NotNull MatchSupplier<? extends Return, Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, @NotNull MatchSupplier<? extends Return, Exn> action14,
                                                      @NotNull Pattern<? super In> pattern15, @NotNull MatchSupplier<? extends Return, Exn> action15,
                                                      @NotNull Pattern<? super In> pattern16, @NotNull MatchSupplier<? extends Return, Exn> action16,
                                                      @NotNull Pattern<? super In> pattern17, @NotNull MatchSupplier<? extends Return, Exn> action17,
                                                      @NotNull Pattern<? super In> pattern18, @NotNull MatchSupplier<? extends Return, Exn> action18,
                                                      @NotNull Pattern<? super In> pattern19, @NotNull MatchSupplier<? extends Return, Exn> action19,
                                                      @NotNull Pattern<? super In> pattern20, @NotNull MatchSupplier<? extends Return, Exn> action20,
                                                      @NotNull Pattern<? super In> pattern21, @NotNull MatchSupplier<? extends Return, Exn> action21,
                                                      @NotNull Pattern<? super In> pattern22, @NotNull MatchSupplier<? extends Return, Exn> action22) throws Exn, MatchException {
        return match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19), withCase(pattern20, action20), withCase(pattern21, action21),
                withCase(pattern22, action22));
    }














    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1) throws Exn, MatchException {
        match(value, withCase(pattern1, action1));
    }

    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2));
    }

    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3));
    }

    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4));
    }


    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5));
    }


    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6));
    }

    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, MatchRunnable<Exn> action7) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7));
    }

    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, MatchRunnable<Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, MatchRunnable<Exn> action8) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8));
    }


    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, MatchRunnable<Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, MatchRunnable<Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, MatchRunnable<Exn> action9) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9));
    }


    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, MatchRunnable<Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, MatchRunnable<Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, MatchRunnable<Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, MatchRunnable<Exn> action10) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10));
    }

    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, MatchRunnable<Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, MatchRunnable<Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, MatchRunnable<Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, MatchRunnable<Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, MatchRunnable<Exn> action11) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11));
    }

    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, MatchRunnable<Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, MatchRunnable<Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, MatchRunnable<Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, MatchRunnable<Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, MatchRunnable<Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, MatchRunnable<Exn> action12) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12));
    }

    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, MatchRunnable<Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, MatchRunnable<Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, MatchRunnable<Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, MatchRunnable<Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, MatchRunnable<Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, MatchRunnable<Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, MatchRunnable<Exn> action13) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13));
    }


    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, MatchRunnable<Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, MatchRunnable<Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, MatchRunnable<Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, MatchRunnable<Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, MatchRunnable<Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, MatchRunnable<Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, MatchRunnable<Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, MatchRunnable<Exn> action14) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14));
    }


    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, MatchRunnable<Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, MatchRunnable<Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, MatchRunnable<Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, MatchRunnable<Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, MatchRunnable<Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, MatchRunnable<Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, MatchRunnable<Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, MatchRunnable<Exn> action14,
                                                      @NotNull Pattern<? super In> pattern15, MatchRunnable<Exn> action15) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15));
    }

    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, MatchRunnable<Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, MatchRunnable<Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, MatchRunnable<Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, MatchRunnable<Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, MatchRunnable<Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, MatchRunnable<Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, MatchRunnable<Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, MatchRunnable<Exn> action14,
                                                      @NotNull Pattern<? super In> pattern15, MatchRunnable<Exn> action15,
                                                      @NotNull Pattern<? super In> pattern16, MatchRunnable<Exn> action16) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16));
    }

    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, MatchRunnable<Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, MatchRunnable<Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, MatchRunnable<Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, MatchRunnable<Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, MatchRunnable<Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, MatchRunnable<Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, MatchRunnable<Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, MatchRunnable<Exn> action14,
                                                      @NotNull Pattern<? super In> pattern15, MatchRunnable<Exn> action15,
                                                      @NotNull Pattern<? super In> pattern16, MatchRunnable<Exn> action16,
                                                      @NotNull Pattern<? super In> pattern17, MatchRunnable<Exn> action17) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17));
    }

    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, MatchRunnable<Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, MatchRunnable<Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, MatchRunnable<Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, MatchRunnable<Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, MatchRunnable<Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, MatchRunnable<Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, MatchRunnable<Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, MatchRunnable<Exn> action14,
                                                      @NotNull Pattern<? super In> pattern15, MatchRunnable<Exn> action15,
                                                      @NotNull Pattern<? super In> pattern16, MatchRunnable<Exn> action16,
                                                      @NotNull Pattern<? super In> pattern17, MatchRunnable<Exn> action17,
                                                      @NotNull Pattern<? super In> pattern18, MatchRunnable<Exn> action18) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18));
    }


    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, MatchRunnable<Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, MatchRunnable<Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, MatchRunnable<Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, MatchRunnable<Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, MatchRunnable<Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, MatchRunnable<Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, MatchRunnable<Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, MatchRunnable<Exn> action14,
                                                      @NotNull Pattern<? super In> pattern15, MatchRunnable<Exn> action15,
                                                      @NotNull Pattern<? super In> pattern16, MatchRunnable<Exn> action16,
                                                      @NotNull Pattern<? super In> pattern17, MatchRunnable<Exn> action17,
                                                      @NotNull Pattern<? super In> pattern18, MatchRunnable<Exn> action18,
                                                      @NotNull Pattern<? super In> pattern19, MatchRunnable<Exn> action19) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19));
    }

    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, MatchRunnable<Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, MatchRunnable<Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, MatchRunnable<Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, MatchRunnable<Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, MatchRunnable<Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, MatchRunnable<Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, MatchRunnable<Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, MatchRunnable<Exn> action14,
                                                      @NotNull Pattern<? super In> pattern15, MatchRunnable<Exn> action15,
                                                      @NotNull Pattern<? super In> pattern16, MatchRunnable<Exn> action16,
                                                      @NotNull Pattern<? super In> pattern17, MatchRunnable<Exn> action17,
                                                      @NotNull Pattern<? super In> pattern18, MatchRunnable<Exn> action18,
                                                      @NotNull Pattern<? super In> pattern19, MatchRunnable<Exn> action19,
                                                      @NotNull Pattern<? super In> pattern20, MatchRunnable<Exn> action20) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19), withCase(pattern20, action20));
    }


    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, MatchRunnable<Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, MatchRunnable<Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, MatchRunnable<Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, MatchRunnable<Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, MatchRunnable<Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, MatchRunnable<Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, MatchRunnable<Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, MatchRunnable<Exn> action14,
                                                      @NotNull Pattern<? super In> pattern15, MatchRunnable<Exn> action15,
                                                      @NotNull Pattern<? super In> pattern16, MatchRunnable<Exn> action16,
                                                      @NotNull Pattern<? super In> pattern17, MatchRunnable<Exn> action17,
                                                      @NotNull Pattern<? super In> pattern18, MatchRunnable<Exn> action18,
                                                      @NotNull Pattern<? super In> pattern19, MatchRunnable<Exn> action19,
                                                      @NotNull Pattern<? super In> pattern20, MatchRunnable<Exn> action20,
                                                      @NotNull Pattern<? super In> pattern21, MatchRunnable<Exn> action21) throws Exn, MatchException {
        match(value, withCase(pattern1, action1), withCase(pattern2, action2), withCase(pattern3, action3),
                withCase(pattern4, action4), withCase(pattern5, action5), withCase(pattern6, action6),
                withCase(pattern7, action7), withCase(pattern8, action8), withCase(pattern9, action9),
                withCase(pattern10, action10), withCase(pattern11, action11), withCase(pattern12, action12),
                withCase(pattern13, action13), withCase(pattern14, action14), withCase(pattern15, action15),
                withCase(pattern16, action16), withCase(pattern17, action17), withCase(pattern18, action18),
                withCase(pattern19, action19), withCase(pattern20, action20), withCase(pattern21, action21));
    }

    /** @hidden */
    public static <In, Exn extends Throwable> void match(@Nullable In value, @NotNull Pattern<? super In> pattern1, MatchRunnable<Exn> action1,
                                                      @NotNull Pattern<? super In> pattern2, MatchRunnable<Exn> action2,
                                                      @NotNull Pattern<? super In> pattern3, MatchRunnable<Exn> action3,
                                                      @NotNull Pattern<? super In> pattern4, MatchRunnable<Exn> action4,
                                                      @NotNull Pattern<? super In> pattern5, MatchRunnable<Exn> action5,
                                                      @NotNull Pattern<? super In> pattern6, MatchRunnable<Exn> action6,
                                                      @NotNull Pattern<? super In> pattern7, MatchRunnable<Exn> action7,
                                                      @NotNull Pattern<? super In> pattern8, MatchRunnable<Exn> action8,
                                                      @NotNull Pattern<? super In> pattern9, MatchRunnable<Exn> action9,
                                                      @NotNull Pattern<? super In> pattern10, MatchRunnable<Exn> action10,
                                                      @NotNull Pattern<? super In> pattern11, MatchRunnable<Exn> action11,
                                                      @NotNull Pattern<? super In> pattern12, MatchRunnable<Exn> action12,
                                                      @NotNull Pattern<? super In> pattern13, MatchRunnable<Exn> action13,
                                                      @NotNull Pattern<? super In> pattern14, MatchRunnable<Exn> action14,
                                                      @NotNull Pattern<? super In> pattern15, MatchRunnable<Exn> action15,
                                                      @NotNull Pattern<? super In> pattern16, MatchRunnable<Exn> action16,
                                                      @NotNull Pattern<? super In> pattern17, MatchRunnable<Exn> action17,
                                                      @NotNull Pattern<? super In> pattern18, MatchRunnable<Exn> action18,
                                                      @NotNull Pattern<? super In> pattern19, MatchRunnable<Exn> action19,
                                                      @NotNull Pattern<? super In> pattern20, MatchRunnable<Exn> action20,
                                                      @NotNull Pattern<? super In> pattern21, MatchRunnable<Exn> action21,
                                                      @NotNull Pattern<? super In> pattern22, MatchRunnable<Exn> action22) throws Exn, MatchException {
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
