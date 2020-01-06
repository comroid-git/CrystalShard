package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#message-reaction-remove-all

import java.util.Optional;

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.channel.Channel;
import org.comroid.crystalshard.api.entity.channel.TextChannel;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.entity.message.Message;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import static org.comroid.crystalshard.util.model.serialization.JSONBinding.cache;

@MainAPI
@JSONBindingLocation(MESSAGE_REACTION_REMOVE_ALL.JSON.class)
public interface MESSAGE_REACTION_REMOVE_ALL extends GatewayEventBase {
    default TextChannel getChannel() {
        return getBindingValue(JSON.CHANNEL);
    }

    default Message getMessage() {
        return getBindingValue(JSON.MESSAGE);
    }

    default Optional<Guild> getGuild() {
        return wrapBindingValue(JSON.GUILD);
    }
    
    interface JSON {
        JSONBinding.TwoStage<Long, TextChannel> CHANNEL = cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asTextChannel));
        JSONBinding.TwoStage<Long, Message> MESSAGE = cache("message_id", CacheManager::getMessageByID);
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
    }
}
