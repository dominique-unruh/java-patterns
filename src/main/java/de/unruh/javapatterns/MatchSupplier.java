package de.unruh.javapatterns;

// DOCUMENT
@FunctionalInterface
public interface MatchSupplier<Return, Exn extends Throwable> {
    Return call() throws Exn, PatternMatchReject;
}
