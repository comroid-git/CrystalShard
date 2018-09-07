package de.kaleidox.crystalshard.main.items.server.emoji;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.message.Message;

public interface Emoji extends DiscordItem {
    /**
     * Gets the ID of a CustomEmoji or {@code -1} if the emoji is a Unicode Emoji.
     *
     * @return The id.
     */
    long getId();

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
    String toString();

    /**
     * Returns a String for a {@link Message} that will get replaced by Discord with a visual of this emoji.
     *
     * @return A discord-printable string of this emoji.
     */
    String toDiscordPrintable();
}
