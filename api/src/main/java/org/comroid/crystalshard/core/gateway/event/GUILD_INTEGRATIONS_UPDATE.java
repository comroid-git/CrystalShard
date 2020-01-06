package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#guild-integrations-update

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;

import static org.comroid.crystalshard.util.model.serialization.JSONBinding.cache;

@MainAPI
@JSONBindingLocation(GUILD_INTEGRATIONS_UPDATE.JSON.class)
public interface GUILD_INTEGRATIONS_UPDATE extends GatewayEventBase {
    default Guild getGuild() {
        return getBindingValue(JSON.GUILD);
    }
    
    interface JSON {
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
    }
}
