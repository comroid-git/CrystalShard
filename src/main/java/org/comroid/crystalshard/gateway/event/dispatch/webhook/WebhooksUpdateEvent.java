package org.comroid.crystalshard.gateway.event.dispatch.webhook;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.crystalshard.gateway.event.dispatch.interaction.InteractionCreateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.message.MessageDeleteEvent;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class WebhooksUpdateEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<WebhooksUpdateEvent> TYPE
            = BASETYPE.subGroup("webhooks-update", WebhooksUpdateEvent::new);
    public static final VarBind<WebhooksUpdateEvent, Long, Channel, Channel> CHANNEL
            = TYPE.createBind("channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.getCache().getChannel(id))
            .build();
    public static final VarBind<WebhooksUpdateEvent, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.getCache().getGuild(id))
            .build();
    public final Reference<Channel> channel = getComputedReference(CHANNEL);
    public final Reference<Guild> guild = getComputedReference(GUILD);

    public Channel getChannel() {
        return channel.assertion();
    }

    public Guild getGuild() {
        return guild.assertion();
    }

    public WebhooksUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        // todo Request webhook changes
    }
}
