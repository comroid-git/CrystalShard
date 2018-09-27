package de.kaleidox.crystalshard.main.items.server.interactive;

import de.kaleidox.crystalshard.main.items.user.User;
import java.time.Instant;
import java.util.OptionalInt;

public interface MetaInvite extends Invite {
    User getInviter();
    
    int getUses();
    
    OptionalInt getMaxUses();
    
    Instant expiresAt();
    
    boolean temporaryMembership();
    
    Instant createdAt();
    
    boolean isRevoked();
}
