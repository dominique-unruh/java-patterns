package de.unruh.javapatterns;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.stream.Stream;

/** An iterator that can be cloned.
 * A {@code CloneableIterator it} behaves like a regular iterator,
 * but at any point (even if it has already been partially or completely consumed) {@code it} can be cloned
 * via {@code it2 = it.}{@link #clone()}.
 * Both the original {@code it} and the cloned {@code it2} will then iterate over the same remaining elements,
 * and access to one of {@code it, it2} does not influence the other.
 */
public interface CloneableIterator<T> extends Iterator<T>, Cloneable {
    CloneableIterator<T> clone();

    /** Creates a {@link CloneableIterator} from a regular {@link Iterator} {@code it}.
     * It is allowed to apply this to an iterator that has already been partially or completely consumed.
     * But after invoking {@code from(iterator)}, {@code iterator} must not be accessed any more.
     *
     * @param iterator the iterator to be transformed into a {@link CloneableIterator}. Must not be accessed any
     *                 more after invoking {@code from}.
     * @return a {@link CloneableIterator} that iterates over the same elements that {@code iterator would
     *         have iterated over.
     */
    // DOCUMENT reference GC issues
    @NotNull
    static <T> CloneableIterator<T> from(Iterator<T> iterator) {
        if (iterator instanceof CloneableIterator)
            return (CloneableIterator<T>)iterator;
        return DefaultCloneableIterator.from(iterator);
    }

    /** Like {@link #from(Iterator)}, except that the same iterator can be passed to several invocations of
     * {@code fromShared}. Furthermore, an iterator passed to {@code fromShared} can also be passed afterwards to
     * {@link #from(Iterator)} (but only once). But the original iterator may not be accessed any more after the
     * first {@code fromShared} invocation.
     *
     * All {@code fromShared} and @{code from} invocations return {@link CloneableIterator}s that return the elements
     * the original iterator contained at the time of the first invocation of {@code fromShared} / @{code from}.
     * (I.e., access to the cloneable iterators does not influence newly create cloneable iterators.)
     *
     * For example, the following are valid sequences of operations:
     * <ul>
     * <li><pre>it1 = fromShared(it); e1 = it1.next(); it1 = fromShared(it); e2 = it2.next();</pre>
     * Then {@code e1} and {@code e2} will contain the same element. (Assuming an ordered iterator.
     * Otherwise it is only guaranteed that draining {@code it1, it2} returns the same elements
     * but not necessarily in the same order.)
     * <li><pre>it1 = fromShared(it); e1 = it1.next(); it1 = from(it); e2 = it2.next();</pre>
     * Same situation as in the previous example.
     * </ul>
     *
     * Examples of invalid sequences of operations are:
     * <ul>
     * <li><pre>from(it); fromShared(it);</pre>
     * <li><pre>fromShared(it); fromShared(it); it.next();</pre>
     * </ul>
     * @param iterator the original iterator
     * @return the cloneable iterator containing the same elements as {@code iterator}.
     */
    @NotNull
    static <T> CloneableIterator<T> fromShared(Iterator<T> iterator) {
        if (iterator instanceof CloneableIterator)
            return ((CloneableIterator<T>) iterator).clone();
        return DefaultCloneableIterator.fromShared(iterator);
    }


    // DOCUMENT
    @NotNull
    static <T> CloneableIterator<T> from(Stream<T> stream) {
        return DefaultCloneableIterator.from(stream);
    }

    // DOCUMENT
    @NotNull
    static <T> CloneableIterator<T> fromShared(Stream<T> stream) {
        return DefaultCloneableIterator.fromShared(stream);
    }
}
