package de.kaleidox.crystalshard.util.model;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface NStream<T> extends Stream<T> {
    Stream<T> unwrapNStream();

    @Override
    NStream<T> filter(Predicate<? super T> predicate);

    @Override <R> NStream<R> map(Function<? super T, ? extends R> mapper);

    @Override
    @Contract("_ -> fail")
    default IntStream mapToInt(ToIntFunction<? super T> mapper)
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    @Contract("_ -> fail")
    default LongStream mapToLong(ToLongFunction<? super T> mapper)
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    @Contract("_ -> fail")
    default DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper)
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    @Contract("_ -> fail")
    default <R> NStream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper)
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    @Contract("_ -> fail")
    default IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper)
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    @Contract("_ -> fail")
    default LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper)
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    @Contract("_ -> fail")
    default DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper)
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    NStream<T> distinct();

    @Override
    @Contract("-> fail")
    default NStream<T> sorted()
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    @Contract("_ -> fail")
    default NStream<T> sorted(Comparator<? super T> comparator)
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    NStream<T> peek(Consumer<? super T> action);

    @Override
    @Contract("_ -> fail")
    default NStream<T> limit(long maxSize) throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    NStream<T> skip(long n);

    @Override
    @Contract("_ -> fail")
    default NStream<T> takeWhile(Predicate<? super T> predicate)
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    @Contract("_ -> fail")
    default NStream<T> dropWhile(Predicate<? super T> predicate)
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    void forEach(Consumer<? super T> action);

    @Override
    void forEachOrdered(Consumer<? super T> action);

    @NotNull
    @Override
    @Contract("-> fail")
    default Object[] toArray()
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @NotNull
    @Override
    @Contract("_ -> fail")
    default <A> A[] toArray(IntFunction<A[]> generator)
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    T reduce(T identity, BinaryOperator<T> accumulator);

    @NotNull
    @Override
    Optional<T> reduce(BinaryOperator<T> accumulator);

    @Override <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner);

    @Override <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner);

    @Override <R, A> R collect(Collector<? super T, A, R> collector);

    @NotNull
    @Override
    @Contract("_ -> fail")
    default Optional<T> min(Comparator<? super T> comparator)
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @NotNull
    @Override
    @Contract("_ -> fail")
    default Optional<T> max(Comparator<? super T> comparator)
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    @Contract("-> fail")
    default long count() throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    @Contract("_ -> fail")
    default boolean anyMatch(Predicate<? super T> predicate)
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    @Contract("_ -> fail")
    default boolean allMatch(Predicate<? super T> predicate)
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @Override
    @Contract("_ -> fail")
    default boolean noneMatch(Predicate<? super T> predicate)
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @NotNull
    @Override
    @Contract("-> fail")
    default Optional<T> findFirst()
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @NotNull
    @Override
    @Contract("-> fail")
    default Optional<T> findAny()
            throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @NotNull
    @Override
    default Iterator<T> iterator() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("NStream does not have an Iterator");
    }

    @NotNull
    @Override
    default Spliterator<T> spliterator() {
        throw new UnsupportedOperationException("NStream does not have a Spliterator");
    }

    @Override
    @Contract("-> false")
    default boolean isParallel() {
        return false;
    }

    @NotNull
    @Override
    @Contract("-> fail")
    default NStream<T> sequential() throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @NotNull
    @Override
    @Contract("-> fail")
    default NStream<T> parallel() throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @NotNull
    @Override
    @Contract("-> fail")
    default NStream<T> unordered() throws FiniteOperationException {
        throw new FiniteOperationException();
    }

    @NotNull
    @Override
    NStream<T> onClose(Runnable closeHandler);

    @Override
    void close();

    final class FiniteOperationException extends UnsupportedOperationException {
        public FiniteOperationException() {
            super("Finite operations are not permitted on NStream");
        }
    }
}
