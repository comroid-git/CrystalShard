package org.comroid.crystalshard.model.message.embed;

import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;

public class EmbedThumbnail extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedThumbnail> TYPE
            = BASETYPE.rootGroup("embed-thumbnail");
}
