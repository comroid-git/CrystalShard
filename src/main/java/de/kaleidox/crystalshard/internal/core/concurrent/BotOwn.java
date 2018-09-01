package de.kaleidox.crystalshard.internal.core.concurrent;

import java.util.concurrent.Executor;

public class BotOwn implements Executor {
    private final ThreadPool pool;

    public BotOwn(ThreadPool pool) {
        this.pool = pool;
    }

    @Override
    public void execute(Runnable command) {
        pool.execute(command);
    }
}
