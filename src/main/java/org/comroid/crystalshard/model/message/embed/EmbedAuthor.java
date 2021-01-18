package org.comroid.crystalshard.model.message.embed;

import org.comroid.api.Polyfill;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public class EmbedAuthor extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedAuthor> TYPE
            = BASETYPE.subGroup("embed-author",
            (ctx, data) -> new EmbedAuthor(ctx.as(Embed.class, "Context must be Embed"), data));
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

    public EmbedAuthor(Embed parent, @Nullable UniNode initialData) {
        super(parent, initialData);
    }
}
