package de.kaleidox.crystalshard.api.entity.message;

import org.intellij.lang.annotations.MagicConstant;

import java.util.Optional;

public interface MessageActivity {
    @MagicConstant(valuesFromClass = Type.class)
    int getType();

    Optional<String> getPartyId();

    class Type {
        public static final int JOIN = 1;
        public static final int SPECTATE = 2;
        public static final int LISTEN = 3;
        public static final int JOIN_REQUEST = 5;
    }
}
