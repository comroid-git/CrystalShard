package org.comroid.crystalshard.api.model;

import java.util.concurrent.CompletableFuture;

import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.model.guild.ban.Ban;
import org.comroid.crystalshard.util.annotation.IntroducedBy;

import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

public interface Partial {
    interface GuildMember {
        @IntroducedBy(PRODUCTION)
        Guild getGuild();

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#create-guild-ban")
        CompletableFuture<Ban> ban(int deleteMessageOfLastDays, String reason);
    }
}
