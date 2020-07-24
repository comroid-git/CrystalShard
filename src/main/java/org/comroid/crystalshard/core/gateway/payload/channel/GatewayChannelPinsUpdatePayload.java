package org.comroid.crystalshard.core.gateway.payload.channel;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.mutatio.proc.Processor;
import org.comroid.uniform.ValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public final class GatewayChannelPinsUpdatePayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayChannelPinsUpdatePayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-channel-pins-update");
    public static final VarBind<Long, DiscordBot, Guild, Guild> guild
            = Root.createBind("guild_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(Snowflake.Type.GUILD, id).get())
            .onceEach()
            .build();
    public static final VarBind<Long, DiscordBot, Channel, Channel> channel
            = Root.createBind("channel_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(Snowflake.Type.CHANNEL, id).requireNonNull("Channel not found"))
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<String, DiscordBot, Instant, Instant> lastPin
            = Root.createBind("last_pin_timestamp")
            .extractAs(ValueType.STRING)
            .andRemap(Instant::parse)
            .onceEach()
            .build();

    public @Nullable Guild getGuild() {
        return get(guild);
    }

    public Processor<Guild> processGuild() {
        return process(guild);
    }

    public Channel getChannel() {
        return requireNonNull(channel);
    }

    public @Nullable Instant getLastPinTimestamp() {
        return get(lastPin);
    }

    public Processor<Instant> processLastPinTimestamp() {
        return process(lastPin);
    }

    public GatewayChannelPinsUpdatePayload(GatewayPayloadWrapper gpw) {
        super(gpw);
    }
}
