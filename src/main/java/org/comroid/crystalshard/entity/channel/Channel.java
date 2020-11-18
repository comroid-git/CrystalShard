package org.comroid.crystalshard.entity.channel;

import org.comroid.api.Named;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.varbind.bind.GroupBind;

public interface Channel extends Snowflake, Named {
    GroupBind<Channel> BASETYPE = Snowflake.BASETYPE.subGroup("channel");
}
