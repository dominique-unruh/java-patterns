package de.unruh.javapatterns;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Predicate;

// DOCUMENT
public final class MatchManager {
    // Making this package private
    @Contract(pure = true)
    MatchManager() {}

    @NotNull
    private final Deque<Capture<?>> captured = new ArrayDeque<>(10);

    <T> void assigned(@NotNull Capture<T> x) {
        captured.add(x);
    }

    void clearCaptured() {
        for (Capture<?> capture : captured)
            capture.clear();
        captured.clear();
    }

    // DOCUMENT PRIORITY
    public boolean protectedBlock(@NotNull PatternRunnable block) {
        int size = captured.size();
        try {
            block.run();
        } catch (PatternMatchReject e) {
            while (captured.size() > size) {
                Capture<?> capture = captured.pop();
                capture.clear();
            }
            return false;
        }
        return true;
    }

/*    @Nullable
    public <T> T excursion(@NotNull PatternSupplier<T> excursion, @NotNull Predicate<T> shouldReset, @Nullable T failValue) {
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
    }*/
}
