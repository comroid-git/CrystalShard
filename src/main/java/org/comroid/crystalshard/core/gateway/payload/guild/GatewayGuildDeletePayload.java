package org.comroid.crystalshard.core.gateway.payload.guild;

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

public final class GatewayGuildDeletePayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayGuildDeletePayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-guild-delete");
    public static final VarBind<Long, DiscordBot, Guild, Guild> guild
            = Root.createBind("id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(Snowflake.Type.GUILD, id)
                    .requireNonNull(MessageSupplier.format("Guild with ID %d not found", id)))
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Boolean, DiscordBot, Boolean, Boolean> unavailable
            = Root.createBind("unavailable")
            .extractAs(ValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .build();

    public Guild getGuild() {
        return requireNonNull(guild);
    }

    public boolean isUnavailable() {
        return wrap(unavailable).orElse(true);
    }

    public boolean isKicked() {
        return getComputedReference(unavailable).isNull();
    }

    public GatewayGuildDeletePayload(GatewayPayloadWrapper gpw) {
        super(gpw);

        getBot().getCache().remove(getGuild().getID(), Snowflake.Type.GUILD);
    }
}
