package org.comroid.crystalshard.gateway.presence;

import org.comroid.crystalshard.model.presence.Activity;
import org.comroid.crystalshard.model.presence.UserStatus;

public interface StatusUpdater {
    StatusUpdater setStatus(UserStatus status);

    StatusUpdater setAFK(boolean afkState);

    StatusUpdater addActivity(Activity activity);
}
