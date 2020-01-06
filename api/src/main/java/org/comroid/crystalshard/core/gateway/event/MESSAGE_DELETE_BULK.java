package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#message-delete-bulk

import java.util.Collection;
import java.util.Optional;

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.channel.Channel;
import org.comroid.crystalshard.api.entity.channel.TextChannel;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.mappingCollection;

@MainAPI
@JSONBindingLocation(MESSAGE_DELETE_BULK.JSON.class)
public interface MESSAGE_DELETE_BULK extends GatewayEventBase {
    default Collection<Long> getIDs() {
        return getBindingValue(JSON.IDS);
    }

    default TextChannel getChannel() {
        return getBindingValue(JSON.CHANNEL);
    }

    default Optional<Guild> getGuild() {
        return wrapBindingValue(JSON.GUILD);
    }

    interface JSON {
        JSONBinding.TriStage<Long, Long> IDS = mappingCollection("ids", JSONObject::getLong, (api, it) -> it);
        JSONBinding.TwoStage<Long, TextChannel> CHANNEL = cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asTextChannel));
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
    }
}
