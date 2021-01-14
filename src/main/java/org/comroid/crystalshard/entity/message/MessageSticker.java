package org.comroid.crystalshard.entity.message;

import org.comroid.api.ContextualProvider;
import org.comroid.api.IntEnum;
import org.comroid.api.Rewrapper;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

// Todo Future patch @ discord is gonna update this
public final class MessageSticker extends Snowflake.Abstract {
    @RootBind
    public static final GroupBind<MessageSticker> TYPE
            = BASETYPE.rootGroup("message-sticker");
    public static final VarBind<MessageSticker, Long, Long, Long> PACK
            = TYPE.createBind("pack_id")
            .extractAs(StandardValueType.LONG)
            .build();
    public static final VarBind<MessageSticker, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<MessageSticker, String, String, String> DESCRIPTION
            = TYPE.createBind("description")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<MessageSticker, String, String, String> TAGS
            = TYPE.createBind("tags")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<MessageSticker, String, String, String> ASSET
            = TYPE.createBind("asset")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<MessageSticker, String, String, String> PREVIEW_ASSET
            = TYPE.createBind("preview_asset")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<MessageSticker, Integer, FormatType, FormatType> FORMAT_TYPE
            = TYPE.createBind("format_type")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(FormatType::valueOf)
            .build();

    protected MessageSticker(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.MESSAGE_STICKER);
    }

    public enum FormatType implements IntEnum {
        PNG(1),
        APNG(2),
        LOTTIE(3);

        private final int value;

        @Override
        public int getValue() {
            return value;
        }

        FormatType(int value) {
            this.value = value;
        }

        public static Rewrapper<FormatType> valueOf(int value) {
            return IntEnum.valueOf(value, FormatType.class);
        }
    }
}
