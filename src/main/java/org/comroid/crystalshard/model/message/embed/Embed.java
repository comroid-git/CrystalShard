package org.comroid.crystalshard.model.message.embed;

import org.comroid.api.Named;
import org.comroid.api.Polyfill;
import org.comroid.common.info.Described;
import org.comroid.crystalshard.Context;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.uniform.model.Serializable;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.awt.*;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public interface Embed extends Context, Serializable {
    @RootBind
    GroupBind<MessageEmbed> TYPE
            = AbstractDataContainer.BASETYPE.subGroup("embed", MessageEmbed::new);
    VarBind<MessageEmbed, String, String, String> TITLE
            = TYPE.createBind("title")
            .extractAs(StandardValueType.STRING)
            .asIdentities()
            .onceEach()
            .build();
    VarBind<MessageEmbed, String, Type, Type> EMBED_TYPE
            = TYPE.createBind("type")
            .extractAs(StandardValueType.STRING)
            .andRemap(Type::valueOf)
            .onceEach()
            .build();
    VarBind<MessageEmbed, String, String, String> DESCRIPTION
            = TYPE.createBind("description")
            .extractAs(StandardValueType.STRING)
            .asIdentities()
            .onceEach()
            .build();
    VarBind<MessageEmbed, String, URL, URL> TARGET_URL
            = TYPE.createBind("url")
            .extractAs(StandardValueType.STRING)
            .andRemap(Polyfill::url)
            .onceEach()
            .build();
    VarBind<MessageEmbed, String, Instant, Instant> TIMESTAMP
            = TYPE.createBind("timestamp")
            .extractAs(StandardValueType.STRING)
            .andRemap(Instant::parse)
            .onceEach()
            .build();
    VarBind<MessageEmbed, Integer, Color, Color> COLOR
            = TYPE.createBind("color")
            .extractAs(StandardValueType.INTEGER)
            .andRemap(Color::new)
            .onceEach()
            .build();
    VarBind<MessageEmbed, UniObjectNode, EmbedFooter, EmbedFooter> FOOTER
            = TYPE.createBind("footer")
            .extractAsObject()
            .andResolve(EmbedFooter::new)
            .onceEach()
            .build();
    VarBind<MessageEmbed, UniObjectNode, EmbedImage, EmbedImage> IMAGE
            = TYPE.createBind("image")
            .extractAsObject()
            .andResolve(EmbedImage::new)
            .onceEach()
            .build();
    VarBind<MessageEmbed, UniObjectNode, EmbedThumbnail, EmbedThumbnail> THUMBNAIL
            = TYPE.createBind("thumbnail")
            .extractAsObject()
            .andResolve(EmbedThumbnail::new)
            .onceEach()
            .build();
    VarBind<MessageEmbed, UniObjectNode, EmbedVideo, EmbedVideo> VIDEO
            = TYPE.createBind("video")
            .extractAsObject()
            .andResolve(EmbedVideo::new)
            .onceEach()
            .build();
    VarBind<MessageEmbed, UniObjectNode, EmbedProvider, EmbedProvider> PROVIDER
            = TYPE.createBind("provider")
            .extractAsObject()
            .andResolve(EmbedProvider::new)
            .onceEach()
            .build();
    VarBind<MessageEmbed, UniObjectNode, EmbedAuthor, EmbedAuthor> AUTHOR
            = TYPE.createBind("author")
            .extractAsObject()
            .andResolve(EmbedAuthor::new)
            .onceEach()
            .build();
    VarBind<MessageEmbed, UniObjectNode, EmbedField, List<EmbedField>> FIELDS
            = TYPE.createBind("fields")
            .extractAsArray()
            .andResolve(EmbedField::new)
            .intoCollection((Supplier<List<EmbedField>>) ArrayList::new)
            .setDefaultValue(Collections::emptyList)
            .build();

    Type getType();

    String getTitle();

    String getDescription();

    String getUrl();

    Instant getTimestamp();

    Color getColor();

    EmbedAuthor getAuthor();

    EmbedThumbnail getThumbnail();

    EmbedImage getImage();

    EmbedFooter getFooter();

    List<EmbedField> getFields();

    enum Type implements Named, Described {
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
