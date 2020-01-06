package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#channel-pins-update

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.channel.Channel;
import org.comroid.crystalshard.api.entity.channel.TextChannel;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.entity.message.Message;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.util.annotation.IntroducedBy;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

@MainAPI
@JSONBindingLocation(CHANNEL_PINS_UPDATE.JSON.class)
public interface CHANNEL_PINS_UPDATE extends GatewayEventBase, JsonDeserializable {
    default Optional<Guild> getGuild() {
        return wrapBindingValue(JSON.GUILD);
    }

    default TextChannel getChannel() {
        return getBindingValue(JSON.CHANNEL);
    }

    default Optional<Instant> getLastPinTimestamp() {
        return wrapBindingValue(JSON.LAST_PIN_TIMESTAMP);
    }

    @IntroducedBy(PRODUCTION)
    default CompletableFuture<Collection<Message>> requestPinnedMessages() {
        return getChannel().requestPinnedMessages();
    }
    
    interface JSON {
        JSONBinding.TwoStage<Long, Guild> GUILD = JSONBinding.cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.TwoStage<Long, TextChannel> CHANNEL = JSONBinding.cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asTextChannel));
        JSONBinding.TwoStage<String, Instant> LAST_PIN_TIMESTAMP = JSONBinding.simple("last_pin_timestamp", JSONObject::getString, Instant::parse);
    }
}
