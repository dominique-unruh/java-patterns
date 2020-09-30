package de.unruh.javapatterns;

public class Case<In, Return, Exn extends Throwable> {
    private final Pattern<? super In> pattern;
    private final MatchSupplier<? extends Return, Exn> action;

    public Case(Pattern<? super In> pattern, MatchSupplier<? extends Return, Exn> action) {
        this.pattern = pattern;
        this.action = action;
    }

    PatternResult<Return> apply(MatchManager mgr, In t) throws Exn {
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
