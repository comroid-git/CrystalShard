package de.kaleidox.crystalshard.internal.items.server.interactive;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.server.interactive.Integration;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.util.markers.IDPair;

import java.time.Instant;

public class IntegrationInternal implements Integration {
    private final Discord discord;
    private final Server server;
    private final long id;
    private final String name;
    private final String type;
    private final boolean enabled;
    private final boolean syncing;
    private final long roleId;
    private final Role role;
    private final int expireBehaviour;
    private final int expireGracePeriod;
    private final User user;
    private final Account account;
    private final Instant syncedAt;

    public IntegrationInternal(Discord discord, Server server, JsonNode data) {
        this.discord = discord;
        this.server = server;
        this.id = data.get("id")
                .asLong();
        this.name = data.get("name")
                .asText();
        this.type = data.get("type")
                .asText();
        this.enabled = data.get("enabled")
                .asBoolean();
        this.syncing = data.get("syncing")
                .asBoolean();
        this.roleId = data.get("role_id")
                .asLong();
        this.role = discord.getRoleCache()
                .getOrRequest(roleId, IDPair.of(server.getId(), roleId));
        this.expireBehaviour = data.get("expire_behaviour")
                .asInt();
        this.expireGracePeriod = data.get("expire_grace_period")
                .asInt();
        this.user = discord.getUserCache()
                .getOrCreate(discord, data.get("user"));
        this.account = new Account(data.get("account"));
        this.syncedAt = Instant.parse(data.get("synced_at")
                .asText());
    }

    // Override Methods
    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isSyncing() {
        return syncing;
    }

    @Override
    public Role getSubscribersRole() {
        return role;
    }

    @Override
    public int expireBehaviour() {
        return expireBehaviour;
    }

    @Override
    public int expireGracePeriod() {
        return expireGracePeriod;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public Instant syncedAt() {
        return syncedAt;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Discord getDiscord() {
        return discord;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public class Account implements Integration.Account {
        private final String id;
        private final String name;

        private Account(JsonNode data) {
            this.id = data.get("id")
                    .asText();
            this.name = data.get("name")
                    .asText();
        }

        // Override Methods
        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
