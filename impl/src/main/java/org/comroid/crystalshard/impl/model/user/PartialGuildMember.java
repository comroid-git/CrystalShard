package org.comroid.crystalshard.impl.model.user;

import java.util.concurrent.CompletableFuture;

import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.entity.user.User;
import org.comroid.crystalshard.api.model.Partial;
import org.comroid.crystalshard.api.model.guild.ban.Ban;

public class PartialGuildMember implements Partial.GuildMember {
    private final User user;
    private final Guild guild;

    public PartialGuildMember(User user, Guild guild) {
        this.user = user;
        this.guild = guild;
    }

    public Guild getGuild() {
        return null;
    }

    public CompletableFuture<Ban> ban(int deleteMessageOfLastDays, String reason) {
        return null;
    }
}
