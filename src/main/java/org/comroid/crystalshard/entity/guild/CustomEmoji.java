package org.comroid.crystalshard.entity.guild;

import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;

public final class CustomEmoji extends Snowflake.Abstract {
    @RootBind
    public static final GroupBind<CustomEmoji> TYPE
            = BASETYPE.rootGroup(Guild.TYPE, "custom-emoji");
}
