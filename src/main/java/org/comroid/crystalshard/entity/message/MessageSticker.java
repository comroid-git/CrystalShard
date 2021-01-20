package org.comroid.crystalshard.entity.message;

import org.comroid.api.ContextualProvider;
import org.comroid.api.IntEnum;
import org.comroid.api.Rewrapper;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.NotNull;

// Todo Future patch @ discord is gonna update this
public final class MessageSticker extends Snowflake.Abstract {
    @RootBind
    public static final GroupBind<MessageSticker> TYPE
            = BASETYPE.subGroup("message-sticker", MessageSticker::resolve);
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

    private MessageSticker(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.MESSAGE_STICKER);
    }

    public static MessageSticker resolve(ContextualProvider context, UniNode data) {
        return Snowflake.resolve(context, data, SnowflakeCache::getMessageSticker, MessageSticker::new);
    }

    public enum FormatType implements IntEnum {
        PNG(1),
        APNG(2),
        LOTTIE(3);

        private final int value;

        @Override
        public @NotNull Integer getValue() {
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
