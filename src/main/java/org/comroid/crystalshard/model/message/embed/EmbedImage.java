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

public final class EmbedImage extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedImage> TYPE
            = BASETYPE.subGroup("embed-image",
            (ctx, data) -> new EmbedImage(ctx.as(MessageEmbed.class, "Context must be Embed"), data));
    public static final VarBind<EmbedImage, String, URL, URL> IMAGE_URL
            = TYPE.createBind("url")
            .extractAs(StandardValueType.STRING)
            .andRemap(Polyfill::url)
            .build();
    public static final VarBind<EmbedImage, String, URL, URL> PROXY_URL
            = TYPE.createBind("proxy_url")
            .extractAs(StandardValueType.STRING)
            .andRemap(Polyfill::url)
            .build();
    public static final VarBind<EmbedImage, Integer, Integer, Integer> HEIGHT
            = TYPE.createBind("height")
            .extractAs(StandardValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<EmbedImage, Integer, Integer, Integer> WIDTH
            = TYPE.createBind("width")
            .extractAs(StandardValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .build();
    public final Reference<URL> url = getComputedReference(IMAGE_URL);

    public URL getUrl() {
        return url.assertion();
    }

    public EmbedImage(Embed parent, String url) {
        this(parent, (UniNode) null);

        put(IMAGE_URL, url);
    }

    public EmbedImage(Embed parent, @Nullable UniNode initialData) {
        super(parent, initialData);
    }
}
