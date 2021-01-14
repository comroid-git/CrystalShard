package org.comroid.crystalshard.model.message.embed;

import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;

public class EmbedAuthor extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedAuthor> TYPE
            = BASETYPE.rootGroup("embed-author");
}
