package org.comroid.crystalshard.model.message.embed;

import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;

public class EmbedImage extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedImage> TYPE
            = BASETYPE.rootGroup("embed-image");
}
