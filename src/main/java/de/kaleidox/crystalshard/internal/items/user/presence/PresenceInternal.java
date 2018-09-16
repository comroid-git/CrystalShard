package de.kaleidox.crystalshard.internal.items.user.presence;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.ServerMember;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.main.items.user.presence.Presence;
import de.kaleidox.crystalshard.main.items.user.presence.UserActivity;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PresenceInternal implements Presence {
    private final static ConcurrentHashMap<String, PresenceInternal> instances = new ConcurrentHashMap<>();
    private final User user;
    private final Server server;
    private UserActivity game;
    private Status status;

    private PresenceInternal(Discord discord, Server server, JsonNode data) {
        this.user = UserInternal.getInstance(discord, data.get("user").get("id").asLong());
        this.server = server;
        this.game = data.has("game") ?
                new UserActivityInternal(data.get("game")) : null;
        this.status = Status.getFromKey(data.get("status").asText());

        instances.put(server.getId() + "/" + user.getId(), this);
    }

    public static Presence getInstance(Discord discord, JsonNode data) {
        long id = data.get("user").get("id").asLong(-1);
        assert id != -1 : "No valid ID found.";
        Server server = ServerInternal.getInstance(discord, data.get("guild_id").asLong());
        return instances.getOrDefault(server.getId() + "/" + id, new PresenceInternal(discord, server, data))
                .updateData(data);
    }

    private Presence updateData(JsonNode data) {
        this.game = data.has("game") ?
                new UserActivityInternal(data.get("game")) : null;
        this.status = Status.getFromKey(data.get("status").asText());
        return this;
    }

    @Override
    public ServerMember getUser() {
        return user.toServerMember(server);
    }

    @Override
    public Optional<UserActivity> getActivity() {
        return Optional.ofNullable(game);
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
