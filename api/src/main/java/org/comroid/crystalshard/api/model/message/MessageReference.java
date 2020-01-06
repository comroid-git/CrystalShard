package org.comroid.crystalshard.api.model.message;

import java.util.Optional;

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.channel.Channel;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.entity.message.Message;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import static org.comroid.crystalshard.util.model.serialization.JSONBinding.cache;

@MainAPI
@JSONBindingLocation(MessageReference.JSON.class)
public interface MessageReference extends JsonDeserializable {
    default Optional<Message> getMessage() {
        return wrapBindingValue(JSON.MESSAGE);
    }

    default Optional<Channel> getChannel() {
        return wrapBindingValue(JSON.CHANNEL);
    }

    default Optional<Guild> getGuild() {
        return wrapBindingValue(JSON.GUILD);
    }

    interface JSON {
        JSONBinding.TwoStage<Long, Message> MESSAGE = cache("message_id", CacheManager::getMessageByID);
        JSONBinding.TwoStage<Long, Channel> CHANNEL = cache("channel_id", CacheManager::getChannelByID);
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
    }
}
