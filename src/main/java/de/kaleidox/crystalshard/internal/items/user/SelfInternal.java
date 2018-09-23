package de.kaleidox.crystalshard.internal.items.user;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.core.net.socket.OpCode;
import de.kaleidox.crystalshard.core.net.socket.Payload;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.Sendable;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.Self;
import de.kaleidox.crystalshard.main.items.user.presence.Presence;
import de.kaleidox.crystalshard.main.items.user.presence.UserActivity;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.helpers.JsonHelper;

import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static de.kaleidox.util.helpers.JsonHelper.*;

public class SelfInternal extends UserInternal implements Self {
    private final static Logger            logger = new Logger(SelfInternal.class);
    private              Presence.Status   status;
    private              UserActivity.Type type;
    private              String            title;
    private              String            url;
    
    public SelfInternal(Discord discord, JsonNode data) {
        super(discord, data);
    }
    
    // Override Methods
    @Override
    public CompletableFuture<Message> sendMessage(Sendable content) {
        throw new AbstractMethodError("Cannot message yourself!");
    }
    
    @Override
    public CompletableFuture<Message> sendMessage(Consumer<Embed.Builder> defaultEmbedModifier) {
        throw new AbstractMethodError("Cannot message yourself!");
    }
    
    @Override
    public CompletableFuture<Message> sendMessage(EmbedDraft embedDraft) {
        throw new AbstractMethodError("Cannot message yourself!");
    }
    
    @Override
    public CompletableFuture<Message> sendMessage(String content) {
        throw new AbstractMethodError("Cannot message yourself!");
    }
    
    @Override
    public CompletableFuture<Void> typing() {
        throw new AbstractMethodError("Cannot type to yourself!");
    }
    
    @Override
    public CompletableFuture<Void> setName(String name) {
        return new WebRequest<Void>(this.getDiscord()).method(Method.PATCH)
                .endpoint(Endpoint.of(Endpoint.Location.SELF_INFO))
                .node(objectNode().set("username", JsonHelper.nodeOf(name)))
                .execute(data -> {
                    if (!discriminator.equals(data.path("discriminator")
                                                      .asText(discriminator))) discriminator = data.get("discriminator")
                            .asText();
                    return null;
                });
    }
    
    @Override
    public CompletableFuture<Void> setNickname(String nickname, Server inServer) {
        return new WebRequest<Void>(getDiscord()).method(Method.PATCH)
                .endpoint(Endpoint.Location.SELF_NICKNAME.toEndpoint(inServer))
                .node(objectNode("nick", nickname))
                .executeNull();
    }
    
    @Override
    public CompletableFuture<Void> setAvatar(URL avatarUrl) {
        return null; // todo @ Image Patch
    }
    
    @Override
    public CompletableFuture<Void> setStatus(Presence.Status status) {
        this.status = status;
        return sendStatus();
    }
    
    @Override
    public CompletableFuture<Void> setActivity(UserActivity.Type type, String title) {
        return setActivity(type, title, null);
    }
    
    @Override
    public CompletableFuture<Void> setActivity(UserActivity.Type type, String title, String url) {
        this.type = type;
        this.title = title;
        this.url = url;
        return sendStatus();
    }
    
    private CompletableFuture<Void> sendStatus() {
        return ((DiscordInternal) getDiscord()).getWebSocket()
                .sendPayload(Payload.create(OpCode.STATUS_UPDATE,
                                            objectNode("since",
                                                       (status == Presence.Status.IDLE ? System.currentTimeMillis() :
                                                        null),
                                                       "game",
                                                       objectNode("type", type.getId(), "name", title,
                                                                  // only include an
                                                                  // URL if there is a
                                                                  // URL actually set,
                                                                  // else include nothing
                                                                  (url != null ? new Object[]{"url", url} :
                                                                   new Object[0])),
                                                       "status",
                                                       status.getKey(),
                                                       "afk",
                                                       false)))
                .thenApply(e -> null);
    }
}
