package de.kaleidox.crystalshard.api.listener.guild;

import de.kaleidox.crystalshard.api.event.guild.GuildEvent;
import de.kaleidox.crystalshard.api.listener.model.AttachableListener;
import de.kaleidox.crystalshard.api.listener.model.Listener;

public interface GuildAttachableListener<E extends GuildEvent> extends Listener<E>, AttachableListener {
}
