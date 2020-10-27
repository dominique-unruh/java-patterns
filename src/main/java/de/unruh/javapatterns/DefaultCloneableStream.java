package de.unruh.javapatterns;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.*;

public class DefaultCloneableStream<T> implements CloneableStream<T> {
    @NotNull
    private StatelessIterator<T> statelessIterator;

    // DOCUMENT
    @NotNull public StatelessIterator<T> getStatelessIterator() {
        return statelessIterator;
    }

    // DOCUMENT
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public DefaultCloneableStream<T> clone() {
        return new DefaultCloneableStream<>(statelessIterator);
    }

    private DefaultCloneableStream(@NotNull StatelessIterator<T> statelessIterator) {
        this.statelessIterator = statelessIterator;
    }

    // DOCUMENT
    @NotNull public static <T> DefaultCloneableStream<T> from(@NotNull StatelessIterator<T> iterator) {
        return new DefaultCloneableStream<>(iterator);
    }

    // DOCUMENT
    @NotNull public static <T> DefaultCloneableStream<T> from(Stream<T> stream) {
        return new DefaultCloneableStream<>(StatelessIterator.from(stream));
    }

    // DOCUMENT
    @NotNull public static <T> DefaultCloneableStream<T> fromShared(Stream<T> stream) {
        return new DefaultCloneableStream<>(StatelessIterator.fromShared(stream));
    }

    private Stream<T> regularStream() {
        Stream<T> stream = StreamSupport.stream(spliterator(), false);
        statelessIterator = StatelessIterator.empty(); // TODO
        return stream;
    };

    @Override
    public Stream<T> filter(Predicate<? super T> predicate) {
        return regularStream().filter(predicate);
    }

    @Override
    public <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
        return regularStream().map(mapper);
    }

    @Override
    public IntStream mapToInt(ToIntFunction<? super T> mapper) {
        return regularStream().mapToInt(mapper);
    }

    @Override
    public LongStream mapToLong(ToLongFunction<? super T> mapper) {
        return regularStream().mapToLong(mapper);
    }

    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        return regularStream().mapToDouble(mapper);
    }

    @Override
    public <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return regularStream().flatMap(mapper);
    }

    @Override
    public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        return regularStream().flatMapToInt(mapper);
    }

    @Override
    public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        return regularStream().flatMapToLong(mapper);
    }

    @Override
    public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        return regularStream().flatMapToDouble(mapper);
    }

    @Override
    public Stream<T> distinct() {
        return regularStream().distinct();
    }

    @Override
    public Stream<T> sorted() {
        return regularStream().sorted();
    }

    @Override
    public Stream<T> sorted(Comparator<? super T> comparator) {
        return regularStream().sorted(comparator);
    }

    @Override
    public Stream<T> peek(Consumer<? super T> action) {
        return regularStream().peek(action);
    }

    @Override
    public Stream<T> limit(long maxSize) {
        return regularStream().limit(maxSize);
    }

    @Override
    public Stream<T> skip(long n) {
        for (long i=0; i<=n; i++) {
            if (!statelessIterator.nonEmpty()) break;
            statelessIterator = statelessIterator.getTail();
        }
        return this;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        while (statelessIterator.nonEmpty()) {
            action.accept(statelessIterator.getHead());
            statelessIterator = statelessIterator.getTail();
        }
    }

    @Override
    public void forEachOrdered(Consumer<? super T> action) {
        forEach(action);
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return regularStream().toArray();
    }

    @NotNull
    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        return regularStream().toArray(generator);
    }

    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return regularStream().reduce(identity, accumulator);
    }

    @NotNull
    @Override
    public Optional<T> reduce(BinaryOperator<T> accumulator) {
        return regularStream().reduce(accumulator);
    }

    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return regularStream().reduce(identity, accumulator, combiner);
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return regularStream().collect(supplier, accumulator, combiner);
    }

    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return regularStream().collect(collector);
    }

    @NotNull
    @Override
    public Optional<T> min(Comparator<? super T> comparator) {
        return regularStream().min(comparator);
    }

    @NotNull
    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        return regularStream().max(comparator);
    }

    @Override
    public long count() {
        return regularStream().count();
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        return regularStream().anyMatch(predicate);
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        return regularStream().allMatch(predicate);
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        return regularStream().noneMatch(predicate);
    }

    @NotNull
    @Override
    public Optional<T> findFirst() {
        if (statelessIterator.nonEmpty()) return Optional.of(statelessIterator.getHead());
        else return Optional.empty();
    }

    @NotNull
    @Override
    public Optional<T> findAny() {
        return findFirst();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return DefaultCloneableIterator.from(statelessIterator);
    }

    @NotNull
    @Override
    public Spliterator<T> spliterator() {
        Iterable<T> iterable = this::iterator;
        return iterable.spliterator();
    }

    @Override
    public boolean isParallel() {
        return false;
    }

    @NotNull
    @Override
    public Stream<T> sequential() {
        return this;
    }

    @NotNull
    @Override
    public Stream<T> parallel() {
        return this;
    }

    @NotNull
    @Override
    public Stream<T> unordered() {
        return this;
    }

    @NotNull
    @Override
    public Stream<T> onClose(Runnable closeHandler) {
        return regularStream().onClose(closeHandler);
    }

    @Override
    public void close() {}
}
