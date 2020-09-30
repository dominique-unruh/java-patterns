package de.unruh.javapatterns;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Predicate;

// DOCUMENT
public final class MatchManager {
    // Making this package private
    MatchManager() {}

    Deque<Capture<?>> captured = new ArrayDeque<>(10);

    <T> void assigned(Capture<T> x) {
        captured.add(x);
    }

    void clearCaptured() {
        for (Capture<?> capture : captured)
            capture.clear();
        captured.clear();
    }

    public boolean excursion(PatternRunnable runnable) {
        return excursion(() -> { runnable.run(); return true; }, x -> true, false);
    }


    public <T> T excursion(PatternSupplier<T> excursion, Predicate<T> shouldReset, T failValue) {
        int size = captured.size();
        T result = failValue;
        try {
            result = excursion.get();
            if (shouldReset.test(result)) throw new PatternMatchReject();
        } catch (PatternMatchReject e) {
            while (captured.size() > size) {
                Capture<?> capture = captured.pop();
                capture.clear();
            }
        }
        return result;
    }
}
