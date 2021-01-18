package org.comroid.crystalshard.entity.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;

public final class PrivateTextChannel extends AbstractTextChannel implements TextChannel {
    @RootBind
    public static final GroupBind<PrivateTextChannel> TYPE
            = TextChannel.BASETYPE.subGroup("private-text-channel");

    PrivateTextChannel(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.PRIVATE_CHANNEL);
    }
}
