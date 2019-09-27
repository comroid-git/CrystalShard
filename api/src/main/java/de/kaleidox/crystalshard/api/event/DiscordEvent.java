package de.kaleidox.crystalshard.api.event;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.event.model.Event;

import org.jetbrains.annotations.NotNull;

public interface DiscordEvent extends Event {
    @NotNull Discord getDiscord();
}
