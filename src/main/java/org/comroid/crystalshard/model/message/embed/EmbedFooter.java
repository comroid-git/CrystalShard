package org.comroid.crystalshard.model.message.embed;

import org.comroid.api.ContextualProvider;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public class EmbedFooter extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedFooter> TYPE
            = BASETYPE.rootGroup("embed-footer");
}
