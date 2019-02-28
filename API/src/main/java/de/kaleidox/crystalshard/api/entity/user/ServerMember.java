package de.kaleidox.crystalshard.api.entity.user;

import de.kaleidox.crystalshard.api.entity.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.api.entity.server.permission.PermissionOverwritable;
import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.exception.DiscordPermissionException;

import java.awt.Color;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ServerMember extends User, PermissionOverwritable {
    List<Role> getRoles();

    Optional<String> getNickname();

    boolean isMuted();

    boolean isDeafened();

    Instant getJoinedInstant();

    Optional<Color> getRoleColor();

    interface Updater {
        Updater setNickname(String nickname) throws DiscordPermissionException;

        Updater addRole(Role role) throws DiscordPermissionException;

        Updater removeRole(Role role) throws DiscordPermissionException;

        Updater setMuted(boolean muted) throws DiscordPermissionException;

        Updater setDeafened(boolean deafened) throws DiscordPermissionException;

        Updater moveTo(ServerVoiceChannel channel) throws DiscordPermissionException;

        CompletableFuture<Void> update();
    }
}
