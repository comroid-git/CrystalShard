package de.kaleidox.crystalshard.internal.items.role;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.permission.PermissionOverride;
import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.items.permission.PermissionOverrideInternal;

import java.awt.Color;
import java.util.concurrent.CompletableFuture;

import static de.kaleidox.util.helpers.JsonHelper.objectNode;

public class RoleBuilderInternal implements Role.Builder {
    private final Discord discord;
    private final Server server;
    private String name;
    private PermissionOverrideInternal override;
    private Color color;
    private boolean hoist;
    private boolean mentionable;

    public RoleBuilderInternal(Discord discord, Server server) {
        this.discord = discord;
        this.server = server;
        this.name = "new role";
        this.override = (PermissionOverrideInternal) server.getEveryoneRole()
                .getPermissions()
                .toOverride();
        this.color = null;
        this.hoist = false;
        this.mentionable = false;
    }

    // Override Methods
    @Override
    public Role.Builder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Role.Builder setPermissions(PermissionOverride permissionOverride) {
        this.override = (PermissionOverrideInternal) permissionOverride;
        return this;
    }

    @Override
    public Role.Builder setColor(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public Role.Builder setHoist(boolean hoist) {
        this.hoist = hoist;
        return this;
    }

    @Override
    public Role.Builder setMentionable(boolean mentionable) {
        this.mentionable = mentionable;
        return this;
    }

    @Override
    public CompletableFuture<Role> build() {
        return CoreInjector.webRequest(Role.class, discord)
                .setMethod(HttpMethod.POST)
                .setUri(DiscordEndpoint.GUILD_ROLES.createUri(server))
                .setNode(toJsonNode())
                .executeAs(node -> discord.getRoleCache()
                        .getOrCreate(discord, server, node));
    }

    public JsonNode toJsonNode() {
        return objectNode("name",
                name,
                "permissions",
                override.toPermissionInt(),
                "color",
                (color == null ? 0 : color.getRGB()),
                "hoist",
                hoist,
                "mentionable",
                mentionable);
    }
}