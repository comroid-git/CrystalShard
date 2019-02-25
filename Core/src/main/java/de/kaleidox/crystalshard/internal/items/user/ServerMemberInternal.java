package de.kaleidox.crystalshard.internal.items.user;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.user.ServerMember;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.util.markers.IDPair;

import java.awt.Color;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServerMemberInternal extends UserInternal implements ServerMember {
    private final static ConcurrentHashMap<Long, ConcurrentHashMap<Long, ServerMember>> instances = new ConcurrentHashMap<>();
    private final Server server;
    private final List<Role> roles;
    private final String nickname;
    private final boolean muted;
    private final boolean deaf;
    private final Instant joined;

    private ServerMemberInternal(Discord discord, Server server, JsonNode data) {
        super(discord.getUserCache()
                .getOrRequest(data.get("user")
                        .get("id")
                        .asLong(), data.get("user")
                        .get("id")
                        .asLong()));
        this.server = server;
        this.nickname = data.path("nick")
                .asText(null);
        this.muted = data.path("mute")
                .asBoolean(false);
        this.deaf = data.path("deaf")
                .asBoolean(false);
        Instant joined1;
        try {
            String joined_at = data.get("joined_at")
                    .asText();
            joined1 = Instant.parse(joined_at.substring(0, joined_at.length() - 7));
        } catch (DateTimeParseException ignored) {
            joined1 = Instant.now();
        }
        this.joined = joined1;
        this.roles = new ArrayList<>();
        data.path("roles")
                .forEach(roleIdNode -> roles.add(discord.getRoleCache()
                        .getOrRequest(roleIdNode.asLong(), IDPair.of(server.getId(), roleIdNode.asLong()))));

        instances.get(super.getId())
                .put(server.getId(), this);
    }

    // Override Methods
    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public Optional<String> getNickname() {
        return Optional.ofNullable(nickname);
    }

    @Override
    public boolean isMuted() {
        return muted;
    }

    @Override
    public boolean isDeafened() {
        return deaf;
    }

    @Override
    public Instant getJoinedInstant() {
        return joined;
    }

    @Override
    public Optional<Color> getRoleColor() {
        return roles.stream()
                .min(Comparator.reverseOrder())
                .map(Role::getColor);
    }

    public static ServerMember getInstance(User user, Server server) {
        instances.putIfAbsent(user.getId(), new ConcurrentHashMap<>());
        return instances.get(user.getId())
                .containsKey(server.getId()) ? instances.get(user.getId())
                .get(server.getId()) :
                CoreInjector.webRequest(ServerMember.class, user.getDiscord())
                        .setMethod(HttpMethod.GET)
                        .setUri(DiscordEndpoint.GUILD_MEMBER.createUri(server.getId(),
                                user.getId()))
                        .executeAs(node -> getInstance(user, server, node))
                        .join();
    }

    public static ServerMember getInstance(User user, Server server, JsonNode dataIfAbsent) {
        instances.putIfAbsent(user.getId(), new ConcurrentHashMap<>());
        return instances.get(user.getId())
                .getOrDefault(server.getId(), new ServerMemberInternal(user.getDiscord(), server, dataIfAbsent));
    }
}
