package de.kaleidox.crystalshard.internal.items;

import de.kaleidox.crystalshard.internal.core.ThreadPool;
import de.kaleidox.crystalshard.main.Discord;

public class DiscordInternal implements Discord {
    private final ThreadPool pool;

    public DiscordInternal() {
         this.pool = new ThreadPool(20);
    }

    public ThreadPool getPool() {
        return pool;
    }
}
