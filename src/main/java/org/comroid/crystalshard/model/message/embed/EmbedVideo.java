package org.comroid.crystalshard.model.message.embed;

import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;

public class EmbedVideo extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedVideo> TYPE
            = BASETYPE.rootGroup("embed-video");
}
