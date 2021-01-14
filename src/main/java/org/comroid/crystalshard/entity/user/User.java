package org.comroid.crystalshard.entity.user;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.Bot;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.PrivateTextChannel;
import org.comroid.crystalshard.model.MessageTarget;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.restless.REST;
import org.comroid.restless.body.BodyBuilderType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;

import java.util.concurrent.CompletableFuture;

public final class User extends Snowflake.Abstract implements MessageTarget {
    public static final GroupBind<User> TYPE = BASETYPE.rootGroup("user");

    @Override
    public CompletableFuture<PrivateTextChannel> getTargetChannel() {
        return requireFromContext(Bot.class).newRequest(
                REST.Method.POST,
                Endpoint.PRIVATE_CHANNELS,
                PrivateTextChannel.TYPE,
                BodyBuilderType.OBJECT,
                obj -> obj.put("recipient_id", getID())
        );
    }

    public User(ContextualProvider context, UniObjectNode data, EntityType entityType) {
        super(context, data, entityType);
    }
}
