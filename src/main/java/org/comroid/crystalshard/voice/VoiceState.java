package org.comroid.crystalshard.voice;

import org.comroid.common.info.MessageSupplier;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class VoiceState extends BotBound.DataBase {
    @RootBind
    public static final GroupBind<DataBase, DiscordBot> Root
            = BaseGroup.rootGroup("voice-state");
    public static final VarBind<Long, DiscordBot, Guild, Guild> guild
            = Root.createBind("guild_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(Snowflake.Type.GUILD, id)
                    .requireNonNull(MessageSupplier.format("Guild with ID %d not found", id)))
            .onceEach()
            .build();
    public static final VarBind<Long, DiscordBot, Channel, Channel> channel
            = Root.createBind("channel_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(Snowflake.Type.CHANNEL, id)
                    .requireNonNull(MessageSupplier.format("Channel with ID %d not found", id)))
            .onceEach()
            .build();

    public VoiceState(DiscordBot bot, UniObjectNode initialData) {
        super(bot, initialData);
    }
}
