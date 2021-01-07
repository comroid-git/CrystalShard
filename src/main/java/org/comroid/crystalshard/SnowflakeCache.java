package org.comroid.crystalshard;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.matrix.Matrix2;
import org.comroid.mutatio.ref.Processor;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniObjectNode;

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

    public Reference<Channel> getChannel(long id) {
        return getSnowflake(EntityType.CHANNEL, id);
    }

    public Reference<User> getUser(long id) {
        return getSnowflake(EntityType.USER, id);
    }

    public <T extends Snowflake> Processor<T> getSnowflake(EntityType<T> type, long id) {
        return matrix.getReference(type, id).flatMap(type.getRelatedClass());
    }
}
