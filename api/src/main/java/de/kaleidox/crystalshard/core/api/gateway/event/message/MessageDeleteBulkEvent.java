package de.kaleidox.crystalshard.core.api.gateway.event.message;

// https://discordapp.com/developers/docs/topics/gateway#message-delete-bulk

import java.util.Collection;
import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.message.MessageDeleteBulkListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(MessageDeleteBulkListener.Manager.class)
public interface MessageDeleteBulkEvent extends GatewayEvent {
    String NAME = "MESSAGE_DELETE_BULK";

    Collection<Long> getIDs();

    TextChannel getChannel();

    Optional<Guild> getGuild();
}
