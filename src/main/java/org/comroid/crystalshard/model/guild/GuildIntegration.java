package org.comroid.crystalshard.model.guild;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.uniform.node.UniObjectNode;

public final class GuildIntegration extends Snowflake.Abstract {
    private GuildIntegration(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.GUILD_INTEGRATION);
    }

    public static GuildIntegration resolve(ContextualProvider context, UniObjectNode data) {
        SnowflakeCache cache = context.requireFromContext(SnowflakeCache.class);
        long id = Snowflake.ID.getFrom(data);
        return cache.getGuildIntegration(id)
                .peek(it -> it.updateFrom(data))
                .orElseGet(() -> new GuildIntegration(context, data));
    }
}
