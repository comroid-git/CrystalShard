package de.kaleidox.crystalshard.api.listener;

import de.kaleidox.crystalshard.api.event.DiscordEvent;
import de.kaleidox.crystalshard.api.listener.model.AttachableListener;
import de.kaleidox.crystalshard.api.listener.model.Listener;

public interface DiscordAttachableListener<E extends DiscordEvent> extends Listener<E>, AttachableListener {
}
