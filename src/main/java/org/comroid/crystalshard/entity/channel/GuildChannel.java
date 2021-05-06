package org.comroid.crystalshard.entity.channel;

import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.model.channel.PermissionOverwrite;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public interface GuildChannel extends Channel {
    GroupBind<GuildChannel> BASETYPE
            = Channel.BASETYPE.subGroup("guild-channel");
    VarBind<GuildChannel, Long, Guild, Guild> GUILD
            = BASETYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((channel, id) -> channel.getCache().getGuild(id))
            .build();
    VarBind<GuildChannel, Integer, Integer, Integer> POSITION
            = BASETYPE.createBind("position")
            .extractAs(StandardValueType.INTEGER)
            .build();
    VarBind<GuildChannel, UniObjectNode, PermissionOverwrite, Span<PermissionOverwrite>> PERMISSION_OVERWRITES
            = BASETYPE.createBind("permission_overwrites")
            .extractAsArray()
            .andResolve(PermissionOverwrite::new)
            .intoSpan()
            .build();
    VarBind<GuildChannel, String, String, String> NAME
            = BASETYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    VarBind<GuildChannel, String, String, String> TOPIC
            = BASETYPE.createBind("topic")
            .extractAs(StandardValueType.STRING)
            .build();
    VarBind<GuildChannel, Boolean, Boolean, Boolean> NSFW
            = BASETYPE.createBind("nsfw")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    VarBind<GuildChannel, Long, GuildChannelCategory, GuildChannelCategory> CATEGORY
            = BASETYPE.createBind("parent_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((channel, id) -> channel.getCache().getChannelCategory(id))
            .build();

    Guild getGuild();
}
