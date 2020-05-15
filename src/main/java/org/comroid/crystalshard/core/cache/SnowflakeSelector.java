package org.comroid.crystalshard.core.cache;

import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.CustomEmoji;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.entity.user.User;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

public final class SnowflakeSelector {
    @SuppressWarnings("unchecked")
    private static final Class<? extends Snowflake>[] managed = new Class[] {
            Guild.class, User.class, Message.class, Channel.class, Role.class, CustomEmoji.class
    };
    private final long id;
    private final Map<Class<? extends Snowflake>, Snowflake> types = new ConcurrentHashMap<>();

    public SnowflakeSelector(long id) {
        this.id = id;
    }

    public Guild asGuild() {
        return as(Guild.class);
    }

    public User asUser() {
        return as(User.class);
    }

    public Message asMessage() {
        return as(Message.class);
    }

    public Channel asChannel() {
        return as(Channel.class);
    }

    public Role asRole() {
        return as(Role.class);
    }

    public CustomEmoji asCustomEmoji() {
        return as(CustomEmoji.class);
    }

    private <T extends Snowflake> T as(Class<T> type) {
        if (!types.containsKey(type))
            throw new NoSuchElementException(String.format("No %s with id %d found", type.getSimpleName(), id));

        return type.cast(types.get(type));
    }

    public void put(Snowflake snowflake) {
        final Class<? extends Snowflake> classOf = classOf(snowflake);

        types.put(classOf, snowflake);
    }

    private Class<? extends Snowflake> classOf(Snowflake snowflake) {
        for (Class<? extends Snowflake> type : managed) {
            if (type.isInstance(snowflake))
                return type;
        }

        throw new IllegalStateException("Unmanaged type: " + snowflake.getClass());
    }
}
