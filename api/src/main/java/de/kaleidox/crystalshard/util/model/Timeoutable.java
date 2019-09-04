package de.kaleidox.crystalshard.util.model;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface Timeoutable {
    ScheduledFuture<?> timeout(long time, TimeUnit unit, Runnable timeoutHandler);
}
