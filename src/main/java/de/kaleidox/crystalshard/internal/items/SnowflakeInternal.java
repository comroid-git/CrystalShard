package de.kaleidox.crystalshard.internal.items;

import de.kaleidox.crystalshard.main.items.Snowflake;
import java.time.Instant;

public class SnowflakeInternal implements Snowflake {
    private final Instant createdInstant;
    private final long    id;
    
    private SnowflakeInternal(long id) {
        this.id = id;
        this.createdInstant = Instant.ofEpochMilli((id >> 22) + DISCORD_EPOCH);
    }
    
    @Override
    public Instant getCreatedTimestamp() {
        return createdInstant;
    }
    
    @Override
    public long getId() {
        return id;
    }
    
    public static Snowflake of(long id) {
        return new SnowflakeInternal(id);
    }
}
