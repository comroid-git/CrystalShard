package de.kaleidox.crystalshard.main.items.server.interactive;

import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.InternalInjector;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.IllegalThreadException;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.util.Castable;

import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Invite extends Castable<Invite> {
    String BASE_INVITE = "https://discord.gg/";

    Discord getDiscord();

    String getInviteCode();

    Optional<Server> getServer();

    ServerChannel getChannel();

    int getApproximateOnlineCount();

    int getApproximateMemberCount();

    URL getUrl();

    CompletableFuture<Void> delete();

    static CompletableFuture<Invite> get(String code) throws IllegalThreadException {
        Discord discord = ThreadPool.getThreadDiscord();
        return CoreInjector.webRequest(Invite.class, discord)
                .setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.INVITE.createUri(code))
                .setNode("with_counts", true)
                .executeAs(node -> InternalInjector.newInstance(Invite.class, discord, node));
    }
}
