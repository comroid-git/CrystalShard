package de.comroid.crystalshard.api.model.channel;

import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.simple;

@JsonTraits(ChannelMention.Trait.class) public
interface ChannelMention extends JsonDeserializable {
    @IntroducedBy(GETTER)
    default Channel getChannel() {
        return getTraitValue(Trait.CHANNEL);
    }

    @IntroducedBy(GETTER)
    default Guild getGuild() {
        return getTraitValue(Trait.GUILD);
    }

    @IntroducedBy(GETTER)
    default ChannelType getChannelType() {
        return getTraitValue(Trait.CHANNEL_TYPE);
    }

    @IntroducedBy(GETTER)
    default String getChannelName() {
        return getTraitValue(Trait.CHANNEL_NAME);
    }

    interface Trait {
        JsonBinding<Long, Channel> CHANNEL = cache("id", CacheManager::getChannelByID);
        JsonBinding<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JsonBinding<Integer, ChannelType> CHANNEL_TYPE = simple(JsonNode::asInt, "type", ChannelType::valueOf);
        JsonBinding<String, String> CHANNEL_NAME = identity(JsonNode::asText, "name");
    }
}
