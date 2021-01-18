package org.comroid.crystalshard.model.message.embed;

import org.comroid.api.Polyfill;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public class EmbedVideo extends EmbedMember {
    @RootBind
    public static final GroupBind<EmbedVideo> TYPE
            = BASETYPE.subGroup("embed-video",
            (ctx, data) -> new EmbedVideo(ctx.as(Embed.class, "Context must be Embed"), data));
    public static final VarBind<EmbedVideo, String, URL, URL> IMAGE_URL
            = TYPE.createBind("url")
            .extractAs(StandardValueType.STRING)
            .andRemap(Polyfill::url)
            .build();
    public static final VarBind<EmbedVideo, Integer, Integer, Integer> HEIGHT
            = TYPE.createBind("height")
            .extractAs(StandardValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<EmbedVideo, Integer, Integer, Integer> WIDTH
            = TYPE.createBind("width")
            .extractAs(StandardValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .build();

    public EmbedVideo(Embed parent, @Nullable UniNode initialData) {
        super(parent, initialData);
    }
}
