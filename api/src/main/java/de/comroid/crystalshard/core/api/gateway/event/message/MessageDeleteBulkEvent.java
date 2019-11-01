package de.comroid.crystalshard.core.api.gateway.event.message;

// https://discordapp.com/developers/docs/topics/gateway#message-delete-bulk

import java.util.Collection;
import java.util.Optional;

import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.message.MessageDeleteBulkListener;
import de.comroid.crystalshard.util.annotation.ManagedBy;

@ManagedBy(MessageDeleteBulkListener.Manager.class)
public interface MessageDeleteBulkEvent extends GatewayEvent {
    String NAME = "MESSAGE_DELETE_BULK";

    Collection<Long> getIDs();

    TextChannel getChannel();

    Optional<Guild> getGuild();
}
