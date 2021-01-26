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

public class EmbedFooter extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedFooter> TYPE
            = BASETYPE.subGroup("embed-footer",
            (ctx, data) -> new EmbedFooter(ctx.as(MessageEmbed.class, "Context must be Embed"), data));
    public static final VarBind<EmbedFooter, String, String, String> TEXT
            = TYPE.createBind("text")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<EmbedFooter, String, URL, URL> ICON_URL
            = TYPE.createBind("icon_url")
            .extractAs(StandardValueType.STRING)
            .andRemap(Polyfill::url)
            .build();
    public static final VarBind<EmbedFooter, String, URL, URL> PROXY_ICON_URL
            = TYPE.createBind("proxy_icon_url")
            .extractAs(StandardValueType.STRING)
            .andRemap(Polyfill::url)
            .build();
    public final Reference<String> text = getComputedReference(TEXT);
    public final Reference<URL> iconUrl = getComputedReference(ICON_URL);

    public String getText() {
        return text.assertion();
    }

    public URL getIconUrl() {
        return iconUrl.assertion();
    }

    public EmbedFooter(Embed parent, String text, String iconUrl) {
        this(parent, null);

        put(TEXT, text);
        if (iconUrl != null)
            put(ICON_URL, iconUrl);
    }

    public EmbedFooter(Embed parent, @Nullable UniNode initialData) {
        super(parent, initialData);
    }
}
