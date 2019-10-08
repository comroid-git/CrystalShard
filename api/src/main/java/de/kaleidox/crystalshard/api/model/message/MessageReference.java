package de.kaleidox.crystalshard.api.model.message;

import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.core.api.cache.CacheManager;
import de.kaleidox.crystalshard.util.model.serialization.JsonDeserializable;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;
import de.kaleidox.crystalshard.util.model.serialization.JsonTraits;

import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.cache;

@JsonTraits(MessageReference.Trait.class)
public interface MessageReference extends JsonDeserializable {
    default Optional<Message> getMessage() {
        return wrapTraitValue(Trait.MESSAGE);
    }

    default Optional<Channel> getChannel() {
        return wrapTraitValue(Trait.CHANNEL);
    }

    default Optional<Guild> getGuild() {
        return wrapTraitValue(Trait.GUILD);
    }

    interface Trait {
        JsonTrait<Long, Message> MESSAGE = cache("message_id", CacheManager::getMessageByID);
        JsonTrait<Long, Channel> CHANNEL = cache("channel_id", CacheManager::getChannelByID);
        JsonTrait<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
    }
}
