package de.comroid.crystalshard.api.event.guild;

import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.event.model.Event;

public interface GuildEvent extends Event {
    Guild getGuild();
}
