package org.comroid.crystalshard.model.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.api.IntegerAttribute;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.crystalshard.model.guild.PermissionSet;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PermissionOverwrite extends AbstractDataContainer {
    @RootBind
    public static final GroupBind<PermissionOverwrite> TYPE
            = BASETYPE.subGroup("permission-overwrite", PermissionOverwrite::new);
    public static final VarBind<PermissionOverwrite, Long, Snowflake, Snowflake> REFERENT
            = TYPE.createBind("id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((po, id) -> po.getCache().getSnowflake(EntityType.SNOWFLAKE, id))
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<PermissionOverwrite, String, ReferentType, ReferentType> REFERENT_TYPE
            = TYPE.createBind("type")
            .extractAs(StandardValueType.STRING)
            .andRemap(ReferentType::find)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<PermissionOverwrite, Integer, PermissionSet, PermissionSet> ALLOWED
            = TYPE.createBind("allow")
            .extractAs(StandardValueType.INTEGER)
            .andRemap(PermissionSet::new)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<PermissionOverwrite, Integer, PermissionSet, PermissionSet> DENIED
            = TYPE.createBind("deny")
            .extractAs(StandardValueType.INTEGER)
            .andRemap(PermissionSet::new)
            .onceEach()
            .setRequired()
            .build();

    public PermissionOverwrite(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }

    public enum ReferentType implements IntegerAttribute {
        ROLE(0),
        MEMBER(1);

        private final int value;

        @Override
        public @NotNull Integer getValue() {
            return value;
        }

        ReferentType(int value) {
            this.value = value;
        }

        public static ReferentType find(String value) {
            for (ReferentType each : values())
                if (each.name().equalsIgnoreCase(value))
                    return each;
            return null;
        }
    }
}
