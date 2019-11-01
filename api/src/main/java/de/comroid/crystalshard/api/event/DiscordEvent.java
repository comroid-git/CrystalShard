package de.comroid.crystalshard.api.event;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.event.model.Event;

import org.jetbrains.annotations.NotNull;

public interface DiscordEvent extends Event {
    @NotNull Discord getDiscord();
}
