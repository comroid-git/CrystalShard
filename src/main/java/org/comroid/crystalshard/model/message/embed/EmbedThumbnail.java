package org.comroid.crystalshard.model.message.embed;

import org.comroid.api.Polyfill;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public class EmbedThumbnail extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedThumbnail> TYPE
            = BASETYPE.subGroup("embed-thumbnail");
    public static final VarBind<EmbedThumbnail, String, URL, URL> THUMBNAIL_URL
            = TYPE.createBind("url")
            .extractAs(StandardValueType.STRING)
            .andRemap(Polyfill::url)
            .build();
    public static final VarBind<EmbedThumbnail, String, URL, URL> PROXY_URL
            = TYPE.createBind("proxy_url")
            .extractAs(StandardValueType.STRING)
            .andRemap(Polyfill::url)
            .build();
    public static final VarBind<EmbedThumbnail, Integer, Integer, Integer> HEIGHT
            = TYPE.createBind("height")
            .extractAs(StandardValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<EmbedThumbnail, Integer, Integer, Integer> WIDTH
            = TYPE.createBind("width")
            .extractAs(StandardValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .build();

    public EmbedThumbnail(Embed parent, @Nullable UniNode initialData) {
        super(parent, initialData);
    }
}
