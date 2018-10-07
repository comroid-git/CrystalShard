package de.kaleidox.crystalshard.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class represents an extension to the CompletableFuture class. For async methods, it will always use a bot-own thread for asynchronity.
 *
 * @param <T> The type variable of the Item.
 */
public class CompletableFutureExtended<T> extends CompletableFuture<T> {
    private final Executor executor;
    
    public CompletableFutureExtended(Executor executor) {
        super();
        this.executor = executor;
    }
    
    // Override Methods
    @Override
    public <U> CompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> fn) {
        return super.thenApplyAsync(fn, executor);
    }
    
    @Override
    public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action) {
        return thenAcceptAsync(action, executor);
    }
    
    @Override
    public <U, V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn) {
        return super.thenCombineAsync(other, fn, executor);
    }
    
    @Override
    public <U> CompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action) {
        return super.thenAcceptBothAsync(other, action, executor);
    }
    
    @Override
    public CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action) {
        return super.runAfterBothAsync(other, action, executor);
    }
    
    @Override
    public <U> CompletableFuture<U> applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn) {
        return super.applyToEitherAsync(other, fn, executor);
    }
    
    @Override
    public CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action) {
        return super.acceptEitherAsync(other, action, executor);
    }
    
    @Override
    public CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action) {
        return super.runAfterEitherAsync(other, action, executor);
    }
    
    @Override
    public <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn) {
        return super.thenComposeAsync(fn, executor);
    }
    
    @Override
    public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action) {
        return super.whenCompleteAsync(action, executor);
    }
    
    @Override
    public <U> CompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn) {
        return super.handleAsync(fn, executor);
    }
}
