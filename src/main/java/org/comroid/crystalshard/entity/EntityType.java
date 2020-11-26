package org.comroid.crystalshard.entity;

import org.comroid.api.BitmaskEnum;
import org.comroid.api.Named;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.channel.GuildChannel;
import org.comroid.crystalshard.entity.channel.TextChannel;
import org.comroid.crystalshard.entity.channel.VoiceChannel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.util.Bitmask;

public final class EntityType<T extends Snowflake> implements Named, BitmaskEnum<EntityType<?>> {
    public static final EntityType<Snowflake> SNOWFLAKE = new EntityType<>(Snowflake.class);

    public static final EntityType<User> USER = new EntityType<>(User.class, SNOWFLAKE);

    public static final EntityType<Guild> GUILD = new EntityType<>(Guild.class, SNOWFLAKE);

    public static final EntityType<Channel> CHANNEL = new EntityType<>(Channel.class, SNOWFLAKE);

    public static final EntityType<TextChannel> TEXT_CHANNEL = new EntityType<>(TextChannel.class, CHANNEL);
    public static final EntityType<VoiceChannel> VOICE_CHANNEL = new EntityType<>(VoiceChannel.class, CHANNEL);

    public static final EntityType<Void> PRIVATE_CHANNEL = new EntityType<>(Void.class, CHANNEL);
    public static final EntityType<Void> GROUP_CHANNEL = new EntityType<>(Void.class, CHANNEL);

    public static final EntityType<GuildChannel> GUILD_CHANNEL = new EntityType<>(GuildChannel.class, CHANNEL);
    public static final EntityType<Void> GUILD_TEXT_CHANNEL = new EntityType<>(Void.class, GUILD_CHANNEL, TEXT_CHANNEL);
    public static final EntityType<Void> GUILD_VOICE_CHANNEL = new EntityType<>(Void.class, GUILD_CHANNEL, VOICE_CHANNEL);

    public static final EntityType<Void> MESSAGE = new EntityType<>(Void.class, SNOWFLAKE);

    private final int value;
    private final Class<T> relatedClass;

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getName() {
        return relatedClass.getSimpleName();
    }

    private EntityType(Class<T> relatedClass, EntityType<?>... inherits) {
        this.relatedClass = relatedClass;
        this.value = Bitmask.combine(Bitmask.combine(inherits), Bitmask.nextFlag());
    }

    public Class<T> getRelatedClass() {
        return relatedClass;
    }

    public boolean isInterface() {
        return getRelatedClass().isInterface();
    }
}
