package org.comroid.crystalshard.core.gateway.payload.guild.ban;

import org.comroid.common.info.MessageSupplier;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class GatewayGuildBanRemovePayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayGuildBanRemovePayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-guild-ban-add");
    public static final VarBind<Long, DiscordBot, Guild, Guild> guild
            = Root.createBind("guild_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(Snowflake.Type.GUILD, id)
                    .requireNonNull(MessageSupplier.format("Guild with ID %d not found", id)))
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<UniObjectNode, DiscordBot, User, User> user
            = Root.createBind("user")
            .extractAsObject()
            .andResolve(User::find)
            .onceEach()
            .setRequired()
            .build();

    public Guild getGuild() {
        return requireNonNull(guild);
    }

    public User getUser() {
        return requireNonNull(user);
    }

    public GatewayGuildBanRemovePayload(GatewayPayloadWrapper gpw) {
        super(gpw);

        getGuild().unbanUser(getUser());
    }
}
