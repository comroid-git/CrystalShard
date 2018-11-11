package de.kaleidox.crystalshard.main.items.server.emoji;

import com.vdurmont.emoji.EmojiParser;
import de.kaleidox.crystalshard.internal.InternalDelegate;
import de.kaleidox.crystalshard.main.Discord;

public interface UnicodeEmoji extends Emoji {
    Discord getDiscord();

    static UnicodeEmoji of(Discord discord, String emojiOrAlias) {
        String aliases = EmojiParser.parseToAliases(emojiOrAlias);
        String unicode = EmojiParser.parseToUnicode(emojiOrAlias);
        if (aliases.equalsIgnoreCase(emojiOrAlias) && unicode.equalsIgnoreCase(emojiOrAlias))
            throw new IllegalArgumentException(
                    "The provided Emoji String is not a valid unicode emoji.");
        else return InternalDelegate.newInstance(UnicodeEmoji.class, discord, aliases, unicode);
    }
}
