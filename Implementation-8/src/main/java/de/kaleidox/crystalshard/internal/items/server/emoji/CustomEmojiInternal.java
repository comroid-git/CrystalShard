package de.kaleidox.crystalshard.internal.items.server.emoji;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.util.markers.IDPair;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class CustomEmojiInternal implements CustomEmoji {
    private final static ConcurrentHashMap<Long, CustomEmoji> instances = new ConcurrentHashMap<>();
    private final static Logger logger = new Logger(CustomEmojiInternal.class);
    private final Discord discord;
    private final Server server;
    private final boolean partialData;
    private final long id;
    private final long serverId;
    private String name;
    private boolean requireColons;
    private boolean managed;
    private User creator;
    private List<Role> whitelistedRoles = new ArrayList<>();
    private boolean animated;

    public CustomEmojiInternal(Discord discord, Server server, JsonNode data, boolean partialData) {
        logger.deeptrace("Creating CustomEmoji object for data: " + data);
        this.discord = discord;
        this.server = server;
        this.serverId = server.getId();
        this.partialData = partialData;
        this.id = data.get("id")
                .asLong();
        this.name = data.get("name")
                .asText();
        if (!partialData) {
            data.path("role")
                    .forEach(node -> whitelistedRoles.add(discord.getRoleCache()
                            .getOrCreate(discord, server, node)));
            this.creator = data.has("user") ? discord.getUserCache()
                    .getOrCreate(discord, data.path("user")) : null;
            this.animated = data.path("animated")
                    .asBoolean();
            this.managed = data.path("managed")
                    .asBoolean();
            this.requireColons = data.path("require_colons")
                    .asBoolean();
        }
        instances.put(id, this);
    }

    // Override Methods
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CustomEmoji) return ((CustomEmoji) obj).getId() == this.getId();
        return false;
    }

    @Override
    public String toString() {
        return ":" + name + ":";
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public CompletableFuture<Void> requestAllData() {
        return CoreInjector.webRequest(discord)
                .setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.CUSTOM_EMOJI_SPECIFIC.createUri(serverId, id))
                .executeAsNode()
                .thenAccept(
                        data -> {
                            data.path("role")
                                    .forEach(node -> whitelistedRoles.add(discord.getRoleCache()
                                            .getOrCreate(discord, server, node)));
                            this.creator = data.has("user") ? discord.getUserCache()
                                    .getOrCreate(discord, data.path("user")) : null;
                            this.animated = data.path("animated")
                                    .asBoolean(false);
                            this.managed = data.path("managed")
                                    .asBoolean(false);
                            this.requireColons = data.path("require_colons")
                                    .asBoolean(false);
                        });
    }

    @Override
    public User getCreator() throws NoSuchElementException {
        if (Objects.nonNull(creator)) return creator;
        throw new NoSuchElementException("No Creator specified yet. Try using method #requestAllData first.");
    }

    @Override
    public boolean isAnimated() throws NoSuchElementException {
        if (Objects.nonNull(animated)) return animated;
        throw new NoSuchElementException("Animated state not specified yet. Try using method #requestAllData first.");
    }

    @Override
    public boolean isManaged() throws NoSuchElementException {
        if (Objects.nonNull(managed)) return managed;
        throw new NoSuchElementException("Managed state not specified yet. Try using method #requestAllData first.");
    }

    @Override
    public boolean requireColons() throws NoSuchElementException {
        if (Objects.nonNull(requireColons)) return requireColons;
        throw new NoSuchElementException("Require colon state not specified yet. " + "Try using method #requestAllData first.");
    }

    @Override
    public CompletableFuture<User> requestCreator() {
        if (Objects.nonNull(creator)) return CompletableFuture.completedFuture(creator);
        return CoreInjector.webRequest(User.class, discord)
                .setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.CUSTOM_EMOJI_SPECIFIC.createUri(serverId, id))
                .executeAs(node -> discord.getUserCache()
                        .getOrCreate(discord, node.get("user")));
    }

    @Override
    public CompletableFuture<Boolean> requestIsAnimated() {
        if (Objects.nonNull(animated)) return CompletableFuture.completedFuture(animated);
        return CoreInjector.webRequest(Boolean.class, discord)
                .setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.CUSTOM_EMOJI_SPECIFIC.createUri(serverId, id))
                .executeAs(node -> {
                    this.animated = node.get("animated")
                            .asBoolean();
                    return animated;
                });
    }

    @Override
    public CompletableFuture<Boolean> requestIsManaged() {
        if (Objects.nonNull(managed)) return CompletableFuture.completedFuture(managed);
        return CoreInjector.webRequest(Boolean.class, discord)
                .setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.CUSTOM_EMOJI_SPECIFIC.createUri(serverId, id))
                .executeAs(node -> {
                    this.managed = node.get("managed")
                            .asBoolean();
                    return managed;
                });
    }

    @Override
    public CompletableFuture<Boolean> requestRequireColons() {
        if (Objects.nonNull(requireColons)) return CompletableFuture.completedFuture(requireColons);
        return CoreInjector.webRequest(Boolean.class, discord)
                .setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.CUSTOM_EMOJI_SPECIFIC.createUri(serverId, id))
                .executeAs(node -> {
                    this.requireColons = node.get("require_colons")
                            .asBoolean();
                    return requireColons;
                });
    }

    @Override
    public String toDiscordPrintable() {
        return "<:" + (requestIsAnimated().join() ? "a:" : "") + name + ":" + id + ">";
    }

    @Override
    public String toAlias() {
        return name;
    }

    @Override
    public Discord getDiscord() {
        return null;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getMentionTag() {
        return toDiscordPrintable();
    }

    @Override
    public Cache<CustomEmoji, Long, IDPair> getCache() {
        return discord.getEmojiCache();
    }

    public Set<EditTrait<CustomEmoji>> updateData(JsonNode data) {
        return null;
    }
}