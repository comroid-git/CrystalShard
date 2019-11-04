package de.comroid.crystalshard.abstraction.event;

import java.util.ArrayList;
import java.util.Collection;

import de.comroid.crystalshard.abstraction.serialization.AbstractJsonDeserializable;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.event.model.Event;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;

import com.alibaba.fastjson.JSONObject;

public abstract class AbstractEvent extends AbstractJsonDeserializable implements Event {
    private final Collection<ListenerAttachable> affected;

    protected AbstractEvent(Discord api, JSONObject data) {
        super(api, data);

        this.affected = new ArrayList<>();

        affects(api);
    }

    @Override
    public ListenerAttachable[] getAffected() {
        return affected.toArray(ListenerAttachable[]::new);
    }

    protected void affects(ListenerAttachable target) {
        affected.add(target);
    }
}
