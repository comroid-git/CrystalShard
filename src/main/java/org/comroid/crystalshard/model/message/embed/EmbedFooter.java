package org.comroid.crystalshard.model.message.embed;

import org.comroid.api.Polyfill;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public class EmbedFooter extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedFooter> TYPE
            = BASETYPE.subGroup("embed-footer");
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

    public EmbedFooter(Embed parent, @Nullable UniNode initialData) {
        super(parent, initialData);
    }
}
