package org.comroid.crystalshard.entity;

import org.comroid.api.BitmaskEnum;
import org.comroid.api.Polyfill;
import org.comroid.common.ref.Named;
import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.channel.*;
import org.comroid.crystalshard.entity.guild.CustomEmoji;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.guild.GuildMember;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.entity.message.MessageAttachment;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.entity.webhook.Webhook;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.Bitmask;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainer;
import org.comroid.varbind.container.DataContainerBase;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Comparator;
import java.util.NoSuchElementException;

public interface Snowflake extends BotBound, Comparable<Snowflake>, DataContainer<DiscordBot> {
    Comparator<Snowflake> SNOWFLAKE_COMPARATOR = Comparator.comparingLong(flake -> flake.getID() >> 22);
    @Language("RegExp")
    String ID_REGEX = "\\d{12,20}"; //todo Improve Regex

    default Instant getCreationTimestamp() {
        return Instant.ofEpochMilli((getID() >> 22) + DiscordAPI.EPOCH);
    }

    default long getID() {
        return requireNonNull(Bind.ID);
    }

    Type<? extends Snowflake> getType();

    @Override
    default int compareTo(@NotNull Snowflake other) {
        return SNOWFLAKE_COMPARATOR.compare(this, other);
    }

    interface Bind {
        GroupBind<Snowflake, DiscordBot> Root = new GroupBind<>(CrystalShard.SERIALIZATION_ADAPTER, "snowflake");
        VarBind<Long, DiscordBot, Long, Long> ID = Root.createBind("id")
                .extractAs(ValueType.LONG)
                .asIdentities()
                .onceEach()
                .build();
    }

    final class Type<T extends Snowflake> implements Named, BitmaskEnum<Type<T>> {
        public static final Type<Channel> CHANNEL
                = new Type<>(Channel.class, "Channel");
        public static final Type<TextChannel> TEXT_CHANNEL
                = new Type<>(TextChannel.class, "TextChannel", CHANNEL);
        public static final Type<VoiceChannel> VOICE_CHANNEL
                = new Type<>(VoiceChannel.class, "VoiceChannel", CHANNEL);
        public static final Type<PrivateChannel> PRIVATE_CHANNEL
                = new Type<>(PrivateChannel.class, "PrivateChannel", CHANNEL);
        public static final Type<GuildChannel> GUILD_CHANNEL
                = new Type<>(GuildChannel.class, "GuildChannel", CHANNEL);
        public static final Type<ChannelCategory> CHANNEL_CATEGORY
                = new Type<>(ChannelCategory.class, "ChannelCategory", GUILD_CHANNEL);
        public static final Type<GuildTextChannel> GUILD_TEXT_CHANNEL
                = new Type<>(GuildTextChannel.class, "GuildTextChannel", GUILD_CHANNEL, TEXT_CHANNEL);
        public static final Type<GuildVoiceChannel> GUILD_VOICE_CHANNEL
                = new Type<>(GuildVoiceChannel.class, "GuildVoiceChannel", GUILD_CHANNEL, VOICE_CHANNEL);
        public static final Type<PrivateTextChannel> PRIVATE_TEXT_CHANNEL
                = new Type<>(PrivateTextChannel.class, "PrivateTextChannel", PRIVATE_CHANNEL, TEXT_CHANNEL);
        public static final Type<Guild> GUILD
                = new Type<>(Guild.class, "Guild");
        public static final Type<Role> ROLE
                = new Type<>(Role.class, "Role");
        public static final Type<CustomEmoji> CUSTOM_EMOJI
                = new Type<>(CustomEmoji.class, "CustomEmoji");
        public static final Type<Message> MESSAGE
                = new Type<>(Message.class, "Message");
        public static final Type<MessageAttachment> MESSAGE_ATTACHMENT
                = new Type<>(MessageAttachment.class, "MessageAttachment");
        public static final Type<User> USER
                = new Type<>(User.class, "User");
        public static final Type<Webhook> WEBHOOK
                = new Type<>(Webhook.class, "Webhook");

        private final Class<T> typeClass;
        private final String name;
        private final int mask;
        private final String hexId;
        private final GroupBind<T, DiscordBot> groupBind;

        public Class<T> getTypeClass() {
            return typeClass;
        }

        @Override
        public int getValue() {
            return mask;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getAlternateFormattedName() {
            return hexId;
        }

        @SafeVarargs
        private Type(Class<T> typeClass, String name, Type<? super T>... parents) {
            this.typeClass = typeClass;
            this.name = name;
            this.mask = (parents.length == 0 ? Bitmask.nextFlag() : Bitmask.combine(parents));
            this.hexId = Integer.toHexString(mask);
            this.groupBind = DataContainerBase.findRootBind(typeClass);
        }

        public T create(DiscordBot bot, UniObjectNode data) {
            return groupBind.findGroupForData(data)
                    .flatMap(GroupBind::getConstructor)
                    .orElseThrow(() -> new NoSuchElementException("Could not find constructor for Entity Type " + typeClass))
                    .autoInvoke(bot, data);
        }
    }

    abstract class Base extends BotBound.DataBase implements Snowflake {
        protected Base(DiscordBot bot, @Nullable UniObjectNode initialData) {
            super(bot, initialData);

            if (bot.getCache().put(getID(), getType(), Polyfill.uncheckedCast(this)) != this)
                throw new RuntimeException("Could not replace cached instance of " + toString());
        }
    }
}
