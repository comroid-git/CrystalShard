package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#guild-delete

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.Snowflake;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.simple;

@MainAPI
@JSONBindingLocation(GUILD_DELETE.JSON.class)
public interface GUILD_DELETE extends GatewayEventBase {
    default Snowflake getGuildID() {
        return getBindingValue(JSON.GUILD_ID);
    }

    default boolean isUnavailable() {
        return !getBindingValue(JSON.WAS_KICKED);
    }

    default boolean isRemoved() {
        return getBindingValue(JSON.WAS_KICKED);
    }
    
    interface JSON {
        /*
        creating json bindings is different here:
        - a deleted guild should immediately be unavailable, so we don't return a guild object.
        - we need to know whether we were kicked
         */
        JSONBinding.TwoStage<Long, Snowflake> GUILD_ID = simple("id", JSONObject::getLong, Snowflake::mime);
        JSONBinding.OneStage<Boolean> WAS_KICKED = identity(null, (json, nil) -> !json.containsKey("unavailable"));
    }
}
