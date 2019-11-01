package de.comroid.crystalshard.api.listener.message;

import de.comroid.crystalshard.api.event.message.MessageEvent;
import de.comroid.crystalshard.api.listener.model.AttachableListener;
import de.comroid.crystalshard.api.listener.model.Listener;

public interface MessageAttachableListener<E extends MessageEvent> extends Listener<E>, AttachableListener {
}
