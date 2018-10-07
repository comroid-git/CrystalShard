package de.kaleidox.crystalshard.internal.items.user;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.ServerMember;
import de.kaleidox.crystalshard.main.items.user.User;
import util.objects.markers.IDPair;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServerMemberInternal extends UserInternal implements ServerMember {
    private final static ConcurrentHashMap<Long, ConcurrentHashMap<Long, ServerMember>> instances = new ConcurrentHashMap<>();
    private final        Server                                                         server;
    private final        List<Role>                                                     roles;
    private final        String                                                         nickname;
    private final        boolean                                                        muted;
    private final        boolean                                                        deaf;
    private final        Instant                                                        joined;
    
    private ServerMemberInternal(Discord discord, Server server, JsonNode data) {
        super(discord.getUserCache().getOrRequest(data.get("user").get("id").asLong(), data.get("user").get("id").asLong()));
        this.server = server;
        this.nickname = data.path("nick").asText(null);
        this.muted = data.path("mute").asBoolean(false);
        this.deaf = data.path("deaf").asBoolean(false);
        Instant joined1;
        try {
            String joined_at = data.get("joined_at").asText();
            joined1 = Instant.parse(joined_at.substring(0, joined_at.length() - 7));
        } catch (DateTimeParseException ignored) {
            joined1 = Instant.now();
        }
        this.joined = joined1;
        this.roles = new ArrayList<>();
        data.path("roles").forEach(roleIdNode -> roles.add(discord.getRoleCache()
                                                                   .getOrRequest(roleIdNode.asLong(), IDPair.of(server.getId(), roleIdNode.asLong()))));
        
        instances.get(super.getId()).put(server.getId(), this);
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
    
// Static membe
    // Static members
    public static ServerMember getInstance(User user, Server server) {
        instances.putIfAbsent(user.getId(), new ConcurrentHashMap<>());
        return instances.get(user.getId()).containsKey(server.getId()) ? instances.get(user.getId()).get(server.getId()) :
               new WebRequest<ServerMember>(user.getDiscord()).method(Method.GET)
                       .endpoint(Endpoint.Location.GUILD_MEMBER.toEndpoint(server.getId(),
                                                                           user.getId()))
                       .execute(node -> getInstance(user, server, node))
                       .join();
    }
    
    public static ServerMember getInstance(User user, Server server, JsonNode dataIfAbsent) {
        instances.putIfAbsent(user.getId(), new ConcurrentHashMap<>());
        return instances.get(user.getId()).getOrDefault(server.getId(), new ServerMemberInternal(user.getDiscord(), server, dataIfAbsent));
    }
}
