package de.kaleidox.crystalshard.api.model.message;

// https://discordapp.com/developers/docs/resources/channel#message-object-message-activity-structure
// https://discordapp.com/developers/docs/resources/channel#message-object-message-activity-types

import java.util.Optional;

public interface MessageActivity {
    Type getType();

    Optional<String> getPartyID();

    enum Type {
        JOIN(1),
        SPECTATE(2),
        LISTEN(3),
        JOIN_REQUEST(5);

        public final int value;

        Type(int value) {
            this.value = value;
        }
    }
}
