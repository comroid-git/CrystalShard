package de.kaleidox.util;

import de.kaleidox.crystalshard.internal.core.concurrent.ThreadPool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class represents an extension to the CompletableFuture class.
 * For async methods, it will always use a bot-own thread for asyncronity.
 *
 * @param <T> The type variable of the Item.
 */
public class CompletableFutureExtended<T> extends CompletableFuture<T> {
    private final ThreadPool threadPool;

    public CompletableFutureExtended(ThreadPool threadPool) {
        super();
        this.threadPool = threadPool;
    }

    @Override
    public <U> CompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> fn) {
        var ref = new Object() {
            CompletableFuture<U> future = new CompletableFuture<>();
        };
        threadPool.execute(() -> ref.future = super.thenApply(fn));
        return ref.future;
    }

    @Override
    public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action) {
        var ref = new Object() {
            CompletableFuture<Void> future = new CompletableFuture<>();
        };
        threadPool.execute(() -> ref.future = super.thenAccept(action));
        return ref.future;
    }

    @Override
    public <U, V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn) {
        var ref = new Object() {
            CompletableFuture<V> future = new CompletableFuture<>();
        };
        threadPool.execute(() -> ref.future = super.thenCombine(other, fn));
        return ref.future;
    }

    @Override
    public <U> CompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action) {
        var ref = new Object() {
            CompletableFuture<Void> future = new CompletableFuture<>();
        };
        threadPool.execute(() -> ref.future = super.thenAcceptBoth(other, action));
        return ref.future;
    }

    @Override
    public CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action) {
        var ref = new Object() {
            CompletableFuture<Void> future = new CompletableFuture<>();
        };
        threadPool.execute(() -> ref.future = super.runAfterBoth(other, action));
        return ref.future;
    }

    @Override
    public <U> CompletableFuture<U> applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn) {
        var ref = new Object() {
            CompletableFuture<U> future = new CompletableFuture<>();
        };
        threadPool.execute(() -> ref.future = super.applyToEither(other, fn));
        return ref.future;
    }

    @Override
    public CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action) {
        var ref = new Object() {
            CompletableFuture<Void> future = new CompletableFuture<>();
        };
        threadPool.execute(() -> ref.future = super.acceptEither(other, action));
        return ref.future;
    }

    @Override
    public CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action) {
        var ref = new Object() {
            CompletableFuture<Void> future = new CompletableFuture<>();
        };
        threadPool.execute(() -> ref.future = super.runAfterEither(other, action));
        return ref.future;
    }

    @Override
    public <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn) {
        var ref = new Object() {
            CompletableFuture<U> future = new CompletableFuture<>();
        };
        threadPool.execute(() -> ref.future = super.thenCompose(fn));
        return ref.future;
    }

    @Override
    public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action) {
        var ref = new Object() {
            CompletableFuture<T> future = new CompletableFuture<>();
        };
        threadPool.execute(() -> ref.future = super.whenComplete(action));
        return ref.future;
    }

    @Override
    public <U> CompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn) {
        var ref = new Object() {
            CompletableFuture<U> future = new CompletableFuture<>();
        };
        threadPool.execute(() -> ref.future = super.handle(fn));
        return ref.future;
    }
}
