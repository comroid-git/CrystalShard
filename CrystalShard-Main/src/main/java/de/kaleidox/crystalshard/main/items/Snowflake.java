package de.kaleidox.crystalshard.main.items;

import de.kaleidox.crystalshard.internal.items.SnowflakeInternal;
import java.time.Instant;

public interface Snowflake {
    // Static Fields
    long DISCORD_EPOCH = 1420070400000L;
    
    Instant getCreatedTimestamp();
    
    long getId();
    
    // Static members
    static Snowflake of(long id) {
        return SnowflakeInternal.of(id);
    }
    
    static boolean canBeSnowflake(long id) {
        return Snowflake.of(id)
                       .getCreatedTimestamp()
                       .toEpochMilli() < DISCORD_EPOCH;
    }
}
