package de.kaleidox.crystalshard.api.model.channel;

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.core.api.cache.CacheManager;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.model.serialization.JsonDeserializable;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;
import de.kaleidox.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.cache;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.identity;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.simple;

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
        JsonTrait<Long, Channel> CHANNEL = cache("id", CacheManager::getChannelByID);
        JsonTrait<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JsonTrait<Integer, ChannelType> CHANNEL_TYPE = simple(JsonNode::asInt, "type", ChannelType::valueOf);
        JsonTrait<String, String> CHANNEL_NAME = identity(JsonNode::asText, "name");
    }
}
