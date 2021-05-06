package org.comroid.crystalshard.model.message;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.guild.CustomEmoji;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.crystalshard.model.emoji.Emoji;
import org.comroid.crystalshard.model.emoji.UnicodeEmoji;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class Reaction extends AbstractDataContainer {
    @RootBind
    public static final GroupBind<Reaction> TYPE
            = BASETYPE.subGroup("message-reaction",
            (ctx, data) -> new Reaction(ctx.as(Message.class, "Context must be Message"), data));
    public static final VarBind<Reaction, Integer, Integer, Integer> COUNT
            = TYPE.createBind("count")
            .extractAs(StandardValueType.INTEGER)
            .build();
    public static final VarBind<Reaction, Boolean, Boolean, Boolean> ME
            = TYPE.createBind("me")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Reaction, UniObjectNode, Emoji, Emoji> EMOJI
            = TYPE.createBind("emoji")
            .extractAsObject()
            .andResolve(Reaction::resolveEmoji)
            .build();
    private final Message parent;

    public Message getMessage() {
        return parent;
    }

    public Reaction(Message parent, @Nullable UniNode initialData) {
        super(parent, initialData);

        this.parent = parent;
    }

    private static Emoji resolveEmoji(ContextualProvider context, UniObjectNode emojiObject) {
        if (emojiObject.isNull("id")/* <== todo test this*/)
            return UnicodeEmoji.getInstance(emojiObject.get("name").asString());
        else if (CustomEmoji.TYPE.isValidData(emojiObject))
            return CustomEmoji.resolve(context, emojiObject);
        return null;
    }
}
