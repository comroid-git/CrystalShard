package de.comroid.crystalshard.abstraction.entity;

import de.comroid.crystalshard.abstraction.serialization.AbstractJsonDeserializable;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.EntityType;
import de.comroid.crystalshard.api.entity.Snowflake;

import com.alibaba.fastjson.JSONObject;

public abstract class AbstractSnowflake<Self extends AbstractSnowflake<Self>>
        extends AbstractJsonDeserializable
        implements Snowflake {
    protected AbstractSnowflake(Discord api, JSONObject data) {
        super(api, data);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.UNKNOWN;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractSnowflake)
            return ((AbstractSnowflake) obj).getID() == getID();

        return false;
    }
}
