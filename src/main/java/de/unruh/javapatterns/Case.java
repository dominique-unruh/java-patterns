package de.unruh.javapatterns;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** One case in a pattern match. Consists of a pattern and an action that has to happen
 * in case of a successful match. See {@link Match}. An instance of this class is
 * created using {@link Match#withCase(Pattern, MatchSupplier)}.
 * @param <In> Type of the value that is pattern matched
 * @param <Return> Return value of the action
 * @param <Exn> Exception that the action may throw. (The action is additionally allowed to
 *             throw {@link PatternMatchReject} by invoking {@link Pattern#reject() reject()}.
 */
public class Case<In, Return, Exn extends Throwable> {
    private final Pattern<? super In> pattern;
    private final MatchSupplier<? extends Return, Exn> action;

    @Contract(pure = true)
    Case(@NotNull Pattern<? super In> pattern, @NotNull MatchSupplier<? extends Return, Exn> action) {
        this.pattern = pattern;
        this.action = action;
    }

    @NotNull
    PatternResult<Return> apply(@NotNull MatchManager mgr, @Nullable In t) throws Exn {
        try {
            pattern.apply(mgr, t);
            return new PatternResultSome<>(action.call());
        } catch (PatternMatchReject e) {
            return new PatternResultNone<>();
        } finally {
            mgr.clearCaptured();
        }
    }
}
