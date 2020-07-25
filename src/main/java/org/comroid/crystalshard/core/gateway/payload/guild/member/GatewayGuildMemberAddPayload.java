package org.comroid.crystalshard.core.gateway.payload.guild.member;

import org.comroid.common.info.MessageSupplier;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.guild.GuildMember;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class GatewayGuildMemberAddPayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<AbstractGatewayPayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-guild-member-add");
    public static final VarBind<UniObjectNode, DiscordBot, GuildMember, GuildMember> member
            = Root.createBind("")
            .extractAsObject()
            .andResolve((obj, bot) -> bot.updateGuildMember(obj))
            .onceEach()
            .build();
    public static final VarBind<Long, DiscordBot, Guild, Guild> guild
            = Root.createBind("guild_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(Snowflake.Type.GUILD, id)
            .requireNonNull(MessageSupplier.format("Guild with ID %d could not be found", id)))
            .onceEach()
            .setRequired()
            .build();

    public GuildMember getGuildMember() {
        return requireNonNull(member);
    }

    public Guild getGuild() {
        return requireNonNull(guild);
    }

    public GatewayGuildMemberAddPayload(GatewayPayloadWrapper gpw) {
        super(gpw);

        getGuild().addUser(getGuildMember());
    }
}
