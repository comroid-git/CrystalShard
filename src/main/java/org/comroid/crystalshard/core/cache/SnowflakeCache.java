package org.comroid.crystalshard.core.cache;

import org.comroid.api.Junction;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.trie.TrieMap;
import org.comroid.varbind.DataContainerCache;
import org.comroid.varbind.bind.VarBind;

public final class SnowflakeCache<F extends Snowflake> extends DataContainerCache<Long, F, DiscordBot> implements BotBound {
    private final DiscordBot bot;

    @Override
    public DiscordBot getBot() {
        return bot;
    }

    public SnowflakeCache(DiscordBot bot, VarBind<?, ? super DiscordBot, ?, Long> idBind) {
        super(
                250,
                new TrieMap.Basic<>(Junction.of(String::valueOf, Long::parseLong), true),
                idBind,
                bot
        );

        this.bot = bot;
    }
}
