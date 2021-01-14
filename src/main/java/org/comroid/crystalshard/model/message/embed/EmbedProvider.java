package org.comroid.crystalshard.model.message.embed;

import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;

public class EmbedProvider extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedProvider> TYPE
            = BASETYPE.rootGroup("embed-provider");
}
