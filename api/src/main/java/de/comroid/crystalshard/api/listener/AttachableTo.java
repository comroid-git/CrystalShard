package de.comroid.crystalshard.api.listener;

import de.comroid.crystalshard.api.event.DiscordEvent;
import de.comroid.crystalshard.api.event.channel.ChannelEvent;
import de.comroid.crystalshard.api.event.guild.GuildEvent;
import de.comroid.crystalshard.api.event.role.RoleEvent;
import de.comroid.crystalshard.api.event.user.UserEvent;
import de.comroid.crystalshard.api.listener.model.AttachableListener;
import de.comroid.crystalshard.api.listener.model.Listener;

public interface ListenerSpec {
    interface AttachableTo {
        interface Discord<E extends DiscordEvent> extends Listener<E>, AttachableListener {
        }
        
        interface Guild<E extends GuildEvent> extends Listener<E>, AttachableListener {
        }

        interface Channel<E extends ChannelEvent> extends Listener<E>, AttachableListener {
        }

        interface Role<E extends RoleEvent> extends Listener<E>, AttachableListener {
        }

        interface User<E extends UserEvent> extends Listener<E>, AttachableListener {
        }
    }
}
