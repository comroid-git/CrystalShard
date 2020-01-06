package org.comroid.crystalshard.abstraction.handling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.comroid.crystalshard.abstraction.AbstractApiBound;
import org.comroid.crystalshard.api.Discord;
import org.comroid.crystalshard.api.event.EventBase;
import org.comroid.crystalshard.api.event.EventHandler;

public class AbstractEvent extends AbstractApiBound implements EventBase {
    private final Collection<EventHandler<? extends EventBase>> affected;

    protected AbstractEvent(Discord api) {
        super(api);

        affected = new ArrayList<>();

        affects(api);
    }

    @Override
    public void affects(EventHandler<? extends EventBase> handler) {
        affected.add(handler);
    }

    @Override
    public Collection<EventHandler<? extends EventBase>> getAffected() {
        return Collections.unmodifiableCollection(affected);
    }
}
