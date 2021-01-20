package org.comroid.crystalshard.entity.message;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.cdn.CDNEndpoint;
import org.comroid.crystalshard.cdn.ImageType;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.net.URL;

public final class MessageApplication extends Snowflake.Abstract {
    @RootBind
    public static final GroupBind<MessageApplication> TYPE
            = BASETYPE.subGroup("message-application", MessageApplication::resolve);
    public static final VarBind<MessageApplication, String, URL, URL> COVER_IMAGE
            = TYPE.createBind("cover_image")
            .extractAs(StandardValueType.STRING)
            .andResolve((msgapp, assetId) -> CDNEndpoint.APPLICATION_ASSET.complete(msgapp.getID(), assetId, ImageType.PNG).getURL())
            .build();
    public static final VarBind<MessageApplication, String, String, String> DESCRIPTION
            = TYPE.createBind("description")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<MessageApplication, String, URL, URL> ICON
            = TYPE.createBind("icon")
            .extractAs(StandardValueType.STRING)
            .andResolve((msgapp, iconId) -> CDNEndpoint.APPLICATION_ICON.complete(msgapp.getID(), iconId, ImageType.PNG).getURL())
            .build();
    public static final VarBind<MessageApplication, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();

    private MessageApplication(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.MESSAGE_APPLICATION);
    }

    public static MessageApplication resolve(ContextualProvider context, UniNode data) {
        return Snowflake.resolve(context, data, SnowflakeCache::getMessageApplication, MessageApplication::new);
    }
}
