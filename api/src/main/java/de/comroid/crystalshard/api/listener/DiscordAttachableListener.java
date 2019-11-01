package de.comroid.crystalshard.api.listener;

import de.comroid.crystalshard.api.event.DiscordEvent;
import de.comroid.crystalshard.api.listener.model.AttachableListener;
import de.comroid.crystalshard.api.listener.model.Listener;

public interface DiscordAttachableListener<E extends DiscordEvent> extends Listener<E>, AttachableListener {
}
