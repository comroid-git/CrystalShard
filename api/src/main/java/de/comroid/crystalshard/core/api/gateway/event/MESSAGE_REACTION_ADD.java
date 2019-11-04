package de.comroid.crystalshard.core.api.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#message-reaction-add

import java.util.Optional;

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.entity.emoji.Emoji;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.require;

@MainAPI
@JSONBindingLocation(MESSAGE_REACTION_ADD.JSON.class)
public interface MESSAGE_REACTION_ADD extends GatewayEventBase {
    default User getUser() {
        return getBindingValue(JSON.USER);
    }

    default TextChannel getChannel() {
        return getBindingValue(JSON.CHANNEL);
    }

    default Message getMessage() {
        return getBindingValue(JSON.MESSAGE);
    }

    default Optional<Guild> getGuild() {
        return wrapBindingValue(JSON.GUILD);
    }

    default Emoji getEmoji() {
        return getBindingValue(JSON.EMOJI);
    }
    
    interface JSON {
        JSONBinding.TwoStage<Long, User> USER = cache("user_id", CacheManager::getUserByID);
        JSONBinding.TwoStage<Long, TextChannel> CHANNEL = cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asTextChannel));
        JSONBinding.TwoStage<Long, Message> MESSAGE = cache("message_id", CacheManager::getMessageByID);
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.TwoStage<JSONObject, Emoji> EMOJI = require("emoji", Emoji.class);
    }
}
