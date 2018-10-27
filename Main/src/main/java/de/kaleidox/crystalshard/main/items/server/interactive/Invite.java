package de.kaleidox.crystalshard.main.items.server.interactive;

import de.kaleidox.crystalshard.core.CoreDelegate;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.InternalDelegate;
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
        return CoreDelegate.webRequest(Invite.class, discord).method(Method.GET)
                .endpoint(Endpoint.Location.INVITE.toEndpoint(code))
                .node("with_counts", true)
                .execute(node -> InternalDelegate.newInstance(Invite.class, discord, node));
    }
}