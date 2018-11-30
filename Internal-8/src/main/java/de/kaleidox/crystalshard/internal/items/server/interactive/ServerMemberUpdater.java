package de.kaleidox.crystalshard.internal.items.server.interactive;

import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.ServerMember;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ServerMemberUpdater implements ServerMember.Updater {
    private final Server server;
    private final ServerMember member;
    private final List<Role> roles;
    private final Discord discord;
    private String nickname;
    private Boolean muted;
    private Boolean deafened;
    private ServerVoiceChannel channel;

    public ServerMemberUpdater(ServerMember member) {
        this.discord = member.getDiscord();
        this.server = member.getServer();
        this.member = member;
        this.roles = member.getRoles();
    }

    // Override Methods
    @Override
    public ServerMember.Updater setNickname(String nickname) throws DiscordPermissionException {
        if (!server.hasPermission(discord, Permission.MANAGE_NICKNAMES))
            throw new DiscordPermissionException("Cannot change nickname!",
                    Permission.MANAGE_NICKNAMES);
        this.nickname = nickname;
        return this;
    }

    @Override
    public ServerMember.Updater addRole(Role role) throws DiscordPermissionException {
        if (!server.hasPermission(discord, Permission.MANAGE_ROLES))
            throw new DiscordPermissionException("Cannot modify roles!", Permission.MANAGE_ROLES);
        if (role.equals(server.getEveryoneRole()))
            throw new IllegalArgumentException("Cannot add @everyone Role to a user!");
        this.roles.add(role);
        return this;
    }

    @Override
    public ServerMember.Updater removeRole(Role role) throws DiscordPermissionException {
        if (!server.hasPermission(discord, Permission.MANAGE_ROLES))
            throw new DiscordPermissionException("Cannot modify roles!", Permission.MANAGE_ROLES);
        if (role.equals(server.getEveryoneRole()))
            throw new IllegalArgumentException("Cannot remove @everyone Role from a user!");
        this.roles.remove(role);
        return this;
    }

    @Override
    public ServerMember.Updater setMuted(boolean muted) throws DiscordPermissionException {
        if (!server.hasPermission(discord, Permission.MUTE_MEMBERS))
            throw new DiscordPermissionException("Cannot mute other users!", Permission.MUTE_MEMBERS);
        this.muted = muted;
        return this;
    }

    @Override
    public ServerMember.Updater setDeafened(boolean deafened) throws DiscordPermissionException {
        if (!server.hasPermission(discord, Permission.DEAFEN_MEMBERS))
            throw new DiscordPermissionException("Cannot deafen other nickname!",
                    Permission.DEAFEN_MEMBERS);
        this.deafened = deafened;
        return this;
    }

    @Override
    public ServerMember.Updater moveTo(ServerVoiceChannel channel) throws DiscordPermissionException {
        if (!server.hasPermission(discord, Permission.MOVE_MEMBERS))
            throw new DiscordPermissionException("Cannot move other users!", Permission.MOVE_MEMBERS);
        // todo Add ServerVoiceChannel#hasPermission(JOIN) check
        this.channel = channel;
        return this;
    }

    @Override
    public CompletableFuture<Void> update() {
        return CoreInjector.webRequest(discord)
                .setMethod(HttpMethod.PATCH)
                .setUri(DiscordEndpoint.GUILD_MEMBER.createUri(server, member))
                .setNode((nickname != null ? new Object[]{"nick",
                                nickname} : new Object[0]),
                        (muted != null ? new Object[]{"mute",
                                muted} : new Object[0]),
                        (deafened != null ? new Object[]{"deaf",
                                deafened} : new Object[0]),
                        (channel != null ? new Object[]{"channel_id",
                                channel.getId()} : new Object[0]),
                        (roles != null ? new Object[]{"roles",
                                roles.stream()
                                        .map(DiscordItem::getId).collect(Collectors.toList())} : new Object[0]))
                .executeAsVoid();
    }
}
