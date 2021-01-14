package org.comroid.crystalshard.entity.channel;

import org.comroid.api.Named;
import org.comroid.api.Rewrapper;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.model.channel.ChannelType;
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
}
