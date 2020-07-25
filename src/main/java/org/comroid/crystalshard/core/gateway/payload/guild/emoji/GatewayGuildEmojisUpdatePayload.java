package org.comroid.crystalshard.core.gateway.payload.guild.emoji;

import org.comroid.common.info.MessageSupplier;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.model.emoji.Emoji;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class GatewayGuildEmojisUpdatePayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<AbstractGatewayPayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-guild-emojis-update");
    public static final VarBind<Long, DiscordBot, Guild, Guild> guild
            = Root.createBind("guild_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(Snowflake.Type.GUILD, id)
                    .requireNonNull(MessageSupplier.format("Guild with ID %d not found", id)))
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<UniObjectNode, DiscordBot, Emoji, Span<Emoji>> emojis
            = Root.createBind("emojis")
            .extractAsObject()
            .andResolve(Emoji::find)
            .intoCollection(Span::new)
            .setRequired()
            .build();

    public Guild getGuild() {
        return requireNonNull(guild);
    }

    public Span<Emoji> getEmojis() {
        return requireNonNull(emojis);
    }

    public GatewayGuildEmojisUpdatePayload(GatewayPayloadWrapper gpw) {
        super(gpw);
    }
}
