package org.comroid.crystalshard.model.message.embed;

import org.comroid.api.Polyfill;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public final class EmbedThumbnail extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedThumbnail> TYPE
            = BASETYPE.subGroup("embed-thumbnail",
            (ctx, data) -> new EmbedThumbnail(ctx.as(MessageEmbed.class, "Context must be Embed"), data));
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
    public final Reference<URL> url = getComputedReference(THUMBNAIL_URL);

    public URL getUrl() {
        return url.assertion();
    }

    public EmbedThumbnail(Embed parent, String url) {
        this(parent, (UniNode) null);

        put(THUMBNAIL_URL, url);
    }

    public EmbedThumbnail(Embed parent, @Nullable UniNode initialData) {
        super(parent, initialData);
    }
}
