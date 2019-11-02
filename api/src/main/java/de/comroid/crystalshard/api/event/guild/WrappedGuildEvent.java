package de.comroid.crystalshard.api.event.guild;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.guild.Guild;

import org.jetbrains.annotations.Nullable;

public interface WrappedGuildEvent extends GuildEvent {
    @Override
    default @Nullable Guild getTriggeringGuild() {
        return wrapTriggeringGuild().orElse(null);
    }

    Optional<Guild> wrapTriggeringGuild();
}
