package org.comroid.crystalshard.core.gateway.payload.generic;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.channel.PrivateTextChannel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.util.HashSet;

public final class GatewayReadyPayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayReadyPayload, DiscordBot> Root
            = BaseGroup.subGroup("gateway-ready", GatewayReadyPayload.class);
    public static final VarBind<Object, Integer, Integer, Integer> GatewayVersion
            = Root.createBind("v")
            .extractAs(ValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, UniObjectNode, User, User> Yourself
            = Root.createBind("user")
            .extractAsObject()
            .andConstruct(User.Bind.Root)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, UniObjectNode, PrivateTextChannel, HashSet<PrivateTextChannel>> PrivateChannels
            = Root.createBind("private_channels")
            .extractAsArray()
            .andConstruct(PrivateTextChannel.Bind.Root)
            .intoCollection(HashSet::new)
            .setRequired()
            .build();
    public static final VarBind<Object, UniObjectNode, Guild, HashSet<Guild>> Guilds
            = Root.createBind("guilds")
            .extractAsArray()
            .andConstruct(Guild.Bind.Root)
            .intoCollection(HashSet::new)
            .setRequired()
            .build();
    public static final VarBind<Object, String, String, String> SessionID
            = Root.createBind("session_id")
            .extractAs(ValueType.STRING)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    // todo Shards field missing

    public GatewayReadyPayload(GatewayPayloadWrapper gpw) {
        super(gpw);
    }
}
