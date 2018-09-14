package de.kaleidox.crystalshard.internal.items.server.emoji;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.role.RoleInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class CustomEmojiInternal implements CustomEmoji {
    private final static ConcurrentHashMap<Long, CustomEmoji> instances = new ConcurrentHashMap<>();
    private final static Logger logger = new Logger(CustomEmojiInternal.class);
    private final Discord discord;
    private final Server server;
    private final boolean partialData;
    private final long id;
    private final String name;
    private final long serverId;
    private boolean requireColons;
    private boolean managed;
    private User creator;
    private List<Role> whitelistedRoles = new ArrayList<>();
    private boolean animated;

    private CustomEmojiInternal(
            Discord discord, Server server, JsonNode data, boolean partialData) {
        logger.deeptrace("Creating CustomEmoji object for data: " + data);
        this.discord = discord;
        this.server = server;
        this.serverId = server.getId();
        this.partialData = partialData;
        this.id = data.get("id").asLong();
        this.name = data.get("name").asText();
        if (!partialData) {
            data.path("role").forEach(node -> whitelistedRoles.add(RoleInternal.getInstance(server, node)));
            this.creator = data.has("user") ?
                    UserInternal.getInstance(discord, data.path("user")) : null;
            this.animated = data.path("animated").asBoolean();
            this.managed = data.path("managed").asBoolean();
            this.requireColons = data.path("require_colons").asBoolean();
        }
        instances.put(id, this);
    }

    public static CustomEmoji getInstance(Discord discord, Server server, JsonNode data, boolean partialData) {
        long id = data.get("id").asLong(-1);
        if (id == -1) throw new NoSuchElementException("No valid ID found.");
        return instances.getOrDefault(id, new CustomEmojiInternal(discord, server, data, partialData));
    }

    public static CompletableFuture<CustomEmoji> getInstance(Server server, String customEmojiMentionTag) {
        String reverse = new StringBuilder(customEmojiMentionTag).reverse().toString();
        int ind = reverse.indexOf(":");
        long id = Long.parseLong(new StringBuilder(reverse.substring(1, ind)).reverse().toString());
        return new WebRequest<CustomEmoji>(server.getDiscord())
                .method(Method.GET)
                .endpoint(Endpoint.Location.CUSTOM_EMOJI_SPECIFIC.toEndpoint(server, id))
                .execute(node -> getInstance(server.getDiscord(), server, node, false));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CustomEmoji)
            return ((CustomEmoji) obj).getId() == this.getId();
        return false;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String toAlias() {
        return name;
    }

    @Override
    public String toDiscordPrintable() {
        return "<:" + (requestIsAnimated().join() ? "a:" : "") + name + ":" + id + ">";
    }

    @Override
    public String toString() {
        return ":" + name + ":";
    }

    @Override
    public Discord getDiscord() {
        return null;
    }

    @Override
    public CompletableFuture<Void> requestAllData() {
        return new WebRequest<Void>(discord)
                .method(Method.GET)
                .endpoint(Endpoint.Location.CUSTOM_EMOJI_SPECIFIC.toEndpoint(serverId, id))
                .execute()
                .thenAccept(data -> {
                    data.path("role").forEach(node ->
                            whitelistedRoles.add(RoleInternal.getInstance(server, node)));
                    this.creator = data.has("user") ?
                            UserInternal.getInstance(discord, data.path("user")) : null;
                    this.animated = data.path("animated").asBoolean(false);
                    this.managed = data.path("managed").asBoolean(false);
                    this.requireColons = data.path("require_colons").asBoolean(false);
                });
    }

    @Override
    public User getCreator() throws NoSuchElementException {
        if (Objects.nonNull(creator))
            return creator;
        throw new NoSuchElementException("No Creator specified yet. Try using method #requestAllData first.");
    }

    @Override
    public boolean isAnimated() throws NoSuchElementException {
        if (Objects.nonNull(animated))
            return animated;
        throw new NoSuchElementException("Animated state not specified yet. Try using method #requestAllData first.");
    }

    @Override
    public boolean isManaged() throws NoSuchElementException {
        if (Objects.nonNull(managed))
            return managed;
        throw new NoSuchElementException("Managed state not specified yet. Try using method #requestAllData first.");
    }

    @Override
    public boolean requireColons() throws NoSuchElementException {
        if (Objects.nonNull(requireColons))
            return requireColons;
        throw new NoSuchElementException("Require colon state not specified yet. " +
                "Try using method #requestAllData first.");
    }

    @Override
    public CompletableFuture<User> requestCreator() {
        if (Objects.nonNull(creator))
            return CompletableFuture.completedFuture(creator);
        return new WebRequest<User>(discord)
                .method(Method.GET)
                .endpoint(Endpoint.Location.CUSTOM_EMOJI_SPECIFIC.toEndpoint(serverId, id))
                .execute(node -> UserInternal.getInstance(discord, node.get("user")));
    }

    @Override
    public CompletableFuture<Boolean> requestIsAnimated() {
        if (Objects.nonNull(animated))
            return CompletableFuture.completedFuture(animated);
        return new WebRequest<Boolean>(discord)
                .method(Method.GET)
                .endpoint(Endpoint.Location.CUSTOM_EMOJI_SPECIFIC.toEndpoint(serverId, id))
                .execute(node -> {
                    this.animated = node.get("animated").asBoolean();
                    return animated;
                });
    }

    @Override
    public CompletableFuture<Boolean> requestIsManaged() {
        if (Objects.nonNull(managed))
            return CompletableFuture.completedFuture(managed);
        return new WebRequest<Boolean>(discord)
                .method(Method.GET)
                .endpoint(Endpoint.Location.CUSTOM_EMOJI_SPECIFIC.toEndpoint(serverId, id))
                .execute(node -> {
                    this.managed = node.get("managed").asBoolean();
                    return managed;
                });
    }

    @Override
    public CompletableFuture<Boolean> requestRequireColons() {
        if (Objects.nonNull(requireColons))
            return CompletableFuture.completedFuture(requireColons);
        return new WebRequest<Boolean>(discord)
                .method(Method.GET)
                .endpoint(Endpoint.Location.CUSTOM_EMOJI_SPECIFIC.toEndpoint(serverId, id))
                .execute(node -> {
                    this.requireColons = node.get("require_colons").asBoolean();
                    return requireColons;
                });
    }

    @Override
    public String getMentionTag() {
        return toDiscordPrintable();
    }
}
