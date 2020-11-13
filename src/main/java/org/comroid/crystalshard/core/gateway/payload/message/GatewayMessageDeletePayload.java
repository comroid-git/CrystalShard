package org.comroid.crystalshard.core.gateway.payload.message;

import org.comroid.common.info.MessageSupplier;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.DiscordEntity;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.mutatio.proc.Processor;
import org.comroid.uniform.ValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class GatewayMessageDeletePayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayMessageDeletePayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-message-delete");
    public static final VarBind<Object, Long, Message, Message> message
            = Root.createBind("id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(DiscordEntity.Type.MESSAGE, id)
                    .requireNonNull(MessageSupplier.format("Message with ID %d not found", id)))
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, Long, Channel, Channel> channel
            = Root.createBind("channel_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(DiscordEntity.Type.CHANNEL, id)
                    .requireNonNull(MessageSupplier.format("Channel with ID %d not found", id)))
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, Long, Guild, Guild> guild
            = Root.createBind("guild_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(DiscordEntity.Type.GUILD, id)
                    .requireNonNull(MessageSupplier.format("Guild with ID %s not found", id)))
            .onceEach()
            .build();

    public Message getMessage() {
        return requireNonNull(message);
    }

    public Channel getChannel() {
        return requireNonNull(channel);
    }

    public @Nullable
    Guild getGuild() {
        return get(guild);
    }

    public GatewayMessageDeletePayload(GatewayPayloadWrapper gpw) {
        super(gpw);

        getBot().getCache().remove(getMessage().getID(), DiscordEntity.Type.MESSAGE);
    }

    public Processor<Guild> processGuild() {
        return process(guild);
    }
}
