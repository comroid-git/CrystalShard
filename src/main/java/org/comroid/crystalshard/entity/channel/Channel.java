package org.comroid.crystalshard.entity.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Named;
import org.comroid.api.Rewrapper;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.model.channel.ChannelType;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public interface Channel extends Snowflake, Named {
    GroupBind<Channel> BASETYPE
            = Snowflake.BASETYPE.subGroup("channel");
    VarBind<Channel, Integer, ChannelType, ChannelType> CHANNEL_TYPE
            = BASETYPE.createBind("type")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(ChannelType::valueOf)
            .onceEach()
            .setRequired()
            .build();

    static Channel resolve(ContextualProvider context, UniObjectNode data) {
        return Snowflake.resolve(context, data, SnowflakeCache::getChannel, (ctx, obj) -> {
            final ChannelType type = CHANNEL_TYPE.getFrom(obj);
            switch (type) {
                case GUILD_TEXT:
                    return new GuildTextChannel(context, data);
                case DM:
                    return new PrivateTextChannel(context, data);
                case GUILD_VOICE:
                    return new GuildVoiceChannel(context, data);
                case GROUP_DM:
                    return new GroupChannel(context, data);
                case GUILD_CATEGORY:
                    return new GuildChannelCategory(context, data);
                case GUILD_NEWS:
                case GUILD_STORE:
                default:
                    throw new UnsupportedOperationException("unimplemented type: " + type);
            }
        });
    }
}
