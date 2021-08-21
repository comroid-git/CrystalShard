package org.comroid.crystalshard.entity.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Named;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.model.channel.ChannelType;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public interface Channel extends Snowflake, Named {
    GroupBind<Channel> BASETYPE
            = Snowflake.BASETYPE.subGroup("channel", Channel::resolve);
    VarBind<Channel, Integer, ChannelType, ChannelType> CHANNEL_TYPE
            = BASETYPE.createBind("type")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(ChannelType::valueOf)
            .onceEach()
            .setRequired()
            .build();

    static Channel resolve(ContextualProvider context, UniNode data) {
        return Snowflake.resolve(context, data, SnowflakeCache::getChannel, (ctx, obj) -> {
            final ChannelType type = CHANNEL_TYPE.getFrom(obj);
            switch (type) {
                case GUILD_TEXT:
                case GUILD_NEWS:
                case GUILD_STORE:
                    return new GuildTextChannel(ctx, obj);
                case DM:
                    return new PrivateTextChannel(ctx, obj);
                case GUILD_VOICE:
                    return new GuildVoiceChannel(ctx, obj);
                case GROUP_DM:
                    return new GroupChannel(ctx, obj);
                case GUILD_CATEGORY:
                    return new GuildChannelCategory(ctx, obj);
                default:
                    throw new UnsupportedOperationException("Unimplemented type " + type + "; data: " + data);
            }
        });
    }
}
