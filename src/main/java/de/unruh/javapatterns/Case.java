package de.unruh.javapatterns;

import java.util.concurrent.Callable;

public class Case<In, Return, Exn extends Throwable> {
    private final Pattern<? super In> pattern;
    private final MatchAction<? extends Return, Exn> action;

    public Case(Pattern<? super In> pattern, MatchAction<? extends Return, Exn> action) {
        this.pattern = pattern;
        this.action = action;
    }

    PatternResult<Return> apply(MatchManager mgr, In t) throws Exn {
//              out.println("Starting case");
        try {
            pattern.apply(mgr, t);
            // PatternMatchReject here will also work to try the next match
            return new PatternResultSome<>(action.call());
        } catch (PatternMatchReject e) {
//                  out.println("Case "+pattern+" fail: "+t);
            return new PatternResultNone<>();
        } finally {
            mgr.clearCaptured();
        }
    }


}
