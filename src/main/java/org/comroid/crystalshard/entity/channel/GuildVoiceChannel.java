package org.comroid.crystalshard.entity.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.impl.AbstractVoiceChannel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public final class GuildVoiceChannel extends AbstractVoiceChannel implements GuildChannel, VoiceChannel {
    @RootBind
    public static final GroupBind<GuildVoiceChannel> TYPE
            = GroupBind.<GuildVoiceChannel>combine("guild-voice-channel", GuildChannel.BASETYPE, VoiceChannel.BASETYPE);

    @Override
    public Guild getGuild() {
        return null; // todo
    }

    public GuildVoiceChannel(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.GUILD_VOICE_CHANNEL);
    }
}
