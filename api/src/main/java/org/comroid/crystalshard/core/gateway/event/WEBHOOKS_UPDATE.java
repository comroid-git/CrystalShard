package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#voice-state-update

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.channel.Channel;
import org.comroid.crystalshard.api.entity.channel.GuildTextChannel;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

@MainAPI
@JSONBindingLocation(WEBHOOKS_UPDATE.JSON.class)
public interface WEBHOOKS_UPDATE extends GatewayEventBase {
    default Guild getGuild() {
        return getBindingValue(JSON.GUILD);
    }

    default GuildTextChannel getChannel() {
        return getBindingValue(JSON.CHANNEL);
    }
    
    interface JSON {
        JSONBinding.TwoStage<Long, Guild> GUILD = JSONBinding.cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.TwoStage<Long, GuildTextChannel> CHANNEL = JSONBinding.cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asGuildTextChannel));
    }
}
