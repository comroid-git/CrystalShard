package org.comroid.crystalshard.model.message.embed;

import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public class EmbedField extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedField> TYPE
            = BASETYPE.subGroup("embed-field");
    public static final VarBind<EmbedField, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<EmbedField, String, String, String> VALUE
            = TYPE.createBind("value")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<EmbedField, Boolean, Boolean, Boolean> INLINE
            = TYPE.createBind("inline")
            .extractAs(StandardValueType.BOOLEAN)
            .build();

    public EmbedField(Embed parent, @Nullable UniNode initialData) {
        super(parent, initialData);
    }
}
