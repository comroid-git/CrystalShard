package de.kaleidox.crystalshard.api.entity.server.emoji;

import de.kaleidox.crystalshard.api.Discord;

public interface UnicodeEmoji extends Emoji {
    Discord getDiscord();

    static UnicodeEmoji of(Discord discord, String emojiOrAlias) {
        String aliases = InternalInjector.parseToAliases(emojiOrAlias);
        String unicode = InternalInjector.parseToUnicode(emojiOrAlias);
        if (aliases.equalsIgnoreCase(emojiOrAlias) && unicode.equalsIgnoreCase(emojiOrAlias))
            throw new IllegalArgumentException(
                    "The provided Emoji String is not a valid unicode emoji.");
        else return InternalInjector.newInstance(UnicodeEmoji.class, discord, aliases, unicode);
    }
}
