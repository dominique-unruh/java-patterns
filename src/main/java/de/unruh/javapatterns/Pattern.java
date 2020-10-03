package de.unruh.javapatterns;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A pattern that matches a value and assigns values to capture variables ({@link Capture}).
 *
 * When implementing a pattern, the central method is {@link #apply apply} which takes a value {@code value} (and a {@link MatchManager} {@code mgr}) and performs
 * the actual pattern matching. Inside {@link #apply apply}, we can perform the following operations:
 * <ul>
 * <li>It can analyze and decompose {@code value} (or compute arbitrary derived values).</li>
 * <li>It can reject {@code value} by invoking {@link #reject()}.</li>
 * <li>It can invoke the {@link #apply apply} function of other patterns on the values obtained by analyzing {@code value}. (Typically, those would be given as arguments
 *     when constructing this pattern.) If any of the subpatterns fail, the this pattern fails, too (unless this is
 *     caught via a protected block (see below).</li>
 * <li>It can invoke {@code mgr}.{@link MatchManager#protectedBlock protectedBlock} to execute a protected block of actions (see below).</li>
 * </ul>
 * <p>
 *
 * <b>Some important notes:</b>
 * <ul>
 * <li>There is no explicit mechanism for assigning capture variables. However,
 *     capture variables are patterns themselves, and thus can be passed to a pattern as subpatterns.
 *     If {@code x} is a capture, then <code>x.{@link #apply apply}(...)</code> will have the effect of
 *     assigning that capture. </li>
 * <li>Failure is marked via an exception {@link PatternMatchReject} (thrown by {@link #reject()}}.
 *     This exception should never be caught because subpatterns might have assigned values to some
 *     capture variables already and it would be undefined which capture variables would be assigned
 *     and which not. Instead, if we want to apply a subpattern but not fail if the subpattern fails,
 *     we need to execute the pattern in a protected block by invoking
 *     <code>mgr.{@link MatchManager#protectedBlock(PatternRunnable) protectedBlock}}(...)</code>. This
 *     protected block then returns {@code false} if the subpattern(s) in {@code ...} fail.
 *     </li>
 * <li>When invoking patterns that were passed as arguments when constructing this pattern,
 *     it is recommended to do so in the order were given. This is to ensure that captures are assigned
 *     in the right order. For example, if two patterns {@code p1} and {@code p2} are given,
 *     and {@code p1} assigns a capture {@code x}, then {@code p2} may already read its assigned value.
 *     (E.g., <code>{@link Patterns#Array Array}(x, {@link Patterns#Is(Capture) Is}(x))</code>
 *     will work but
 *     <code>{@link Patterns#Array Array}({@link Patterns#Is(Capture) Is}(x), x)</code>
 *     will not.) If the patterns are invoked in a different order, this should be documented clearly. </li>
 * <li>If a patten fails, it does not matter at which point it does it. E.g.,
 *     "<code>subpattern.{@link #apply apply}(...); if (...) {@link #reject()}</code>" and
 *     "<code>if (...) {@link #reject()}; subpattern.{@link #apply apply}(...)</code>" are equivalent.
 *     (Unless the subpattern has any additional side-effects besides assigning captures.)
 *     All captures that were assigned by this pattern or subpatterns will be reset upon failure.</li>
 * <li>Patterns should not have any side-effects.</li>
 * <li>When taking subpatterns as arguments, those should have types of the form
 * {@code Pattern<? super X>} for some type {@code X}, not {@code Pattern<X>}.
 * (This means, {@code Pattern} should be treated as a contravariant type constructor.)</li>
 * </ul>
 * <p>
 *
 * <b>Example 1:</b>
 * As an example for designing a pattern, consider the following use case. Assume a class {@code Pair<X,Y>}
 * with two members {@code X first} and {@code Y second}. We want to construct a pattern for this class. For this,
 * we create a static method (by convention, of the same name as {@code Pair})
 * that returns an anonymous subclass of {@code Pattern<Pair<X,Y>>}:
 * <pre>
 *{@code static <X, Y> Pattern<Pair<X, Y>> Pair(Pattern<? super X> firstPattern, Pattern<? super Y> secondPattern)} {
 *    {@code return new Pattern<>()} {
 *        {@code public void apply(MatchManager mgr, Pair<X, Y> value) throws PatternMatchReject} {
 *             if (value == null) reject();
 *             firstPattern.apply(mgr, value.first);
 *             secondPattern.apply(mgr, value.second);
 *         }
 *
 *         public String toString() {
 *             return "Pair(" + firstPattern + "," + secondPattern + ")";
 *         }
 *     };
 * }
 * </pre>
 * Most of this is boilerplate. The crucial part are the three lines in the definition of {@code apply}:
 * {@code if (value == null) reject();} rejects the matched value if it is {@code null}.
 * And {@code firstPattern.apply(mgr, value.first);} matches the first component of the matched
 * value using the subpattern {@code subpattern}. And analogously for the next line and the second component. <p>
 *
 * The full example can be found in
 * <a href="https://github.com/dominique-unruh/java-patterns/blob/911c12d0f5bd25f8271d705aa6c87377944336f2/src/test/java/de/unruh/javapatterns/test/PatternDocumentationTest.java#L40">PatternDocumentationTest.java</a>,
 * test case {@code example1}. <p>
 *
 * <b>Example 2:</b> As an example for a pattern that does not correspond to an existing class such
 * as {@code Pair}, we consider the following use case. We want a pattern that matches strings of
 * the form "firstname lastname" (and allows us to apply further subpatterns to the first and last name).
 * The following method achieves this:
 * <pre>
 *{@code static Pattern<String> FullName(Pattern<String> firstNamePattern, Pattern<String> lastNamePattern)} {
 *    {@code return new Pattern<>()} {
 *         public void apply(MatchManager mgr, String value) throws PatternMatchReject {
 *             if (value == null) reject();
 *             String[] parts = value.split(" ");
 *             if (parts.length != 2) reject();
 *             firstNamePattern.apply(mgr, parts[0]);
 *             lastNamePattern.apply(mgr, parts[1]);
 *         }
 *
 *         public String toString() {
 *             return "FullName(" + firstNamePattern + "," + lastNamePattern + ")";
 *         }
 *     };
 * }
 * </pre>
 * Here the {@code apply} method checks whether the matched value is non-null and consists of
 * two words (we do not handle names with more than two components) and rejects otherwise.
 * Then it applies the two subpatterns to the first and second word of the string, respectively. <p>
 *
 * The full example can be found in
 * <a href="https://github.com/dominique-unruh/java-patterns/blob/911c12d0f5bd25f8271d705aa6c87377944336f2/src/test/java/de/unruh/javapatterns/test/PatternDocumentationTest.java#L72">PatternDocumentationTest.java</a>,
 * test case {@code example2}.
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

    /** Creates a new capture variable.
     *
     * @param name Name of the capture. Used only for informative purposes
     *             (printing patterns, error messages). It is recommended
     *             that this is the name of the variable holds this capture.
     * @return the capture variable
     */
    @Contract(pure = true, value = "_ -> new")
    public static <T> Capture<T> capture(@NotNull String name) {
        return new Capture<T>(name);
    }
}
