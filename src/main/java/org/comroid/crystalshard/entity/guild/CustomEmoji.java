package org.comroid.crystalshard.entity.guild;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.model.emoji.Emoji;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;

public final class CustomEmoji extends Snowflake.Abstract implements Emoji {
    @RootBind
    public static final GroupBind<CustomEmoji> TYPE
            = BASETYPE.rootGroup(Guild.TYPE, "custom-emoji");

    public CustomEmoji(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.CUSTOM_EMOJI);
    }
}
