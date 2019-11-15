package de.comroid.crystalshard.api.event.multipart.guild;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.event.multipart.APIEvent;

import org.jetbrains.annotations.Nullable;

public interface WrappedGuildEvent extends GuildEvent, APIEvent {
    @Override
    default @Nullable Guild getGuild() {
        return wrapGuild().orElse(null);
    }

    Optional<Guild> wrapGuild();
}
