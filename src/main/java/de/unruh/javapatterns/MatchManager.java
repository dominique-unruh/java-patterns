package de.unruh.javapatterns;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;

/** Manages the state of captured variables in a pattern match. <p>
 *
 * This class should never be used outside of {@link Pattern#apply}, see there.
 */
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

    /** Executes a proctected block during a pattern match.<p>
     *
     * {@code block} is executed. If {@code block} executes successfully,
     * {@code protectedBlock} returns {@code true}. If {@code block} throws
     * a {@link PatternMatchReject} exception (by calling {@link Pattern#reject()})
     * then {@code protectedBlock} returns {@code false} and resets all
     * captured variables to their state at the beginning of the invocation.
     *
     * @param block a lambda expression of the form {@code () -> ...}. The code
     *              to be executed.
     * @return {@code true} if {@code block} succeeded, {@code false} if
     *         {@code block} threw a {@link PatternMatchReject} exception
     */
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
