package org.comroid.crystalshard.entity;

import org.comroid.api.BitmaskEnum;
import org.comroid.api.Named;
import org.comroid.util.Bitmask;

public enum EntityType implements Named, BitmaskEnum<EntityType> {
    SNOWFLAKE,

    USER(SNOWFLAKE),

    GUILD(SNOWFLAKE),

    CHANNEL(SNOWFLAKE),

    TEXT_CHANNEL(CHANNEL),
    VOICE_CHANNEL(CHANNEL),

    PRIVATE_CHANNEL(TEXT_CHANNEL),
    GROUP_CHANNEL(TEXT_CHANNEL),

    GUILD_CHANNEL(CHANNEL),
    GUILD_TEXT_CHANNEL(GUILD_CHANNEL, TEXT_CHANNEL),
    GUILD_VOICE_CHANNEL(GUILD_CHANNEL, VOICE_CHANNEL),

    MESSAGE(SNOWFLAKE);

    private final int value;

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name();
    }

    EntityType(EntityType... inherits) {
        this.value = Bitmask.combine(Bitmask.combine(inherits), Bitmask.nextFlag());
    }
}
