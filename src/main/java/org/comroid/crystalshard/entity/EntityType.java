package org.comroid.crystalshard.entity;

import org.comroid.api.BitmaskEnum;
import org.comroid.api.Named;
import org.comroid.api.Polyfill;
import org.comroid.crystalshard.entity.channel.*;
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
import org.comroid.util.Bitmask;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Stream;

public final class EntityType<T extends Snowflake> implements BitmaskEnum<EntityType<?>>, Named {
    private static final Collection<EntityType<?>> values = new ArrayList<>();

    public static final EntityType<Snowflake> SNOWFLAKE
            = new EntityType<>(Snowflake.class);

    public static final EntityType<User> USER
            = new EntityType<>(User.class);
    public static final EntityType<Webhook> WEBHOOK
            = new EntityType<>(Webhook.class);

    public static final EntityType<Guild> GUILD
            = new EntityType<>(Guild.class);
    public static final EntityType<Role> ROLE
            = new EntityType<>(Role.class);
    public static final EntityType<CustomEmoji> CUSTOM_EMOJI
            = new EntityType<>(CustomEmoji.class);
    public static final EntityType<GuildIntegration> INTEGRATION
            = new EntityType<>(GuildIntegration.class);

    public static final EntityType<Channel> CHANNEL
            = new EntityType<>(Channel.class);

    public static final EntityType<TextChannel> TEXT_CHANNEL
            = new EntityType<>(TextChannel.class, CHANNEL);
    public static final EntityType<VoiceChannel> VOICE_CHANNEL
            = new EntityType<>(VoiceChannel.class, CHANNEL);

    public static final EntityType<PrivateTextChannel> PRIVATE_CHANNEL
            = new EntityType<>(PrivateTextChannel.class, TEXT_CHANNEL);
    public static final EntityType<GroupChannel> GROUP_CHANNEL
            = new EntityType<>(GroupChannel.class, TEXT_CHANNEL);

    public static final EntityType<GuildChannel> GUILD_CHANNEL
            = new EntityType<>(GuildChannel.class, CHANNEL);
    public static final EntityType<GuildChannelCategory> GUILD_CHANNEL_CATEGORY
            = new EntityType<>(GuildChannelCategory.class, GUILD_CHANNEL);
    public static final EntityType<GuildTextChannel> GUILD_TEXT_CHANNEL
            = new EntityType<>(GuildTextChannel.class, GUILD_CHANNEL, TEXT_CHANNEL);
    public static final EntityType<GuildVoiceChannel> GUILD_VOICE_CHANNEL
            = new EntityType<>(GuildVoiceChannel.class, GUILD_CHANNEL, VOICE_CHANNEL);

    public static final EntityType<Message> MESSAGE
            = new EntityType<>(Message.class);
    public static final EntityType<MessageAttachment> MESSAGE_ATTACHMENT
            = new EntityType<>(MessageAttachment.class);
    public static final EntityType<MessageApplication> MESSAGE_APPLICATION
            = new EntityType<>(MessageApplication.class);
    public static final EntityType<MessageSticker> MESSAGE_STICKER
            = new EntityType<>(MessageSticker.class);

    private final int value;
    private final boolean isCacheName;
    private final EntityType<? super T> relatedCacheName;
    private final Class<T> relatedClass;

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getName() {
        return relatedClass.getSimpleName();
    }

    @Override
    public String getAlternateFormattedName() {
        return getRelatedCacheName().getName();
    }

    @Internal
    public EntityType<? super T> getRelatedCacheName() {
        if (isCacheName || relatedCacheName == null)
            return this;
        return relatedCacheName;
    }

    private EntityType(Class<T> relatedClass, EntityType<?>... inherits) {
        this.relatedClass = relatedClass;
        if (relatedClass.equals(Snowflake.class)) {
            this.value = Bitmask.nextFlag();
            this.isCacheName = false;
            this.relatedCacheName = null;
        } else if (inherits.length == 0) {
            this.value = Bitmask.combine(SNOWFLAKE.value, Bitmask.nextFlag());
            this.isCacheName = true;
            this.relatedCacheName = this;
        } else {
            this.value = Bitmask.combine(Bitmask.combine(inherits), Bitmask.nextFlag());
            this.isCacheName = false;
            this.relatedCacheName = Stream.of(inherits)
                    .map(EntityType::getRelatedCacheName)
                    .findFirst()
                    .map(Polyfill::<EntityType<? super T>>uncheckedCast)
                    .orElseThrow(() -> new NoSuchElementException("Could not find related cache name"));
        }
    }

    public Class<T> getRelatedClass() {
        return relatedClass;
    }

    public boolean isInterface() {
        return getRelatedClass().isInterface();
    }
}
