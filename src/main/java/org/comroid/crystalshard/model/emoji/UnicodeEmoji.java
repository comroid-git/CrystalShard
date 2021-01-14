package org.comroid.crystalshard.model.emoji;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class UnicodeEmoji implements Emoji {
    private static final Map<String, UnicodeEmoji> cache = new ConcurrentHashMap<>();
    private final String emoji;

    @Override
    public String getName() {
        return emoji;
    }

    private UnicodeEmoji(String emoji) {
        if (emoji.length() > 4)
            throw new IllegalArgumentException("Not a unicode emoji: " + emoji);
        this.emoji = emoji;
    }

    public static UnicodeEmoji getInstance(String emoji) {
        return cache.computeIfAbsent(emoji, UnicodeEmoji::new);
    }
}
