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

public final class EmbedAuthor extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedAuthor> TYPE
            = BASETYPE.subGroup("embed-author",
            (ctx, data) -> new EmbedAuthor(ctx.as(MessageEmbed.class, "Context must be Embed"), data));
    public static final VarBind<EmbedAuthor, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<EmbedAuthor, String, URL, URL> AUTHOR_URL
            = TYPE.createBind("url")
            .extractAs(StandardValueType.STRING)
            .andRemap(Polyfill::url)
            .build();
    public static final VarBind<EmbedAuthor, String, URL, URL> ICON_URL
            = TYPE.createBind("icon_url")
            .extractAs(StandardValueType.STRING)
            .andRemap(Polyfill::url)
            .build();
    public static final VarBind<EmbedAuthor, String, URL, URL> PROXY_ICON_URL
            = TYPE.createBind("proxy_icon_url")
            .extractAs(StandardValueType.STRING)
            .andRemap(Polyfill::url)
            .build();
    public final Reference<String> name = getComputedReference(NAME);
    public final Reference<URL> url = getComputedReference(AUTHOR_URL);
    public final Reference<URL> iconUrl = getComputedReference(ICON_URL);
    public final Reference<URL> proxyIconUrl = getComputedReference(PROXY_ICON_URL);

    public String getName() {
        return name.assertion();
    }

    public URL getUrl() {
        return url.assertion();
    }

    public URL getIconUrl() {
        return iconUrl.assertion();
    }

    public URL getProxyIconUrl() {
        return proxyIconUrl.assertion();
    }

    public EmbedAuthor(Embed parent, String name, String url, String iconUrl) {
        this(parent, null);

        put(NAME, name);
        if (url != null)
            put(AUTHOR_URL, url);
        if (iconUrl != null)
            put(ICON_URL, iconUrl);
    }

    public EmbedAuthor(Embed parent, @Nullable UniNode initialData) {
        super(parent, initialData);
    }
}
