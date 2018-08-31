package de.kaleidox.util;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FutureHelper {
    /**
     * Links the CompletableFutures together.
     * When the parent future is done, the value gets passed to the other futures.
     * When the parent future completes exceptionally, the exception is passed to the other futures.
     *
     * @param parentFuture The parent future to listen from.
     * @param others Futures to be completed with the parent future.
     * @param <T> Type variable of the futures.
     * @return The parent future.
     */
    public static <T> CompletableFuture<T> linkFutures(CompletableFuture<T> parentFuture,
                                                       CompletableFuture<T>... others) {
        List.of(others).forEach(otherFuture -> {
            parentFuture.thenAcceptAsync(otherFuture::complete);
            parentFuture.exceptionally(throwable -> {
                otherFuture.completeExceptionally(throwable);
                return null;
            });
        });
        return parentFuture;
    }

    public static <T> CompletableFuture<T> newFuture() {
        return new CompletableFuture<>();
    }
}
