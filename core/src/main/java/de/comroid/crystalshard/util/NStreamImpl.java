package de.comroid.crystalshard.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import de.comroid.crystalshard.util.model.NStream;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public class NStreamImpl<T> implements NStream<T>, Consumer<T> {
    private final Collection<T> values;

    private final Collection<Runnable> closeHandlers;
    private Collection<T> earlies;

    private ContinuationWrapper<?> continuationWrapper;

    public NStreamImpl() {
        values = new LinkedList<>();

        closeHandlers = new ArrayList<>();
        earlies = new LinkedList<>();
    }

    private NStreamImpl(Collection<T> elements) {
        this();

        elements.forEach(this);
    }

    @Override
    public void accept(T value) {
        values.add(value);

        if (continuationWrapper != null)
            continuationWrapper.accept(value);
        else earlies.add(value);
    }

    @Override
    public synchronized Stream<T> unwrapNStream() {
        Stream<T> yield = values.stream();
        values.clear();

        return yield;
    }

    @Override
    public NStream<T> filter(Predicate<? super T> predicate) {
        continuationWrapper = new ContinuationWrapper<T>(this, predicate);
        replay();

        return (NStream<T>) continuationWrapper.nStream();
    }

    @Override
    public <R> NStream<R> map(Function<? super T, ? extends R> mapper) {
        continuationWrapper = new ContinuationWrapper<R>(this, mapper);
        replay();

        return (NStream<R>) continuationWrapper.nStream();
    }

    @Override
    public NStream<T> distinct() {
        continuationWrapper = new ContinuationWrapper<T>(this, new DistinctionPredicate<>());
        replay();

        return (NStream<T>) continuationWrapper.nStream();
    }

    @Override
    public NStream<T> peek(Consumer<? super T> action) {
        continuationWrapper = new ContinuationWrapper<T>(this, action);
        replay();

        return (NStream<T>) continuationWrapper.nStream();
    }

    @Override
    public NStream<T> skip(long n) {
        continuationWrapper = new ContinuationWrapper<T>(this, new SkipPredicate<>(n));
        replay();

        return (NStream<T>) continuationWrapper.nStream();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        forEachOrdered(action);
    }

    @Override
    public void forEachOrdered(Consumer<? super T> action) {
        continuationWrapper = new ContinuationWrapper<T>(this, action);
    }

    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return null; // todo
    }

    @Override
    public @NotNull Optional<T> reduce(BinaryOperator<T> accumulator) {
        return Optional.empty(); // todo
    }

    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return null; // Todo
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return null; // todo
    }

    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return null; // todo
    }

    @Override
    public @NotNull NStream<T> onClose(Runnable closeHandler) {
        closeHandlers.add(closeHandler);

        // todo

        return this;
    }

    @Override
    public void close() {
        // todo

        continuationWrapper = null;

        closeHandlers.forEach(Runnable::run);
    }

    private void replay() {
        if (earlies == null)
            throw new IllegalStateException("Cannot reuse old NStream");

        earlies.forEach(this);

        // once replayed, there is always a ContinuationWrapper
        earlies = null;
    }

    public static void main(String[] args) {
        NStreamImpl<String> stream = new NStreamImpl<>();

        ScheduledExecutorService service = Executors.newScheduledThreadPool(5);

        /*
        service.schedule(() -> stream.accept("abc"), 2, TimeUnit.SECONDS);
        service.schedule(() -> stream.accept("def"), 4, TimeUnit.SECONDS);
        service.schedule(() -> stream.accept("abc"), 6, TimeUnit.SECONDS);
        service.schedule(() -> stream.accept("ghi"), 8, TimeUnit.SECONDS);
         */

        stream.accept("first-a");

        stream.peek(System.out::println)
                .distinct()
                .filter(str -> str.contains("a") || str.contains("h") || str.contains("f"))
                .map(String::toCharArray)
                .map(Arrays::toString)
                .forEach(System.out::println);

        //stream.accept("second-a");

        System.out.println("fuck you");
    }

    class ContinuationWrapper<R> implements Consumer<T> {
        private final NStreamImpl<T> base;

        private Function<? super T, ? extends R> mapper;
        private Predicate<? super T> filter;
        private Consumer<? super T> consumer;

        private NStreamImpl<R> underlying;

        private ContinuationWrapper(NStreamImpl<T> base) {
            this.base = base;
        }

        ContinuationWrapper(NStreamImpl<T> base, Function<? super T, ? extends R> mapper) {
            this(base);

            this.mapper = mapper;
        }

        ContinuationWrapper(NStreamImpl<T> base, Predicate<? super T> filter) {
            this(base);

            this.filter = filter;
        }

        ContinuationWrapper(NStreamImpl<T> base, Consumer<? super T> consumer) {
            this(base);

            this.consumer = consumer;
        }

        @Override
        public void accept(T value) {
            if (underlying != null && underlying.continuationWrapper != null) {
                append(value);
            } else accumulate(value);

            /*
            todo

            in this else, we need to distinct between the final stream action and
            an element thatwas added before the complete stream was built.
             */
        }

        R append(T value) {
            R accumulate = accumulate(value);

            if (filter == null || accumulate != null) {
                base.values.remove(value);
                underlying.accept(accumulate);
            } else {
                // in filter mode, skip nulls

                base.values.remove(value);
            }

            return accumulate;
        }

        NStream<R> nStream() {
            Collection<R> yields = new LinkedList<>();

            for (T value : base.values) {
                R accumulate = accumulate(value);

                if (filter == null || accumulate != null) {
                    yields.add(accumulate);
                    base.values.remove(value);
                } else {
                    // in filter mode, skip nulls

                    base.values.remove(value);
                }

            }

            return (underlying = new NStreamImpl<>(yields));
        }

        @SuppressWarnings("unchecked")
        private R accumulate(T value) {
            if (mapper != null)
                return mapper.apply(value);

            if (filter != null)
                return filter.test(value) ? (R) value : null;

            if (consumer != null) {
                consumer.accept(value);
                return (R) value;
            }

            throw new AssertionError("Unreachable Statement Exception");
        }
    }

    static class DistinctionPredicate<T> implements Predicate<T> {
        private final Collection<T> values = new LinkedList<>();

        @Override
        public boolean test(T value) {
            boolean yield = !values.contains(value);

            values.add(value);

            return yield;
        }
    }

    static class SkipPredicate<T> implements Predicate<T> {
        private final long skip;

        private long counted;

        SkipPredicate(long skip) {
            this.skip = skip;

            counted = 0;
        }

        @Override
        public boolean test(T t) {
            return skip < ++counted;
        }
    }
}
