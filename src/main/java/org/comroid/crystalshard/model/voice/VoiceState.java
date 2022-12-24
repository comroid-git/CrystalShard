package org.comroid.crystalshard.model.voice;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.model.DiscordDataContainer;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class VoiceState extends DataContainerBase<DiscordDataContainer> implements DiscordDataContainer {
    @RootBind
    public static final GroupBind<VoiceState> TYPE
            = BASETYPE.subGroup("voice-state", VoiceState::new);
    public static final VarBind<VoiceState, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((vs, id) -> vs.getCache().getGuild(id))
            .onceEach()
            .build();
    public static final VarBind<VoiceState, Long, Channel, Channel> CHANNEL
            = TYPE.createBind("channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((vs, id) -> vs.getCache().getChannel(id))
            .onceEach()
            .build();
    public static final VarBind<VoiceState, Long, User, User> USER
            = TYPE.createBind("user_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((vs, id) -> vs.getCache().getUser(id))
            .onceEach()
            .build();
    public static final VarBind<VoiceState, UniObjectNode, UniObjectNode, UniObjectNode> GUILD_MEMBER
            = TYPE.createBind("member")
            .extractAsObject()
            .asIdentities() // todo
            .onceEach()
            .build();
    public static final VarBind<VoiceState, String, String, String> SESSION_ID
            = TYPE.createBind("session_id")
            .extractAs(StandardValueType.STRING)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<VoiceState, Boolean, Boolean, Boolean> DEAFENED
            = TYPE.createBind("deaf")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<VoiceState, Boolean, Boolean, Boolean> MUTED
            = TYPE.createBind("mute")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<VoiceState, Boolean, Boolean, Boolean> SELF_DEAFENED
            = TYPE.createBind("self_deaf")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<VoiceState, Boolean, Boolean, Boolean> SELF_MUTED
            = TYPE.createBind("self_mute")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<VoiceState, Boolean, Boolean, Boolean> STREAMING
            = TYPE.createBind("self_stream")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<VoiceState, Boolean, Boolean, Boolean> STREAMING_WEBCAM
            = TYPE.createBind("self_video")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<VoiceState, Boolean, Boolean, Boolean> SUPPRESSED
            = TYPE.createBind("suppress")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();

    public VoiceState(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
