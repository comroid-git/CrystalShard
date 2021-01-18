package org.comroid.crystalshard.model.message.embed;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Named;
import org.comroid.api.Polyfill;
import org.comroid.common.info.Described;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.net.URL;
import java.time.Instant;

public final class Embed extends AbstractDataContainer {
    @RootBind
    public static final GroupBind<Embed> TYPE
            = BASETYPE.subGroup("embed", Embed::new);
    public static final VarBind<Embed, String, String, String> TITLE
            = TYPE.createBind("title")
            .extractAs(StandardValueType.STRING)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Embed, String, Type, Type> EMBED_TYPE
            = TYPE.createBind("type")
            .extractAs(StandardValueType.STRING)
            .andRemap(Type::valueOf)
            .onceEach()
            .build();
    public static final VarBind<Embed, String, String, String> DESCRIPTION
            = TYPE.createBind("description")
            .extractAs(StandardValueType.STRING)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Embed, String, URL, URL> TARGET_URL
            = TYPE.createBind("url")
            .extractAs(StandardValueType.STRING)
            .andRemap(Polyfill::url)
            .onceEach()
            .build();
    public static final VarBind<Embed, String, Instant, Instant> TIMESTAMP
            = TYPE.createBind("timestamp")
            .extractAs(StandardValueType.STRING)
            .andRemap(Instant::parse)
            .onceEach()
            .build();
    public static final VarBind<Embed, Integer, Color, Color> COLOR
            = TYPE.createBind("color")
            .extractAs(StandardValueType.INTEGER)
            .andRemap(Color::new)
            .onceEach()
            .build();
    public static final VarBind<Embed, UniObjectNode, EmbedFooter, EmbedFooter> FOOTER
            = TYPE.createBind("footer")
            .extractAsObject()
            .andResolve(EmbedFooter::new)
            .onceEach()
            .build();
    public static final VarBind<Embed, UniObjectNode, EmbedImage, EmbedImage> IMAGE
            = TYPE.createBind("image")
            .extractAsObject()
            .andResolve(EmbedImage::new)
            .onceEach()
            .build();
    public static final VarBind<Embed, UniObjectNode, EmbedThumbnail, EmbedThumbnail> THUMBNAIL
            = TYPE.createBind("thumbnail")
            .extractAsObject()
            .andResolve(EmbedThumbnail::new)
            .onceEach()
            .build();
    public static final VarBind<Embed, UniObjectNode, EmbedVideo, EmbedVideo> VIDEO
            = TYPE.createBind("video")
            .extractAsObject()
            .andResolve(EmbedVideo::new)
            .onceEach()
            .build();
    public static final VarBind<Embed, UniObjectNode, EmbedProvider, EmbedProvider> PROVIDER
            = TYPE.createBind("provider")
            .extractAsObject()
            .andResolve(EmbedProvider::new)
            .onceEach()
            .build();
    public static final VarBind<Embed, UniObjectNode, EmbedAuthor, EmbedAuthor> AUTHOR
            = TYPE.createBind("author")
            .extractAsObject()
            .andResolve(EmbedAuthor::new)
            .onceEach()
            .build();
    public static final VarBind<Embed, UniObjectNode, EmbedField, Span<EmbedField>> FIELDS
            = TYPE.createBind("fields")
            .extractAsArray()
            .andResolve(EmbedField::new)
            .intoSpan()
            .build();

    public Embed(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }

    public enum Type implements Named, Described {
        rich("generic embed rendered from embed attributes"),
        image("image embed"),
        video("video embed"),
        gifv("animated gif image embed rendered as a video embed"),
        article("article embed"),
        link("link embed");

        private final String description;

        @Override
        public String getDescription() {
            return description;
        }

        Type(String description) {
            this.description = description;
        }
    }
}
