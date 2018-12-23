package de.kaleidox.crystalshard.api.entity.server.interactive;

import de.kaleidox.crystalshard.api.entity.DiscordItem;
import de.kaleidox.crystalshard.api.entity.Nameable;
import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.user.User;

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
