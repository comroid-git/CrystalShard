package org.comroid.crystalshard.ui;

import org.comroid.api.IntEnum;
import org.comroid.api.Rewrapper;
import org.comroid.common.info.Described;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.NotNull;

public final class InteractionResponse extends AbstractDataContainer {
    @RootBind
    public static final GroupBind<InteractionResponse> TYPE
            = BASETYPE.subGroup("interaction-response");
    public static final VarBind<InteractionResponse, Integer, Type, Type> RESPONSE_TYPE
            = TYPE.createBind("type")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(Type::valueOf)
            .build();
    public static final int 

    InteractionResponse(InteractionCore core) {
        super(core, null);
    }

    public enum Type implements IntEnum, Described {
        Pong(1, "ACK a Ping"),
        Acknowledge(2, "ACK a command without sending a message, eating the user's input"),
        ChannelMessage(3, "respond with a message, eating the user's input"),
        ChannelMessageWithSource(4, "respond with a message, showing the user's input"),
        AcknowledgeWithSource(5, "ACK a command without sending a message, showing the user's input");

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
            return IntEnum.valueOf(value, Type.class);
        }
    }
}
