package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#guild-member-add

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.entity.user.GuildMember;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.rooted;

@MainAPI
@JSONBindingLocation(GUILD_MEMBER_ADD.JSON.class)
public interface GUILD_MEMBER_ADD extends GatewayEventBase {
    default Guild getGuild() {
        return getBindingValue(JSON.GUILD);
    }

    default GuildMember getMember() {
        return getBindingValue(JSON.MEMBER);
    }
    
    interface JSON {
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.TwoStage<JSONObject, GuildMember> MEMBER = rooted(GuildMember.class);
    }
}
