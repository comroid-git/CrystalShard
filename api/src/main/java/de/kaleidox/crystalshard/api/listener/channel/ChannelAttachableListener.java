package de.kaleidox.crystalshard.api.listener.channel;

import de.kaleidox.crystalshard.api.event.channel.ChannelEvent;
import de.kaleidox.crystalshard.api.listener.model.AttachableListener;
import de.kaleidox.crystalshard.api.listener.model.Listener;

public interface ChannelAttachableListener<E extends ChannelEvent> extends Listener<E>, AttachableListener {
}
