package de.comroid.crystalshard.api.event;

import java.util.Collection;

import org.jetbrains.annotations.ApiStatus.Internal;

public interface EventBase {
    @Internal
    void affects(EventHandler<? extends EventBase> handler);

    Collection<EventHandler<? extends EventBase>> getAffected();
}
