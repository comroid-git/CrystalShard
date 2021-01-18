package org.comroid.crystalshard.entity.message;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Polyfill;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.net.URL;

public final class MessageAttachment extends Snowflake.Abstract {
    @RootBind
    public static final GroupBind<MessageAttachment> TYPE
            = BASETYPE.subGroup("message-attachment", MessageAttachment::resolve);
    public static final VarBind<MessageAttachment, String, String, String> FILENAME
            = TYPE.createBind("filename")
            .extractAs(StandardValueType.STRING)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<MessageAttachment, Integer, Integer, Integer> SIZE
            = TYPE.createBind("size")
            .extractAs(StandardValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<MessageAttachment, String, URL, URL> SOURCE_URL
            = TYPE.createBind("url")
            .extractAs(StandardValueType.STRING)
            .andRemap(Polyfill::url)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<MessageAttachment, String, URL, URL> PROXY_URL
            = TYPE.createBind("proxy_url")
            .extractAs(StandardValueType.STRING)
            .andRemap(Polyfill::url)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<MessageAttachment, Integer, Integer, Integer> HEIGHT
            = TYPE.createBind("height")
            .extractAs(StandardValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<MessageAttachment, Integer, Integer, Integer> WIDTH
            = TYPE.createBind("width")
            .extractAs(StandardValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .build();

    private MessageAttachment(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.MESSAGE_ATTACHMENT);
    }

    public static MessageAttachment resolve(ContextualProvider context, UniNode data) {
        return Snowflake.resolve(context, data, SnowflakeCache::getMessageAttachment, MessageAttachment::new);
    }
}
