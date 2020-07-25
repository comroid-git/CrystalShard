package org.comroid.crystalshard.core.gateway.payload.message.reaction;

import org.comroid.common.info.MessageSupplier;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.model.emoji.Emoji;
import org.comroid.mutatio.proc.Processor;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class GatewayMessageReactionRemoveEmojiPayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayMessageReactionRemoveEmojiPayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-message-reaction-remove-emoji");
    public static final VarBind<Long, DiscordBot, Channel, Channel> channel
            = Root.createBind("channel_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(Snowflake.Type.CHANNEL, id)
                    .requireNonNull(MessageSupplier.format("Channel with ID %d not found", id)))
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Long, DiscordBot, Guild, Guild> guild
            = Root.createBind("guild_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(Snowflake.Type.GUILD, id).get())
            .onceEach()
            .build();
    public static final VarBind<Long, DiscordBot, Message, Message> message
            = Root.createBind("message_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(Snowflake.Type.MESSAGE, id)
                    .requireNonNull(MessageSupplier.format("Message with ID %d not found", id)))
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<UniObjectNode, DiscordBot, Emoji, Emoji> emoji
            = Root.createBind("emoji")
            .extractAsObject()
            .andResolve(Emoji::find)
            .onceEach()
            .setRequired()
            .build();

    public Channel getChannel() {
        return requireNonNull(channel);
    }

    public @Nullable Guild getGuild() {
        return get(guild);
    }

    public Processor<Guild> processGuild() {
        return process(guild);
    }

    public Message getMessage() {
        return requireNonNull(message);
    }

    public Emoji getEmoji() {
        return requireNonNull(emoji);
    }

    public GatewayMessageReactionRemoveEmojiPayload(GatewayPayloadWrapper gpw) {
        super(gpw);

        getMessage().getReactions(getEmoji()).clear();
    }
}
