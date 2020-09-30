package de.unruh.javapatterns;

@FunctionalInterface
public interface MatchAction<Return, Exn extends Throwable> {
    Return call() throws Exn, PatternMatchReject;
}
