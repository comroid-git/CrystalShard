package de.kaleidox.crystalshard.api.entity.channel;

import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.EntityType;
import de.kaleidox.crystalshard.api.model.channel.ChannelType;

import org.jetbrains.annotations.Contract;

public interface GuildChannelCategory extends GuildChannel {
    @Override
    default ChannelType getChannelType() {
        return ChannelType.GUILD_CATEGORY;
    }

    @Override
    default EntityType getEntityType() {
        return EntityType.GUILD_CHANNEL_CATEGORY;
    }

    @Override
    default Optional<GuildChannelCategory> getCategory() {
        return Optional.empty();
    }

    interface Builder extends GuildChannel.Builder<GuildChannelCategory, GuildChannelCategory.Builder> {
        @Override
        default Optional<GuildChannelCategory> getCategory() {
            return Optional.empty();
        }

        @Override
        @Contract("_ -> fail")
        default Builder setGuildChannelCategory(GuildChannelCategory guildChannelCategory)
                throws IllegalArgumentException {
            throw new IllegalArgumentException("Cannot stack categories!");
        }
    }

    interface Updater extends GuildChannel.Updater<GuildChannelCategory, GuildChannelCategory.Updater> {
        @Override
        default Optional<GuildChannelCategory> getCategory() {
            return Optional.empty();
        }

        @Override
        @Contract("_ -> fail")
        default Updater setGuildChannelCategory(GuildChannelCategory guildChannelCategory)
                throws IllegalArgumentException {
            throw new IllegalArgumentException("Cannot stack categories!");
        }
    }
}
