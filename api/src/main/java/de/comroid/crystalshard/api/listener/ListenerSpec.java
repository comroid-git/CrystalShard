package de.comroid.crystalshard.api.listener;

import de.comroid.crystalshard.api.event.DiscordEvent;
import de.comroid.crystalshard.api.event.channel.ChannelEvent;
import de.comroid.crystalshard.api.event.guild.GuildEvent;
import de.comroid.crystalshard.api.event.message.MessageEvent;
import de.comroid.crystalshard.api.event.role.RoleEvent;
import de.comroid.crystalshard.api.event.user.UserEvent;
import de.comroid.crystalshard.api.listener.model.AttachableListener;
import de.comroid.crystalshard.api.listener.model.Listener;

public interface ListenerSpec {
    interface AttachableTo {
        interface Discord extends Listener, AttachableListener {
        }
        
        interface Guild extends Listener, AttachableListener {
        }

        interface Channel extends Listener, AttachableListener {
        }

        interface Role extends Listener, AttachableListener {
        }

        interface User extends Listener, AttachableListener {
        }

        interface Message extends Listener, AttachableListener {
        }
    }
}
