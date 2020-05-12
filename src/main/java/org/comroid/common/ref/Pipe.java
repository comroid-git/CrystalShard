package org.comroid.common.ref;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

public interface Pipe<T> {
    static <T> Pipe<T> of(T... firstInput) {
        return Support
    }

    Pipe<T> filter(Predicate<? super T> predicate);

    <R> Pipe<R> map(Function<? super T, ? extends R> mapper);

    <R> Pipe<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);

    Pipe<T> distinct();

    Pipe<T> sorted();

    Pipe<T> sorted(Comparator<? super T> comparator);

    Pipe<T> peek(Consumer<? super T> action);

    Pipe<T> limit(long maxSize);

    Pipe<T> skip(long n);

    void forEach(Consumer<? super T> action);

    void forEachOrdered(Consumer<? super T> action);

    @NotNull
    Object[] toArray();

    @NotNull <A> A[] toArray(IntFunction<A[]> generator);

    <R, A> R collect(Collector<? super T, A, R> collector);

    @NotNull
    Optional<T> min(Comparator<? super T> comparator);

    @NotNull
    Optional<T> max(Comparator<? super T> comparator);

    long count();

    boolean anyMatch(Predicate<? super T> predicate);

    boolean allMatch(Predicate<? super T> predicate);

    boolean noneMatch(Predicate<? super T> predicate);

    @NotNull
    Optional<T> findFirst();

    @NotNull
    Optional<T> findAny();
}
