package org.comroid.crystalshard.entity;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.channel.GuildChannelCategory;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.entity.webhook.Webhook;
import org.comroid.mutatio.ref.Processor;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.ref.ReferenceMap;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class SnowflakeCache implements ContextualProvider.Underlying {
    private final ReferenceMap<String, Snowflake> cache = ReferenceMap.create();
    private final ContextualProvider context;

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return context.plus(this);
    }

    SnowflakeCache(ContextualProvider context) {
        this.context = context;
    }

    public Reference<Guild> getGuild(long id) {
        return getSnowflake(EntityType.GUILD, id);
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

    public Reference<User> getUser(long id) {
        return getSnowflake(EntityType.USER, id);
    }

    public Reference<Webhook> getWebhook(long id) {
        return getSnowflake(EntityType.WEBHOOK, id);
    }

    public <T extends Snowflake> Processor<T> getSnowflake(EntityType<T> type, long id) {
        return cache.getReference(getKey(type, id), true)
                .flatMap(type.getRelatedClass());
    }

    private <T extends Snowflake> String getKey(EntityType<T> type, long id) {
        Objects.requireNonNull(type, "Type is null");

        return String.format("%s#%d", type.getRelatedCacheName(), id);
    }
}
