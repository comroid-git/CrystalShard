package org.comroid.crystalshard.gateway.event.dispatch.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class ChannelPinsUpdate extends GatewayEvent {
    public static final GroupBind<ChannelPinsUpdate> TYPE
            = BASETYPE.subGroup("channel-pins-update", ChannelPinsUpdate::new);
    public static final VarBind<ChannelPinsUpdate, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.requireFromContext(SnowflakeCache.class).getGuild(id))
            .build();
    public static final VarBind<ChannelPinsUpdate, Long, Channel, Channel> CHANNEL
            = TYPE.createBind("channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.requireFromContext(SnowflakeCache.class).getChannel(id))
            .build();
    public static final VarBind<ChannelPinsUpdate, String, Instant, Instant> LAST_PIN_TIMESTAMP
            = TYPE.createBind("last_pin_timestamp")
            .extractAs(StandardValueType.STRING)
            .andRemap(Instant::parse)
            .build();

    public ChannelPinsUpdate(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
