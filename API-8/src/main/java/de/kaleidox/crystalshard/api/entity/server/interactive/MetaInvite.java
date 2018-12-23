package de.kaleidox.crystalshard.api.entity.server.interactive;

import de.kaleidox.crystalshard.api.entity.user.User;

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
