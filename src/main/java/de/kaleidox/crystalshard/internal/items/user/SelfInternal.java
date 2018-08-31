package de.kaleidox.crystalshard.internal.items.user;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.core.net.request.Endpoint;
import de.kaleidox.crystalshard.internal.core.net.request.Method;
import de.kaleidox.crystalshard.internal.core.net.request.WebRequest;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.Sendable;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.Self;
import de.kaleidox.crystalshard.main.items.user.presence.UserActivity;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.helpers.JsonHelper;

import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SelfInternal extends UserInternal implements Self {
    private final static Logger logger = new Logger(SelfInternal.class);

    public SelfInternal(Discord discord, JsonNode data) {
        super(discord, data);
    }

    @Override
    public CompletableFuture<Message> sendMessage(Sendable content) {
        throw new IllegalArgumentException("Cannot message yourself!");
    }

    @Override
    public CompletableFuture<Message> sendMessage(Consumer<Embed.Builder> defaultEmbedModifier) {
        throw new IllegalArgumentException("Cannot message yourself!");
    }

    @Override
    public CompletableFuture<Message> sendMessage(EmbedDraft embedDraft) {
        throw new IllegalArgumentException("Cannot message yourself!");
    }

    @Override
    public CompletableFuture<Message> sendMessage(String content) {
        throw new IllegalArgumentException("Cannot message yourself!");
    }

    @Override
    public CompletableFuture<Void> typing() {
        throw new IllegalArgumentException("Cannot type to yourself!");
    }

    @Override
    public CompletableFuture<Void> setName(String name) {
        return new WebRequest<Void>(this.getDiscord())
                .method(Method.PATCH)
                .endpoint(Endpoint.of(Endpoint.Location.SELF_INFO))
                .node(JsonHelper.objectNode().set("username", JsonHelper.nodeOf(name)))
                .execute(json -> null);
    }

    @Override
    public CompletableFuture<Void> setNickname(String nickname, Server inServer) {
        return null;
    }

    @Override
    public CompletableFuture<Void> setAvatar(URL avatarUrl) {
        return null;
    }

    @Override
    public CompletableFuture<Void> setPresence(UserActivity.ActivityType activityType, String description) {
        return null;
    }

    @Override
    public CompletableFuture<Void> setPresence(UserActivity.ActivityType activityType, String description, String url) {
        return null;
    }
}
