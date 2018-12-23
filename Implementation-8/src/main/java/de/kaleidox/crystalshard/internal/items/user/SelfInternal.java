package de.kaleidox.crystalshard.internal.items.user;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.core.net.socket.OpCode;
import de.kaleidox.crystalshard.core.net.socket.Payload;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.Self;
import de.kaleidox.crystalshard.main.items.user.presence.Presence;
import de.kaleidox.crystalshard.main.items.user.presence.UserActivity;
import de.kaleidox.util.helpers.JsonHelper;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

import static de.kaleidox.util.helpers.JsonHelper.objectNode;

public class SelfInternal extends UserInternal implements Self {
    private final static Logger logger = new Logger(SelfInternal.class);
    private Presence.Status status;
    private UserActivity.Type type;
    private String title;
    private String url;

    public SelfInternal(Discord discord, JsonNode data) {
        super(discord, data);
    }

    @Override
    public CompletableFuture<Void> setName(String name) {
        return CoreInjector.webRequest(this.getDiscord())
                .setMethod(HttpMethod.PATCH)
                .setUri(DiscordEndpoint.SELF_INFO.createUri())
                .setNode(objectNode().set("username",
                        JsonHelper.nodeOf(name)))
                .executeAs(data -> {
                    if (!discriminator.equals(data.path("discriminator")
                            .asText(discriminator))) discriminator = data.get("discriminator")
                            .asText();
                    return null;
                })
                .thenApply(n -> null);
    }

    @Override
    public CompletableFuture<Void> setNickname(String nickname, Server inServer) {
        return CoreInjector.webRequest(getDiscord())
                .setMethod(HttpMethod.PATCH)
                .setUri(DiscordEndpoint.SELF_NICKNAME.createUri(inServer))
                .setNode(objectNode("nick",
                        nickname))
                .executeAsVoid();
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
        return getDiscord().getWebSocket()
                .sendPayload(Payload.create(OpCode.STATUS_UPDATE,
                        objectNode("since",
                                (status == Presence.Status.IDLE ?
                                        System.currentTimeMillis() : null),
                                "game",
                                objectNode("type", type.getId(), "name", title,
                                        // only include an URL if there is a URL
                                        // actually
                                        // set, else include nothing
                                        (url != null ? new Object[]{"url", url} :
                                                new Object[0])),
                                "status",
                                status.getKey(),
                                "afk",
                                false)))
                .thenApply(e -> null);
    }
}
