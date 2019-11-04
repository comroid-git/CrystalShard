package de.comroid.crystalshard.core.api.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#guild-members-chunk

import java.util.Collection;

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.GuildMember;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.mappingCollection;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.serializableCollection;

@MainAPI
@JSONBindingLocation(GUILD_MEMBERS_CHUNK.JSON.class)
public interface GUILD_MEMBERS_CHUNK extends GatewayEventBase {
    default Guild getGuild() {
        return getBindingValue(JSON.GUILD);
    }

    default Collection<GuildMember> getMembers() {
        return getBindingValue(JSON.MEMBERS);
    }

    default Collection<Long> getUnknownIDs() {
        return getBindingValue(JSON.UNRECOGNIZED_IDS);
    }

    interface JSON {
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.TriStage<JSONObject, GuildMember> MEMBERS = serializableCollection("members", GuildMember.class);
        JSONBinding.TriStage<Long, Long> UNRECOGNIZED_IDS = mappingCollection("not_found", JSONObject::getLong, (api, it) -> it);
    }
}
