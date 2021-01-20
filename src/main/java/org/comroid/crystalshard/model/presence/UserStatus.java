package org.comroid.crystalshard.model.presence;

import org.comroid.api.Named;
import org.comroid.api.Rewrapper;
import org.comroid.common.info.Described;

public enum UserStatus implements Named, Described {
    ONLINE("online", "Online"),
    DO_NOT_DISTURB("dnd", "Do Not Disturb"),
    IDLE("idle", "AFK"),
    INVISIBLE("invisible", "Invisible and shown as offline"),
    OFFLINE("offline", "Offline");

    private final String ident;
    private final String description;

    @Override
    public String getDescription() {
        return description;
    }

    UserStatus(String ident, String description) {
        this.ident = ident;
        this.description = description;
    }

    public static Rewrapper<UserStatus> byIdent(String ident) {
        for (UserStatus each : values())
            if (each.ident.equals(ident))
                return () -> each;
        return Rewrapper.empty();
    }
}
