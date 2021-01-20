package org.comroid.crystalshard.entity;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.channel.GuildChannelCategory;
import org.comroid.crystalshard.entity.guild.CustomEmoji;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.entity.message.MessageApplication;
import org.comroid.crystalshard.entity.message.MessageAttachment;
import org.comroid.crystalshard.entity.message.MessageSticker;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.entity.webhook.Webhook;
import org.comroid.crystalshard.model.guild.GuildIntegration;
import org.comroid.mutatio.ref.KeyedReference;
import org.comroid.mutatio.ref.Processor;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.ref.ReferenceMap;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.Objects;

public final class SnowflakeCache implements ContextualProvider.Underlying {
    private final ReferenceMap<String, Snowflake> cache = ReferenceMap.create();
    private final ContextualProvider context;

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return context.plus(this);
    }

    @Internal
    public SnowflakeCache(ContextualProvider context) {
        this.context = context;
    }

    public Reference<Guild> getGuild(long id) {
        return getSnowflake(EntityType.GUILD, id);
    }

    public Reference<GuildIntegration> getGuildIntegration(long id) {
        return getSnowflake(EntityType.GUILD_INTEGRATION, id);
    }

    public Reference<Role> getRole(long id) {
        return getSnowflake(EntityType.ROLE, id);
    }

    public Reference<Channel> getChannel(long id) {
        return getSnowflake(EntityType.CHANNEL, id);
    }

    public Reference<GuildChannelCategory> getChannelCategory(long id) {
        return getSnowflake(EntityType.GUILD_CHANNEL_CATEGORY, id);
    }

    public Reference<Message> getMessage(long id) {
        return getSnowflake(EntityType.MESSAGE, id);
    }

    public Reference<MessageApplication> getMessageApplication(long id) {
        return getSnowflake(EntityType.MESSAGE_APPLICATION, id);
    }

    public Reference<MessageAttachment> getMessageAttachment(long id) {
        return getSnowflake(EntityType.MESSAGE_ATTACHMENT, id);
    }

    public Reference<MessageSticker> getMessageSticker(long id) {
        return getSnowflake(EntityType.MESSAGE_STICKER, id);
    }

    public Reference<User> getUser(long id) {
        return getSnowflake(EntityType.USER, id);
    }

    public Reference<Webhook> getWebhook(long id) {
        return getSnowflake(EntityType.WEBHOOK, id);
    }

    public Reference<CustomEmoji> getCustomEmoji(long id) {
        return getSnowflake(EntityType.CUSTOM_EMOJI, id);
    }

    public <T extends Snowflake> Processor<T> getSnowflake(EntityType<T> type, long id) {
        return getReference(type, id).flatMap(type.getRelatedClass());
    }

    public <T extends Snowflake> KeyedReference<String, Snowflake> getReference(EntityType<T> type, long id) {
        return cache.getReference(getKey(type, id), true);
    }

    private <T extends Snowflake> String getKey(EntityType<T> type, long id) {
        Objects.requireNonNull(type, "Type is null");

        return String.format("%s#%d", type.getRelatedCacheName(), id);
    }

    public int size() {
        return cache.size();
    }
}
