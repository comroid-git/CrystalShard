package org.comroid.crystalshard.gateway.presence;

import org.comroid.crystalshard.DiscordBotBase;
import org.comroid.crystalshard.DiscordBotShard;
import org.comroid.mutatio.ref.ReferenceList;

import java.util.concurrent.CompletableFuture;

public final class BotBasedPresence extends AbstractPresence {
    private final ReferenceList<DiscordBotShard> shards;

    public BotBasedPresence(DiscordBotBase bot, ReferenceList<DiscordBotShard> shards) {
        super(bot);

        this.shards = shards;
    }

    @Override
    public CompletableFuture<Void> update() {
        return CompletableFuture.allOf(shards.stream()
                .map(DiscordBotShard::getOwnPresence)
                .map(presence -> {
                    presence.setAFK(afkState);
                    presence.setStatus(status);
                    activities.forEach(presence::addActivity);
                    return presence.update();
                })
                .toArray(CompletableFuture[]::new)
        );
    }
}
