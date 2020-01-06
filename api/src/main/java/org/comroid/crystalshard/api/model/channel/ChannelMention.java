package org.comroid.crystalshard.api.model.channel;

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.channel.Channel;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.util.annotation.IntroducedBy;
import org.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.simple;

@MainAPI
@JSONBindingLocation(ChannelMention.JSON.class) 
public interface ChannelMention extends JsonDeserializable {
    @IntroducedBy(GETTER)
    default Channel getChannel() {
        return getBindingValue(JSON.CHANNEL);
    }

    @IntroducedBy(GETTER)
    default Guild getGuild() {
        return getBindingValue(JSON.GUILD);
    }

    @IntroducedBy(GETTER)
    default ChannelType getChannelType() {
        return getBindingValue(JSON.CHANNEL_TYPE);
    }

    @IntroducedBy(GETTER)
    default String getChannelName() {
        return getBindingValue(JSON.CHANNEL_NAME);
    }

    interface JSON {
        JSONBinding.TwoStage<Long, Channel> CHANNEL = cache("id", CacheManager::getChannelByID);
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.TwoStage<Integer, ChannelType> CHANNEL_TYPE = simple("type", JSONObject::getInteger, ChannelType::valueOf);
        JSONBinding.OneStage<String> CHANNEL_NAME = identity("name", JSONObject::getString);
    }
}
