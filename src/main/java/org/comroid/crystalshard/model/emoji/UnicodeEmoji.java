package org.comroid.crystalshard.model.emoji;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.trie.TrieMap;
import org.comroid.varbind.container.DataContainerBase;

public final class UnicodeEmoji extends DataContainerBase<DiscordBot> implements Emoji {
    private static final TrieMap<String, Emoji> cache = TrieMap.ofString();
    private final String emoji;

    @Override
    public String getName() {
        return emoji;
    }

    private UnicodeEmoji(String emoji) {
        super(null, null);

        this.emoji = emoji;
    }

    protected static Emoji of(String emoji) {
        return cache.computeIfAbsent(emoji, UnicodeEmoji::new);
    }
}
