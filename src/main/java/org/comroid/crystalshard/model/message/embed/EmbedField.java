package org.comroid.crystalshard.model.message.embed;

import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class EmbedField extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedField> TYPE
            = BASETYPE.subGroup("embed-field",
            (ctx, data) -> new EmbedField(ctx.as(MessageEmbed.class, "Context must be Embed"), data));
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
    public final Reference<String> name = getComputedReference(NAME);
    public final Reference<String> value = getComputedReference(VALUE);
    public final Reference<Boolean> isInline = getComputedReference(INLINE);

    public String getName() {
        return name.assertion();
    }

    public String getValue() {
        return value.assertion();
    }

    public boolean isInline() {
        return isInline.assertion();
    }

    public EmbedField(Embed parent, String name, String value, boolean inline) {
        this(parent, null);

        put(NAME, name);
        put(VALUE, value);
        put(INLINE, inline);
    }

    public EmbedField(Embed parent, @Nullable UniNode initialData) {
        super(parent, initialData);
    }
}
