package de.kaleidox.crystalshard.main.items.server.emoji;

import com.vdurmont.emoji.EmojiParser;
import de.kaleidox.crystalshard.main.items.Mentionable;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.util.Castable;

import java.util.Optional;

public interface Emoji extends Mentionable, Castable<Emoji> {
    /**
     * Gets the alias or name of this emoji.
     *
     * @return The alias of the emoji.
     */
    String toAlias();

    /**
     * Gets the name of this emoji.
     *
     * @return The name of the emoji.
     */
    default String getName() {
        return this.toAlias();
    }

    /**
     * Gets the universal replacement of this emoji.
     *
     * @return universal replacement of the emoji.
     */
    @Override
    String toString();

    @Override
    boolean equals(Object obj);

    /**
     * Returns a String for a {@link Message} that will get replaced by Discord with a visual of this emoji.
     * This method is analogous to {@link Mentionable#getMentionTag()}.
     *
     * @return A discord-printable string of this emoji.
     */
    String toDiscordPrintable();

    default Optional<UnicodeEmoji> toUnicodeEmoji() {
        return castTo(UnicodeEmoji.class);
    }

    default Optional<CustomEmoji> toCustomEmoji() {
        return castTo(CustomEmoji.class);
    }

    static Emoji of(String anyEmoji) {
        String s = EmojiParser.parseToAliases(anyEmoji);
        if (s.equalsIgnoreCase(anyEmoji)) {
            // is likely customEmoji
        } else {
            // is likely unicodeEmoji
        }
        return null; // todo
    }
}
