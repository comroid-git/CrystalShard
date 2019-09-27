package de.kaleidox.crystalshard.api.listener.message;

import de.kaleidox.crystalshard.api.event.message.MessageEvent;
import de.kaleidox.crystalshard.api.listener.model.AttachableListener;
import de.kaleidox.crystalshard.api.listener.model.Listener;

public interface MessageAttachableListener<E extends MessageEvent> extends Listener<E>, AttachableListener {
}
