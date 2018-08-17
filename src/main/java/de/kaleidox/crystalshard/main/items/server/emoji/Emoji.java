package de.kaleidox.crystalshard.main.items.server.emoji;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.util.Castable;

import java.util.Optional;

public interface Emoji extends DiscordItem, Nameable, Castable<Emoji> {
    default Optional<CustomEmoji> toCustomEmoji() {
        return castTo(CustomEmoji.class);
    }

    default Optional<UnicodeEmoji> toUnicodeEmoji() {
        return castTo(UnicodeEmoji.class);
    }
}
