package org.comroid.crystalshard.gateway.presence;

import org.comroid.crystalshard.DiscordBotShard;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.OpCode;
import org.comroid.uniform.node.UniArrayNode;
import org.comroid.uniform.node.UniObjectNode;

import java.util.concurrent.CompletableFuture;

public final class ShardBasedPresence extends AbstractPresence {
    private final DiscordBotShard shard;

    public ShardBasedPresence(DiscordBotShard shard, User yourself) {
        super(yourself);

        this.shard = shard;
    }

    @Override
    public CompletableFuture<Void> update() {
        final UniObjectNode obj = shard.getGateway()
                .createPayloadBase(OpCode.PRESENCE_UPDATE);
        final UniObjectNode data = obj.putObject("d");
        data.putNull("since");
        data.put("status", status.getIdent());
        data.put("afk", afkState);
        final UniArrayNode activities = data.putArray("activities");
        this.activities.forEach(activity -> activity.toObjectNode(activities.addObject()));

        return shard.getGateway()
                .getSocket()
                .send(obj.toString())
                .thenApply(nil -> null);
    }
}
