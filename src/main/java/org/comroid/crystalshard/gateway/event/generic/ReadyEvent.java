package org.comroid.crystalshard.gateway.event.generic;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.DiscordBotShard;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniArrayNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public final class ReadyEvent extends GatewayEvent {
    public static final GroupBind<ReadyEvent> TYPE
            = BASETYPE.rootGroup("ready");
    public static final VarBind<ReadyEvent, Integer, Integer, Integer> VERSION
            = TYPE.createBind("v")
            .extractAs(ValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<ReadyEvent, UniObjectNode, User, User> YOURSELF
            = TYPE.createBind("user")
            .extractAsObject()
            .andProvideRef(
                    Snowflake.ID,
                    (ready, id) -> ready.requireFromContext(DiscordBotShard.class)
                            .getSnowflakeCache()
                            .getUser(id),
                    User.TYPE)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<ReadyEvent, UniObjectNode, Channel, ArrayList<Channel>> PRIVATE_CHANNELS
            = TYPE.createBind("private_channels")
            .extractAsArray() // todo this wont work
            .andConstruct(Channel.BASETYPE) // todo UniArrayNode @ EXTR is being overwritten
            .intoCollection(ArrayList::new)
            .setRequired()
            .build();
    public static final VarBind<ReadyEvent, UniObjectNode, Guild, ArrayList<Guild>> GUILDS
            = TYPE.createBind("guilds")
            .extractAsArray()
            .andConstruct(Guild.TYPE) // construct, because we are in READY event
            .intoCollection(ArrayList::new)
            .setRequired()
            .build();
    public static final VarBind<ReadyEvent, String, String, String> SESSION_ID
            = TYPE.createBind("session_id")
            .extractAs(ValueType.STRING)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<ReadyEvent, UniArrayNode, UniArrayNode, UniArrayNode> SHARD
            = TYPE.createBind("shard")
            .extractAsArray()
            .asIdentities()
            .onceEach() // identities once here, will be handled
            .setRequired()
            .build();
    public final Reference<Integer> version = getComputedReference(VERSION);
    public final Reference<User> yourself = getComputedReference(YOURSELF);
    public final Reference<ArrayList<Channel>> privateChannels = getComputedReference(PRIVATE_CHANNELS);
    public final Reference<ArrayList<Guild>> guilds = getComputedReference(GUILDS);
    public final Reference<String> sessionID = getComputedReference(SESSION_ID);
    public final Reference<UniArrayNode> shard = getComputedReference(SHARD);

    public ReadyEvent(ContextualProvider context, @Nullable UniObjectNode initialData) {
        super(context, initialData);
    }
}
