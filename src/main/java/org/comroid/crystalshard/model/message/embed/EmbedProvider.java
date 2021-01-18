package org.comroid.crystalshard.model.message.embed;

import org.comroid.api.Polyfill;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public class EmbedProvider extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedProvider> TYPE
            = BASETYPE.subGroup("embed-provider",
            (ctx, data) -> new EmbedProvider(ctx.as(Embed.class, "Context must be Embed"), data));
    public static final VarBind<EmbedProvider, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<EmbedProvider, String, URL, URL> PROVIDER_URL
            = TYPE.createBind("url")
            .extractAs(StandardValueType.STRING)
            .andRemap(Polyfill::url)
            .build();

    public EmbedProvider(Embed parent, @Nullable UniNode initialData) {
        super(parent, initialData);
    }
}
