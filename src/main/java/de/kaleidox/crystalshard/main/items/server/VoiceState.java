package de.kaleidox.crystalshard.main.items.server;

import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.VoiceChannel;
import de.kaleidox.crystalshard.main.items.user.User;

import java.util.Optional;

public interface VoiceState {
    Discord getDiscord();
    
    Optional<Server> getServer();
    
    VoiceChannel getChannel();
    
    User getUser();
    
    boolean isDeafened();
    
    boolean isMuted();
    
    boolean isSelfDeafened();
    
    boolean isSelfMuted();
    
    boolean isSuppressed();
}
