package de.kaleidox.crystalshard.main.items.server.emoji;

public interface UnicodeEmoji extends Emoji {
    /**
     * Always returns {@code -1} because UnicodeEmojis can't have IDs.
     *
     * @return Always -1.
     */
    long getId();
}
