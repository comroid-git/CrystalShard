package de.kaleidox.crystalshard.abstraction.event;

import java.util.ArrayList;
import java.util.Collection;

import de.kaleidox.crystalshard.abstraction.model.AbstractJsonDeserializable;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.event.model.Event;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class AbstractEvent extends AbstractJsonDeserializable implements Event {
    private final Collection<ListenerAttachable> affected;

    protected AbstractEvent(Discord api, JsonNode data) {
        super(api, data);

        this.affected = new ArrayList<>();

        affects(api);
    }

    protected void affects(ListenerAttachable target) {
        affected.add(target);
    }

    @Override
    public ListenerAttachable[] getAffected() {
        return affected.toArray(ListenerAttachable[]::new);
    }
}
