package de.comroid.crystalshard.core.api.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#presence-update

import java.util.Collection;
import java.util.Optional;

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.guild.Role;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.model.user.Presence;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.mappingCollection;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.require;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.serializableCollection;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.simple;

@MainAPI
@JSONBindingLocation(PRESENCE_UPDATE_EVENT.JSON.class)
public interface PRESENCE_UPDATE_EVENT extends GatewayEventBase {
    default User getUser() {
        return getBindingValue(JSON.USER);
    }

    default Collection<Role> getRoles() {
        return getBindingValue(JSON.ROLES);
    }

    default Optional<Presence.Activity> getActivity() {
        return wrapBindingValue(JSON.ACTIVITY);
    }

    default Guild getGuild() {
        return getBindingValue(JSON.GUILD);
    }

    default Presence.Status getStatus() {
        return getBindingValue(JSON.STATUS);
    }

    default Collection<Presence.Activity> getActivities() {
        return getBindingValue(JSON.ACTIVITIES);
    }

    default Presence.ClientStatus getClientStatus() {
        return getBindingValue(JSON.CLIENT_STATUS);
    }
    
    interface JSON {
        JSONBinding.TwoStage<JSONObject, User> USER = require("user", User.class);
        JSONBinding.TriStage<Long, Role> ROLES = mappingCollection("roles", JSONObject::getLong, (api, id) -> api.getCacheManager().getRoleByID(id).orElse(null)); // todo Inspect
        JSONBinding.TwoStage<JSONObject, Presence.Activity> ACTIVITY = require("game", Presence.Activity.class);
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.TwoStage<String, Presence.Status> STATUS = simple("status", JSONObject::getString, Presence.Status::valueOf);
        JSONBinding.TriStage<JSONObject, Presence.Activity> ACTIVITIES = serializableCollection("activities", Presence.Activity.class);
        JSONBinding.TwoStage<JSONObject, Presence.ClientStatus> CLIENT_STATUS = require("client_status", Presence.ClientStatus.class);
    }
}
