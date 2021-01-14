package org.comroid.crystalshard.entity.user;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.Bot;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.TextChannel;
import org.comroid.crystalshard.model.MessageTarget;
import org.comroid.crystalshard.rest.BoundEndpoint;
import org.comroid.restless.REST;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;

import java.util.concurrent.CompletableFuture;

public final class User extends Snowflake.Abstract implements MessageTarget {
    public static final GroupBind<User> TYPE = BASETYPE.rootGroup("user");

    @Override
    public CompletableFuture<TextChannel> getTargetChannel() {
        return requireFromContext(Bot.class)
                .newRequest(REST.Method.POST, BoundEndpoint.PRIVATE_CHANNELS)
    }

    public User(ContextualProvider context, UniObjectNode data, EntityType entityType) {
        super(context, data, entityType);
    }
}
