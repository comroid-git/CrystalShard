package org.comroid.crystalshard.model.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.webhook.Webhook;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class ChannelFollowing extends DataContainerBase<DiscordDataContainer> implements DiscordDataContainer {
    @RootBind
    public static final GroupBind<ChannelFollowing> TYPE
            = BASETYPE.subGroup("channel-following", ChannelFollowing::new);
    public static final VarBind<ChannelFollowing, Long, Channel, Channel> CHANNEL
            = TYPE.createBind("channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((flw, id) -> flw.getCache().getChannel(id))
            .build();
    public static final VarBind<ChannelFollowing, Long, Webhook, Webhook> WEBHOOK
            = TYPE.createBind("webhook_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((flw, id) -> flw.getCache().getWebhook(id))
            .build();

    public ChannelFollowing(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
