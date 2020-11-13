package org.comroid.crystalshard.core.cache;

import org.comroid.common.info.MessageSupplier;
import org.comroid.crystalshard.entity.DiscordEntity.Type;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.trie.TrieMap;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SnowflakeSelector implements Snowflake {
    public static final Pattern KEY_PATTERN = Pattern.compile("([(\\[{]id=(?<id>\\d+),? ?t(ype)?=(?<type>[\\w]+)[)\\]}])");
    static final Map<String, SnowflakeSelector> cache = TrieMap.ofString();

    private final long id;
    private final Type<? extends Snowflake> type;
    private final String key;

    @Override
    public long getID() {
        return id;
    }

    public Type<? extends Snowflake> getType() {
        return type;
    }

    private SnowflakeSelector(long id, Type<? extends Snowflake> type) {
        this.id = id;
        this.type = type;
        this.key = generateKey(id, type);

        cache.put(key, this);
    }

    public static SnowflakeSelector ofString(String key) {
        return cache.computeIfAbsent(key, k -> {
            final Matcher matcher = KEY_PATTERN.matcher(k);

            if (matcher.matches()) {
                long id = Long.parseLong(matcher.group("id"));
                String typeName = matcher.group("type");
                Type<? extends Snowflake> type = Type.valueOfName(typeName)
                        .requireNonNull(MessageSupplier.format("Type not found by name %s", typeName));

                return new SnowflakeSelector(id, type);
            } else
                throw new IllegalArgumentException(String.format("String [ %s ] does not match pattern: %s", key, KEY_PATTERN));
        });
    }

    public static String generateKey(long id, Type<? extends Snowflake> type) {
        return String.format("{id=%d, type=%s}", id, type);
    }

    @Override
    public String toString() {
        return key;
    }
}
