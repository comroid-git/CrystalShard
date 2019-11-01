package de.comroid.crystalshard.api.event.channel;

import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.event.model.Event;

import org.jetbrains.annotations.NotNull;

public interface ChannelEvent<C extends Channel> extends Event {
    /*
    The @NotNull annotation being present and no #wrapChannel method being present
    depends on whether there are any events that can be:
    - attached to a Channel
    - fired by something else than a Channel
     */
    @NotNull C getChannel();
}
