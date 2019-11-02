package de.comroid.crystalshard.api.event.message;

import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.api.event.model.Event;

import org.jetbrains.annotations.NotNull;

public interface MessageEvent extends Event {
    /*
    The @NotNull annotation being present and no #wrapMessage method being present
    depends on whether there are any events that can be:
    - attached to a Message
    - fired by something else than a Message
     */
    @NotNull Message getTriggeringMessage();
}
