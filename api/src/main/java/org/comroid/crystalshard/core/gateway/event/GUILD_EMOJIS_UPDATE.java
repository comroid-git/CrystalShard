package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#guild-emojis-update

import java.util.Collection;

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.emoji.CustomEmoji;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.serializableCollection;

@MainAPI
@JSONBindingLocation(GUILD_EMOJIS_UPDATE.JSON.class)
public interface GUILD_EMOJIS_UPDATE extends GatewayEventBase {
    default Guild getGuild() {
        return getBindingValue(JSON.GUILD);
    }

    default Collection<CustomEmoji> getEmojis() {
        return getBindingValue(JSON.EMOJIS);
    }
    
    interface JSON {
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.TriStage<JSONObject, CustomEmoji> EMOJIS = serializableCollection("emojis", CustomEmoji.class);
    }
}
