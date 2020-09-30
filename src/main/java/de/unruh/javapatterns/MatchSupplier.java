package de.unruh.javapatterns;

@FunctionalInterface
public interface MatchSupplier<Return, Exn extends Throwable> {
    Return call() throws Exn, PatternMatchReject;
}
