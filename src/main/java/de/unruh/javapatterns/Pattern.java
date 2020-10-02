package de.unruh.javapatterns;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A pattern that matches a value and assigns values to capture variables ({@link Capture}).
 *
 * When implementing a pattern, the central method is {@link #apply apply} which takes a value {@code value} (and a {@link MatchManager} @{code mgr}) and performs
 * the actual pattern matching. Inside {@link #apply apply}, we can perform the following operations:
 * <ul>
 * <li>It can analyze and decompose {@code value} (or compute arbitrary derived values).</li>
 * <li>It can reject {@code value} by invoking {@link #reject()}.</li>
 * <li>It can invoke the {@link #apply apply} function of other patterns on the values obtained by analyzing {@code value}. (Typically, those would be given as arguments
 *     when constructing this pattern.) If any of the subpatterns fail, the this pattern fails, too (unless this is
 *     caught via a protected block (see below).</li>
 * <li>It can invoke {@code mgr}.{@link MatchManager#protectedBlock excursion} to execute a protected block of actions (see below).</li>
 * </ul>
 * Some important notes:
 * <ul>
 * <li>There is no explicit mechanism for assigning capture variables. However,
 *     capture variables are patterns themselves, and thus can be passed to a pattern as subpatterns.
 *     If {@code x} is a capture, then <code>x.{@link #apply}(...)</code> will have the effect of
 *     assigning that capture. </li>
 * <li>Failure is marked via an exception {@link PatternMatchReject} (thrown by {@link #reject()}}.
 *     This exception should never be caught because subpatterns might have assigned values to some
 *     capture variables already and it would be undefined which capture variables would be assigned
 *     and which not. Instead, if we want to apply a subpattern but not fail if the subpattern fails,
 *     we need to execute the pattern in a protected block by invoking
 *     <code>mgr.{@link MatchManager#protectedBlock(PatternRunnable) excursion}}(...)</code>. This
 *     protected block then returns {@code false} if the subpattern(s) in {@code ...} fail.
 *     </li>
 * <li>When invoking patterns that were passed as arguments when constructing this pattern,
 *     it is recommended to do so in the order were given. This is to ensure that captures are assigned
 *     in the right order. For example, if two patterns {@code p1} and {@code p2} are given,
 *     and {@code p1} assigns a capture {@code x}, then {@code p2} may already read its assigned value.
 *     (E.g., <code>{@link Patterns#Array Array}(x, {@link Patterns#Is(Object) Is}(x.{@link Capture#v() v()})</code>
 *     will work but
 *     <code>{@link Patterns#Array Array}({@link Patterns#Is(Object) Is}(x.{@link Capture#v() v()}, x)</code>
 *     will not.) If the patterns are invoked in a different order, this should be documented clearly. </li>
 * <li>If a patten fails, it does not matter at which point it does it. E.g.,
 *     <code>subpattern.{@link #apply apply}(...); if (...) {@link #reject()}</code> and
 *     <code>if (...) {@link #reject()}; subpattern.{@link #apply apply}(...)</code> are equivalent.
 *     (Unless the subpattern has any additional side-effects besides assigning captures.)
 *     All captures that were assigned by this pattern or subpatterns will be reset upon failure.</li>
 * <li>Patterns should not have any side-effects.</li>
 * </ul>
 *
 * @param <T> The type of the value that is pattern-matched
 */
public abstract class Pattern<T> {
    /** Performs the pattern match. See the {@link Pattern class documentation}.<p>
     *
     * Must only be invoked from withing an {@code apply}. (I.e.,
     * a pattern may invoke {@code apply} on its subpatterns from within
     * its own {@code apply} method.)
     *
     * @param mgr the {@link MatchManager} that manages the life-cycle of the captures in this
     *            pattern match. Must be passed to subpatterns and not be kept after the termination
     *            of the invocation of {@code apply(...)}.
     * @param value the value to be pattern-matched
     * @throws PatternMatchReject to indicate that value did not match the pattern.
     *         Should be thrown only by calling {@link #reject()}}.
     */
    public abstract void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject;

/*    @Contract(pure = true, value = "-> this")
    public final <U extends T> Pattern<U> contravariance() {
        //noinspection unchecked
        return (Pattern<U>) this;
    }*/

    /** Should give a human readable representation of this pattern.
     * Used in error messages. */
    @Override
    @Contract(pure = true)
    public abstract String toString();

    /** Should be called inside {@link #apply} to indicate that the pattern did not match.<p>
     *
     * Can also be invoked inside a match action (the code that is executed if a pattern did
     * match) to declare the match as failed. If that happens, the pattern match (via {@link Match#match})
     * will continue with the next pattern.
     *
     * @throws PatternMatchReject always thrown
     */
    @Contract(pure = true, value = "-> fail")
    public static void reject() throws PatternMatchReject {
        throw new PatternMatchReject();
    }
}
