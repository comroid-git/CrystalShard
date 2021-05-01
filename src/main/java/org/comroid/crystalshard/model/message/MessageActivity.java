package org.comroid.crystalshard.model.message;

import org.comroid.api.ContextualProvider;
import org.comroid.api.IntegerAttribute;
import org.comroid.api.Rewrapper;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MessageActivity extends AbstractDataContainer {
    @RootBind
    public static final GroupBind<MessageActivity> TYPE
            = BASETYPE.subGroup("message-activity", MessageActivity::new);
    public static final VarBind<MessageActivity, Integer, Rewrapper<Type>, Rewrapper<Type>> ACTIVITY_TYPE
            = TYPE.createBind("type")
            .extractAs(StandardValueType.INTEGER)
            .andRemap(Type::valueOf)
            .build();
    public static final VarBind<MessageActivity, String, String, String> PARTY_ID
            = TYPE.createBind("party_id")
            .extractAs(StandardValueType.STRING)
            .build();

    public MessageActivity(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }

    public enum Type implements IntegerAttribute {
        JOIN(1),
        SPECTATE(2),
        LISTEN(3),
        JOIN_REQUEST(5);

        private final int value;

        @Override
        public @NotNull Integer getValue() {
            return value;
        }

        Type(int value) {
            this.value = value;
        }

        public static Rewrapper<Type> valueOf(int value) {
            return IntegerAttribute.valueOf(value, Type.class);
        }
    }
}
