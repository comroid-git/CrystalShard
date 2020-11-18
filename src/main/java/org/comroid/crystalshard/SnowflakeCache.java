package org.comroid.crystalshard;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.matrix.Matrix2;

public final class SnowflakeCache implements ContextualProvider.Underlying {
    public final Matrix2<EntityType, Long, Snowflake> matrix = Matrix2.create();
    private final ContextualProvider context;

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return context.plus(this);
    }

    SnowflakeCache(ContextualProvider context) {
        this.context = context;
    }
}
