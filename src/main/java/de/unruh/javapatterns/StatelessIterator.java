package de.unruh.javapatterns;

import com.google.common.collect.MapMaker;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

/** Iterator with effectively stateless access (wrapping a stateful iterator). <p>
 *
 * Given an {@link Iterator} {@code it}, we can construct a stateless iterator as {@code slit = StatelessIterator.from(it)}.
 * {@code slit} then allows access to the elements of {@code it} in an effectively stateless way.
 * (Effectively stateless means that {@code slit} seems to be stateless, but internally it has state to
 * enable lazy computation of the values contained in the iterator.) <p>
 *
 * From outside, {@code slit} looks like a linked list, i.e., {@code slit.}{@link #getHead()} returns the first element
 * of the original iterator, and {@code slit.}{@link #getTail()} returns a new {@code StatelessIterator}
 * starting at the second element of the original iterator. It is possible to invoke {@link #getHead()} and
 * {@link #getTail()} repeatedly (and concurrently on different threads), and they will always give the same result.
 * Thus, a {@code StatelessIterator} can be shared after creation and accesses to different stateless iterators will
 * not interfere with each other. <p>
 *
 * A stateless iterator is created from a regular {@link Iterator} using {@link #from}. The regular iterator is consumed
 * in the sense that after the invocation of {@link #from}, it is in an unspecified state and must not be accessed
 * any more. (The stateless iterator will produce incorrect results if the regular iterator is accessed after creation
 * of the stateless iterator.) <p>
 *
 * One exception to this rule is to pass the same {@link Iterator} instance several times to {@link #fromShared}.
 * This is permitted, and it will lead in stateless iterators with the same content as the {@link Iterator} had
 * upon the first invocation of {@link #fromShared}. (Thus, if an iterator should be shared between different functions/threads,
 * it is possible to do so as long as all functions/threads apply {@link #fromShared} to the iterator and only operate on
 * the result.) <p>
 *
 * DOCUMENT: all the above works also with {@link Stream}s.<p>
 *
 * DOCUMENT: in links above, don't show the argument types<p>
 *
 * <b>About garbage collection:</b> If an iterator or stream {@code it} is converted into a stateless iterator {@code slit}, then {@code slit}
 * will have to cache the values from {@code it} if they are needed again. With iterators or streams that iterate over a large
 * number of elements, this can potentially result in a large number of elements that cannot be garbage collected.
 * The following rules establish which elements cannot be garbage collected:
 * <ul>
 * <li>Consider all <i>still-referenced</i> stateless iterators that were derived from {@code it} (via
 * {@link #from from(it)}, {@link #fromShared fromShared(it)}, and
 * iterated applications of {@link #getTail()}).
 * Each of these refers to some element of the iterator (that element would be returned by {@link #getHead()}).
 * Let {@code low} denote the first of these (in the order in which they are originally contained in {@code it}).
 * <li>Consider all (not necessarily stil-referenced) stateless iterators derived from {@code it}.
 * Each of these refers to some element of the iterator.
 * Let {@code high} denote the last of these. Let {@code start} denote the first element in {@code it}.
 * <li>If {@code it} is an {@link Iterator}, or if {@code it} is a {@link Stream} but {@code it.iterator()}
 * returns {@code it} (i.e., {@code it.iterator()} is just implemented as a type case), we call {@code it}
 * "direct". Otherwise, we call {@code it} "indirect".
 * <li>If one of the stateless iterators derived from {@code it} returned {@code false} on a {@link #nonEmpty()} call,
 * we say {@code it} is "drained".</li>
 * <li>If {@code it} was converted into a stateless iterator using {@link #from from} and some of the stateless iterators
 * are still referenced: Then {@code low},...,{@code high}
 * cannot be reclaimed. And {@code it} cannot be collected unless it was drained.
 * <li>If {@code it} was converted into a stateless iterator using {@link #from from} and none of the stateless iterators
 * are still referenced: All cached values and {@code it} can be reclaimed.
 * <li>If {@code it} is direct and was converted into a stateless iterator using {@link #fromShared fromShared} and
 * {@link #forget forget}{@code (it)} was not invoked, and {@code it} <i>or</i> one of the stateless iterators are
 * still referenced: Then {@code start},...,{@code high} cannot be reclaimed. And {@code it} cannot be reclaimed.
 * <li>If {@code it} is indirect and was converted into a stateless iterator using {@link #fromShared fromShared} and
 * {@link #forget forget}{@code (it)} was not invoked, and {@code it} is
 * still referenced: Then {@code start},...,{@code high} cannot be reclaimed. And {@code it} cannot be reclaimed.
 * <li>If {@code it} is direct and was converted into a stateless iterator using {@link #fromShared fromShared} and
 * {@link #forget forget}{@code (it)} was not invoked, and {@code it} <i>and</i> the stateless iterators are not
 * referenced any more:
 * Then all cached values and {@code it} can be reclaimed.
 * <li>If {@code it} is indirect and was converted into a stateless iterator using {@link #fromShared fromShared} and
 * {@link #forget forget}{@code (it)} was not invoked, and {@code it} is not
 * referenced any more:
 * Then all cached values and {@code it} can be reclaimed.
 * <li> If {@code it} was converted into a stateless iterator using {@link #fromShared fromShared} and
 * {@link #forget forget}{@code (it)} was invoked: The same rules apply as if {@code it} was converted using {@link #from from}
 * (see above).
 * </ul>
 * The <b>rule of thumb</b> from these rules: Given an iterator {@code it} that produces a lot of elements, if possible
 * use {@link #from} and not {@link #fromShared}. (Or if that is not possible, apply {@link #forget} as soon as possible.)
 * And avoid keeping references to the original iterator. Given a stream {@code it} that produces a lot of elements,
 * avoid keeping references to the original stream.<p>
 *
 * Notes:
 * <ul>
 * <li>Despite its name, a {@code StatelessIterator} is not a subclass of {@link Iterator} since the {@link Iterator}
 * interface is inherently stateful. However, one can get an {@link Iterator} from it via {@link #iterator()}
 * </ul>
 *
 * @param <T> type of the iterator elements
 */
public class StatelessIterator<T> implements Iterable<T> {
    @NotNull private final static ConcurrentMap<Object, StatelessIterator<?>> iterators =
            new MapMaker().weakKeys().concurrencyLevel(1).makeMap();

    // DOCUMENT
    @NotNull public static <T> StatelessIterator<T> fromShared(@NotNull Iterator<T> iterator) {
        if (iterator instanceof DefaultCloneableIterator)
            return ((DefaultCloneableIterator<T>)iterator).getStatelessIterator();
        //noinspection unchecked
        return (StatelessIterator<T>) iterators.computeIfAbsent(iterator, i -> new StatelessIterator<>(iterator));
    }

    // DOCUMENT
    @NotNull public static <T> StatelessIterator<T> fromShared(@NotNull Stream<T> stream) {
        //noinspection unchecked
        return (StatelessIterator<T>) iterators.computeIfAbsent(stream, i -> new StatelessIterator<T>(stream.iterator()));
    }

    // DOCUMENT
    public static void forget(@NotNull Iterator<?> iterator) {
        iterators.remove(iterator);
    }

    // DOCUMENT
    public static void forget(@NotNull Stream<?> stream) {
        iterators.remove(stream);
    }

    // DOCUMENT
    @NotNull public static <T> StatelessIterator<T> from(@NotNull Iterator<T> iterator) {
        if (iterator instanceof DefaultCloneableIterator)
            return ((DefaultCloneableIterator<T>)iterator).getStatelessIterator();
        return new StatelessIterator<>(iterator);
    }

    // DOCUMENT
    @NotNull public static <T> StatelessIterator<T> from(@NotNull Stream<T> stream) {
        return new StatelessIterator<T>(stream.iterator());
    }

    private enum State { uninitialized, empty, nonEmpty }

    private volatile State state = State.uninitialized;
    private Iterator<T> iterator;
    private T head = null;
    private StatelessIterator<T> tail = null;

    private StatelessIterator(@NotNull Iterator<T> iterator) {
        this.iterator = iterator;
    }

    private synchronized void initialize() {
        if (state==State.uninitialized) {
            if (iterator.hasNext()) {
                head = iterator.next();
                tail = new StatelessIterator<>(iterator);
                state = State.nonEmpty;
            } else {
                iterator = null;
                state = State.empty;
            }
        }
    }

    // DOCUMENT
    public T getHead() {
        if (state==State.uninitialized)
            initialize();
        if (state==State.empty)
            throw new NoSuchElementException();
        return head;
    }

    // DOCUMENT
    @NotNull public StatelessIterator<T> getTail() {
        if (state==State.uninitialized)
            initialize();
        if (state==State.empty)
            throw new NoSuchElementException();
        return tail;
    }

    // DOCUMENT
    public boolean nonEmpty() {
        if (state==State.uninitialized)
            initialize();
        return state == State.nonEmpty;
    }

    // DOCUMENT
    @Override
    @Contract(pure = true, value = "-> new")
    public @NotNull DefaultCloneableIterator<T> iterator() {
        return DefaultCloneableIterator.from(this);
    }
}
