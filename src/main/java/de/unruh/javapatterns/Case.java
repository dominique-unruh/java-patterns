package de.unruh.javapatterns;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// DOCUMENT PRIORITY
public class Case<In, Return, Exn extends Throwable> {
    private final Pattern<? super In> pattern;
    private final MatchSupplier<? extends Return, Exn> action;

    @Contract(pure = true)
    public Case(@NotNull Pattern<? super In> pattern, @NotNull MatchSupplier<? extends Return, Exn> action) {
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
