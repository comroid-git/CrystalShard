package de.kaleidox.crystalshard.core.api.gateway.listener.guild.ban;

import de.kaleidox.crystalshard.core.api.gateway.event.guild.ban.GuildBanAddEvent;

@FunctionalInterface
public interface GuildBanAddListener {
    void onGuildBanAdd(GuildBanAddEvent event);
}
