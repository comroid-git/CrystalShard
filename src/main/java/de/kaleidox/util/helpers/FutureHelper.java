package de.kaleidox.util.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class FutureHelper extends NullHelper {
    /**
     * Links the CompletableFutures together.
     * When the parent future is done, the value gets passed to the other futures.
     * When the parent future completes exceptionally, the exception is passed to the other futures.
     *
     * @param parentFuture The parent future to listen from.
     * @param others       Futures to be completed with the parent future.
     * @param <T>          Type variable of the futures.
     * @return The parent future.
     */
    @SafeVarargs
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

    /**
     * Waits for the completion of all given {@link CompletableFuture}s.
     * Returns a future that completes with a list of values returned by the Futures.
     * Runs in async by default.
     *
     * @param futures A list of futures to wait for their completion.
     * @param <U>     The type of the returning list.
     * @return A future to contain all the items that the futures completed with.
     */
    public static <U> CompletableFuture<List<U>> waitForCompletion(Collection<CompletableFuture<U>> futures) {
        CompletableFuture<List<U>> val = new CompletableFuture<>();
        List<U> returnVals = new ArrayList<>();

        CompletableFuture.supplyAsync(() -> new int[]{}).thenAcceptAsync(a -> {
            boolean stop = false;
            while (!stop) {
                for (CompletableFuture<U> future : futures) {
                    if (future.isDone()) {
                        returnVals.add(future.join());
                    }

                    if (future.isCancelled()) {
                        futures.remove(future);
                    }
                }

                if (returnVals.size() == futures.size()) {
                    val.complete(returnVals);

                    stop = true;
                }
            }
        });

        return val;
    }

    public static <T> CompletableFuture<T> newFuture() {
        return new CompletableFuture<>();
    }
}
