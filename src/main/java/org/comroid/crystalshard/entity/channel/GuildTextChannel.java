package org.comroid.crystalshard.entity.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.time.Instant;

public final class GuildTextChannel extends AbstractTextChannel implements GuildChannel, TextChannel {
    @RootBind
    public static final GroupBind<GuildTextChannel> TYPE
            = GroupBind.<GuildTextChannel>combine("guild-text-channel", GuildChannel.BASETYPE, TextChannel.BASETYPE);
    public static final VarBind<GuildTextChannel, String, Instant, Instant> LAST_PIN_TIMESTAMP
            = TYPE.createBind("last_pin_timestamp")
            .extractAs(StandardValueType.STRING)
            .andRemap(Instant::parse)
            .build();

    @Override
    public Guild getGuild() {
        return null; // Todo
    }

    GuildTextChannel(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.GUILD_TEXT_CHANNEL);
    }
}
