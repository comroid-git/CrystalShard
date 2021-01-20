package org.comroid.crystalshard.model.presence;

import org.comroid.api.Named;
import org.comroid.api.Rewrapper;
import org.comroid.api.ValueBox;
import org.comroid.api.ValueType;
import org.comroid.common.info.Described;
import org.comroid.util.StandardValueType;

public enum UserStatus implements Named, Described, ValueBox<String> {
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

    @Override
    public String getValue() {
        return getIdent();
    }

    @Override
    public ValueType<? extends String> getHeldType() {
        return StandardValueType.STRING;
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

    public String getIdent() {
        return ident;
    }
}
