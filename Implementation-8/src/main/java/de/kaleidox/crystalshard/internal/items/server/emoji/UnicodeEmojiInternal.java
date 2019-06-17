package de.kaleidox.crystalshard.internal.items.server.emoji;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.server.emoji.UnicodeEmoji;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.InternalInjector;
import de.kaleidox.crystalshard.logging.Logger;

public class UnicodeEmojiInternal implements UnicodeEmoji {
    private final static Logger logger = new Logger(UnicodeEmojiInternal.class);
    private final DiscordInternal discord;
    private final String emojiExact;
    private final String aliases;

    public UnicodeEmojiInternal(Discord discord, JsonNode data, boolean partialData) {
        logger.deeptrace("Creating UnicodeEmoji object for data: " + data);
        this.discord = (DiscordInternal) discord;
        this.emojiExact = data.get("name")
                .asText();
        this.aliases = InternalInjector.parseToAliases(emojiExact);
    }

    public UnicodeEmojiInternal(Discord discord, String aliases, String unicode) {
        logger.deeptrace("Creating UnicodeEmoji object [" + unicode + "] and aliases: [" + aliases + "]");
        this.discord = (DiscordInternal) discord;
        this.emojiExact = unicode;
        this.aliases = aliases;
    }

    // Override Methods
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UnicodeEmoji) return ((UnicodeEmoji) obj).toDiscordPrintable()
                .equalsIgnoreCase(this.toDiscordPrintable());
        if (obj instanceof String) return ((String) obj).equalsIgnoreCase(toDiscordPrintable());
        return false;
    }

    @Override
    public String toString() {
        return emojiExact;
    }

    @Override
    public String toDiscordPrintable() {
        return emojiExact;
    }

    @Override
    public String toAlias() {
        return aliases;
    }

    @Override
    public Discord getDiscord() {
        return null;
    }

    @Override
    public String getMentionTag() {
        return toDiscordPrintable();
    }
}