package de.kaleidox.crystalshard.internal.items.server.emoji;

import com.fasterxml.jackson.databind.JsonNode;
import com.vdurmont.emoji.EmojiParser;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.server.emoji.UnicodeEmoji;
import de.kaleidox.logging.Logger;

public class UnicodeEmojiInternal implements UnicodeEmoji {
    private final static Logger logger = new Logger(UnicodeEmojiInternal.class);
    private final DiscordInternal discord;
    private final String emojiExact;
    private final String aliases;

    public UnicodeEmojiInternal(DiscordInternal discord, JsonNode data, boolean partialData) {
        logger.deeptrace("Creating UnicodeEmoji object for data: " + data);
        this.discord = discord;
        this.emojiExact = data.get("name").asText();
        this.aliases = EmojiParser.parseToAliases(emojiExact);
    }

    @Override
    public long getId() {
        return -1;
    }

    @Override
    public String toAlias() {
        return aliases;
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
    public Discord getDiscord() {
        return null;
    }
}
