package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#typing-start

import java.time.Instant;
import java.util.Optional;

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.channel.Channel;
import org.comroid.crystalshard.api.entity.channel.TextChannel;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.entity.user.User;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.simple;

@MainAPI
@JSONBindingLocation(TYPING_START.JSON.class)
public interface TYPING_START extends GatewayEventBase {
    default TextChannel getChannel() {
        return getBindingValue(JSON.CHANNEL);
    }

    default Optional<Guild> getGuild() {
        return wrapBindingValue(JSON.GUILD);
    }

    default User getUser() {
        return getBindingValue(JSON.USER);
    }

    default Instant getStartedTimestamp() {
        return getBindingValue(JSON.STARTED_AT);
    }
    
    interface JSON {
        JSONBinding.TwoStage<Long, TextChannel> CHANNEL = cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asTextChannel));
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.TwoStage<Long, User> USER = cache("user_id", CacheManager::getUserByID);
        JSONBinding.TwoStage<Integer, Instant> STARTED_AT = simple("timestamp", JSONObject::getInteger, Instant::ofEpochSecond);
    }
}
