package de.kaleidox.crystalshard.main.items.message;

import java.util.Optional;

public interface MessageActivity {
    MessageActivityType getType();

    Optional<String> getPartyId();
}
