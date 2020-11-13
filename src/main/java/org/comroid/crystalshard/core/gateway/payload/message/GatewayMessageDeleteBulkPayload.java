package org.comroid.crystalshard.core.gateway.payload.message;

import org.comroid.common.info.MessageSupplier;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.DiscordEntity;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.mutatio.proc.Processor;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.ValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class GatewayMessageDeleteBulkPayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayMessageDeleteBulkPayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-message-delete-bulk");
    public static final VarBind<Object, Long, Message, Span<Message>> messages
            = Root.createBind("ids")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(DiscordEntity.Type.MESSAGE, id).get())
            .intoCollection(Span::new)
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
            .andResolve((id, bot) -> bot.getSnowflake(DiscordEntity.Type.GUILD, id).get())
            .onceEach()
            .build();

    public Span<Message> getMessages() {
        return requireNonNull(messages);
    }

    public Channel getChannel() {
        return requireNonNull(channel);
    }

    public GatewayMessageDeleteBulkPayload(GatewayPayloadWrapper gpw) {
        super(gpw);

        getMessages().stream()
                .mapToLong(Snowflake::getID)
                .forEach(id -> getBot().getCache().remove(id, DiscordEntity.Type.MESSAGE));
    }

    public @Nullable
    Guild get() {
        return get(guild);
    }

    public Processor<Guild> processGuild() {
        return process(guild);
    }
}
