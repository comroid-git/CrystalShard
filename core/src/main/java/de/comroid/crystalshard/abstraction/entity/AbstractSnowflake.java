package de.comroid.crystalshard.abstraction.entity;

import de.comroid.crystalshard.abstraction.serialization.AbstractJsonDeserializable;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.EntityType;
import de.comroid.crystalshard.api.entity.Snowflake;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public abstract class AbstractSnowflake<Self extends AbstractSnowflake<Self>>
        extends AbstractJsonDeserializable
        implements Snowflake {
    protected @JsonProperty long id;

    protected AbstractSnowflake(Discord api, JsonNode data) {
        super(api, data);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.UNKNOWN;
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractSnowflake)
            return ((AbstractSnowflake) obj).id == id;

        return false;
    }
}
