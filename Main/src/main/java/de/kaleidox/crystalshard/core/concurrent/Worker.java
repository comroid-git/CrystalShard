package de.kaleidox.crystalshard.core.concurrent;

import de.kaleidox.crystalshard.main.Discord;

public abstract class Worker extends Thread {
    public Worker(String name) {
        super(name);
    }
    
    public Worker(Runnable initTask, String name) {
        super(initTask, name);
    }
    
    public abstract Discord getDiscord();
}
