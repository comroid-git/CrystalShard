package de.kaleidox.crystalshard.main.items.server.emoji;

import com.vdurmont.emoji.EmojiParser;
import de.kaleidox.crystalshard.internal.items.server.emoji.UnicodeEmojiInternal;
import de.kaleidox.crystalshard.main.Discord;

public interface UnicodeEmoji extends Emoji {
    Discord getDiscord();
    
// Static membe
    static UnicodeEmoji of(Discord discord, String emojiOrAlias) {
        String aliases = EmojiParser.parseToAliases(emojiOrAlias);
        String unicode = EmojiParser.parseToUnicode(emojiOrAlias);
        if (aliases.equalsIgnoreCase(emojiOrAlias) && unicode.equalsIgnoreCase(emojiOrAlias))
            throw new IllegalArgumentException("The provided Emoji String is not a valid unicode emoji.");
        else return new UnicodeEmojiInternal(discord, aliases, unicode);
    }
}
