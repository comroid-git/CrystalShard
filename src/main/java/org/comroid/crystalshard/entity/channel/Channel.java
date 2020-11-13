package org.comroid.crystalshard.entity.channel;

import org.comroid.api.IntEnum;
import org.comroid.api.Specifiable;
import org.comroid.common.info.Described;
import org.comroid.api.Named;
import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.DiscordEntity;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.model.Mentionable;
import org.comroid.crystalshard.model.guild.Invite;
import org.comroid.uniform.ValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface Channel extends DiscordEntity, Mentionable, Specifiable<Channel>, Named, Described {
    @Override
    default String getDefaultFormattedName() {
        return toString();
    }

    @Override
    default String getAlternateFormattedName() {
        return getMentionTag();
    }

    @Override
    default String getName() {
        return requireNonNull(Bind.Type).getDescription();
    }

    @Override
    default String getDescription() {
        return requireNonNull(Bind.Type).getDescription();
    }

    default DiscordEntity.Type<? extends Snowflake> getType() {
        return requireNonNull(Bind.Type);
    }

    default Optional<TextChannel> asTextChannel() {
        return as(TextChannel.class);
    }

    default Optional<VoiceChannel> asVoiceChannel() {
        return as(VoiceChannel.class);
    }

    default Optional<GuildChannel> asGuildChannel() {
        return as(GuildChannel.class);
    }

    default Optional<ChannelCategory> asChannelCategory() {
        return as(ChannelCategory.class);
    }

    default Optional<PrivateChannel> asPrivateChannel() {
        return as(PrivateChannel.class);
    }

    default Optional<GuildTextChannel> asGuildTextChannel() {
        return as(GuildTextChannel.class);
    }

    default Optional<GuildVoiceChannel> asGuildVoiceChannel() {
        return as(GuildVoiceChannel.class);
    }

    default Optional<PrivateTextChannel> asPrivateTextChannel() {
        return as(PrivateTextChannel.class);
    }

    @Internal
    void addInvite(Invite invite);

    enum Type implements IntEnum, Named, Described {
        GUILD_TEXT(0, "a text channel within a server"),
        DM(1, "a direct message between users"),
        GUILD_VOICE(2, "a voice channel within a server"),
        GROUP_DM(3, "a direct message between multiple users"),
        GUILD_CATEGORY(4, "an organizational category that contains up to 50 channels"),
        GUILD_NEWS(5, "a channel that users can follow and crosspost into their own server"),
        GUILD_STORE(6, "a channel in which game developers can sell their game on Discord");

        private final int value;
        private final String description;

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getName() {
            return name();
        }

        Type(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public static Type valueOf(int value) {
            for (Type type : values()) {
                if (type.value == value)
                    return type;
            }

            throw new NoSuchElementException("ChannelType with value " + value);
        }
    }

    interface Bind extends DiscordEntity.Bind {
        GroupBind<Channel, DiscordBot> Root = new GroupBind<>(CrystalShard.SERIALIZATION_ADAPTER, "channel");
        VarBind.TwoStage<Integer, Type> Type = Root.bind2stage("type", ValueType.INTEGER, Channel.Type::valueOf);
    }
}
