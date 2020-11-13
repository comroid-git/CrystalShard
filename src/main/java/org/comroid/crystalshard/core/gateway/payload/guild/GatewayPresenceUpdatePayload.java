package org.comroid.crystalshard.core.gateway.payload.guild;

import org.comroid.common.info.MessageSupplier;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.DiscordEntity;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.model.user.UserActivity;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class GatewayPresenceUpdatePayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<AbstractGatewayPayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-presence-update");
    public static final VarBind<Object, UniObjectNode, User, User> user
            = Root.createBind("user")
            .extractAsObject()
            .andProvide(
                    DiscordEntity.Bind.ID,
                    (id, bot) -> bot.getSnowflake(DiscordEntity.Type.USER, id).get(),
                    User.Bind.Root)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, Long, Role, Span<Role>> roles
            = Root.createBind("roles")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(DiscordEntity.Type.ROLE, id)
                    .requireNonNull(MessageSupplier.format("Role with ID %d not found", id)))
            .intoCollection(Span::new)
            .setRequired()
            .build();
    public static final int game
            = Root.createBind("game")
            .extractAsObject()
            .andConstruct(UserActivity.Root)

    public GatewayPresenceUpdatePayload(GatewayPayloadWrapper gpw) {
        super(gpw);
    }
}
