package de.kaleidox.util.objects;

import de.kaleidox.util.annotations.NotNull;

import java.util.concurrent.ScheduledExecutorService;

public class DeletingReference<T> {
    private final T referent;
    private final ScheduledExecutorService scheduler;

    public DeletingReference(@NotNull T referent,
                             @NotNull ScheduledExecutorService scheduler) {
        this.referent = referent;
        this.scheduler = scheduler;
    }
}
