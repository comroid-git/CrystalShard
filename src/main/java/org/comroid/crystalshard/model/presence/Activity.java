package org.comroid.crystalshard.model.presence;

import org.comroid.api.ContextualProvider;
import org.comroid.api.IntegerAttribute;
import org.comroid.api.Rewrapper;
import org.comroid.common.info.Described;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public final class Activity extends DataContainerBase<DiscordDataContainer> implements DiscordDataContainer {
    @RootBind
    public static final GroupBind<Activity> TYPE
            = BASETYPE.subGroup("activity", Activity::new);
    public static final VarBind<Activity, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Activity, Integer, Type, Type> ACTIVITY_TYPE
            = TYPE.createBind("type")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(Type::valueOf)
            .build();
    public static final VarBind<Activity, Long, Instant, Instant> CREATED_AT
            = TYPE.createBind("created_at")
            .extractAs(StandardValueType.LONG)
            .andRemap(Instant::ofEpochMilli)
            .build();

    public Activity(ContextualProvider context, Activity.Type type, String detail) {
        this(context, null);

        put(ACTIVITY_TYPE, type);
        put(NAME, detail);
    }

    public Activity(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }

    public enum Type implements IntegerAttribute, Described {
        PLAYING(0, "Playing {name}"),
        STREAMING(1, "Streaming {details}"),
        LISTENING(2, "Listening to {name}"),
        CUSTOM(4, "{emoji} {name}"),
        COMPETING(5, "Competing in {name}");

        private final int value;
        private final String description;

        @Override
        public @NotNull Integer getValue() {
            return value;
        }

        @Override
        public String getDescription() {
            return description;
        }

        Type(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public static Rewrapper<Type> valueOf(int value) {
            return IntegerAttribute.valueOf(value, Type.class);
        }
    }
}
