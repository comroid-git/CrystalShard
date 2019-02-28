package de.kaleidox.crystalshard.api.entity.server.interactive;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.channel.ServerChannel;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.exception.IllegalThreadException;
import de.kaleidox.crystalshard.api.util.Castable;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;

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
