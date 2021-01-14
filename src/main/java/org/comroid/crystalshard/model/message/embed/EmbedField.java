package org.comroid.crystalshard.model.message.embed;

import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;

public class EmbedField extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedField> TYPE
            = BASETYPE.rootGroup("embed-field");
}
