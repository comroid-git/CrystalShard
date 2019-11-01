package de.comroid.crystalshard.api.listener.channel;

import de.comroid.crystalshard.api.event.channel.ChannelEvent;
import de.comroid.crystalshard.api.listener.model.AttachableListener;
import de.comroid.crystalshard.api.listener.model.Listener;

public interface ChannelAttachableListener<E extends ChannelEvent> extends Listener<E>, AttachableListener {
}
