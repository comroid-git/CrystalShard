package de.kaleidox.crystalshard.main.items.server.interactive;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;

import java.time.Instant;

public interface Integration extends DiscordItem, Nameable {
    String getType();
    
    boolean isEnabled();
    
    boolean isSyncing();
    
    Role getSubscribersRole();
    
    int expireBehaviour();
    
    int expireGracePeriod();
    
    User getUser();
    
    Account getAccount();
    
    Instant syncedAt();
    
    Server getServer();
    
    interface Account extends Nameable {
        String getId();
    }
}
