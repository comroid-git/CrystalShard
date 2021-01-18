package org.comroid.crystalshard.entity.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;

public final class GuildVoiceChannel extends AbstractVoiceChannel implements GuildChannel, VoiceChannel {
    @RootBind
    public static final GroupBind<GuildVoiceChannel> TYPE
            = GroupBind.<GuildVoiceChannel>combine("guild-voice-channel", GuildChannel.BASETYPE, VoiceChannel.BASETYPE);

    @Override
    public Guild getGuild() {
        return null; // todo
    }

    GuildVoiceChannel(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.GUILD_VOICE_CHANNEL);
    }
}
