package de.kaleidox.crystalshard.core.api.gateway.event.message;

// https://discordapp.com/developers/docs/topics/gateway#message-delete-bulk

import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface MessageDeleteBulkEvent extends GatewayEvent {
    String NAME = "MESSAGE_DELETE_BULK";

    Snowflake[] getIDs();

    TextChannel getChannel();

    Optional<Guild> getGuild();
}
