package org.comroid.crystalshard.core.gateway.payload.guild.integration;

import org.comroid.common.info.MessageSupplier;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.uniform.ValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class GatewayGuildIntegrationsUpdatePayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<AbstractGatewayPayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-guild-integrations-update");
    public static final VarBind<Long, DiscordBot, Guild, Guild> guild
            = Root.createBind("guild_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(Snowflake.Type.GUILD, id)
                    .requireNonNull(MessageSupplier.format("Guild with ID %d not found", id)))
            .onceEach()
            .setRequired()
            .build();

    public GatewayGuildIntegrationsUpdatePayload(GatewayPayloadWrapper gpw) {
        super(gpw);
    }
}
