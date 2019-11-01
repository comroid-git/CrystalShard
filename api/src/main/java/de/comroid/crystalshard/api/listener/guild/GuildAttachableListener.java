package de.comroid.crystalshard.api.listener.guild;

import de.comroid.crystalshard.api.event.guild.GuildEvent;
import de.comroid.crystalshard.api.listener.model.AttachableListener;
import de.comroid.crystalshard.api.listener.model.Listener;

public interface GuildAttachableListener<E extends GuildEvent> extends Listener<E>, AttachableListener {
}
