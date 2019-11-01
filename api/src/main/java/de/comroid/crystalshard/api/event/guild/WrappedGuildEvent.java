package de.comroid.crystalshard.api.event.guild;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.guild.Guild;

import org.jetbrains.annotations.Nullable;

public interface WrappedGuildEvent extends GuildEvent {
    @Override
    @Nullable Guild getGuild();

    default Optional<Guild> wrapGuild() {
        return Optional.ofNullable(getGuild());
    }
}
