package org.comroid.crystalshard.entity.guild;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.model.emoji.Emoji;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;

public final class CustomEmoji extends Snowflake.Abstract implements Emoji {
    @RootBind
    public static final GroupBind<CustomEmoji> TYPE
            = BASETYPE.subGroup("custom-emoji", CustomEmoji::resolve);

    private CustomEmoji(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.CUSTOM_EMOJI);
    }

    public static CustomEmoji resolve(ContextualProvider context, UniNode data) {
        return Snowflake.resolve(context, data, SnowflakeCache::getCustomEmoji, CustomEmoji::new);
    }
}
