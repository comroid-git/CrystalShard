package de.comroid.crystalshard.api.model.message;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import static de.comroid.crystalshard.util.model.serialization.JsonBinding.cache;

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
        JsonBinding<Long, Message> MESSAGE = cache("message_id", CacheManager::getMessageByID);
        JsonBinding<Long, Channel> CHANNEL = cache("channel_id", CacheManager::getChannelByID);
        JsonBinding<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
    }
}
