package de.kaleidox.crystalshard.core.listener;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.handling.listener.Listener;

public abstract class ListenerImpl implements Listener {
    protected final Discord discord;

    protected ListenerImpl(Discord discord) {
        this.discord = discord;
    }

    @Override
    public Discord getDiscord() {
        return discord;
    }
}
