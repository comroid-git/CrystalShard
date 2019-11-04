package de.comroid.crystalshard.api.model.message;

import java.util.Optional;

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import static de.comroid.crystalshard.util.model.serialization.JSONBinding.cache;

@MainAPI
@JSONBindingLocation(MessageReference.Trait.class)
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
