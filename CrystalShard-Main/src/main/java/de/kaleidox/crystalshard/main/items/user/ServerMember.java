package de.kaleidox.crystalshard.main.items.user;

import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.items.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverwritable;
import de.kaleidox.crystalshard.main.items.role.Role;

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
