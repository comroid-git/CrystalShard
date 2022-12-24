package org.comroid.crystalshard.model.message.embed;

import org.comroid.api.ContextualProvider;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.net.URL;
import java.time.Instant;
import java.util.List;

public final class MessageEmbed extends DataContainerBase<DiscordDataContainer> implements Embed, DiscordDataContainer {
    public final Reference<Type> type = getComputedReference(EMBED_TYPE);
    public final Reference<String> title = getComputedReference(TITLE);
    public final Reference<String> description = getComputedReference(DESCRIPTION);
    public final Reference<URL> url = getComputedReference(TARGET_URL);
    public final Reference<Instant> timestamp = getComputedReference(TIMESTAMP);
    public final Reference<Color> color = getComputedReference(COLOR);
    public final Reference<EmbedAuthor> author = getComputedReference(AUTHOR);
    public final Reference<EmbedThumbnail> thumbnail = getComputedReference(THUMBNAIL);
    public final Reference<EmbedImage> image = getComputedReference(IMAGE);
    public final Reference<EmbedVideo> video = getComputedReference(VIDEO);
    public final Reference<EmbedFooter> footer = getComputedReference(FOOTER);
    public final Reference<EmbedProvider> provider = getComputedReference(PROVIDER);

    @Override
    public Type getType() {
        return type.assertion();
    }

    @Override
    public String getTitle() {
        return title.assertion();
    }

    @Override
    public String getDescription() {
        return description.assertion();
    }

    @Override
    public String getUrl() {
        return url.into(URL::toExternalForm);
    }

    @Override
    public Instant getTimestamp() {
        return timestamp.assertion();
    }

    @Override
    public Color getColor() {
        return color.assertion();
    }

    @Override
    public EmbedAuthor getAuthor() {
        return author.assertion();
    }

    @Override
    public EmbedThumbnail getThumbnail() {
        return thumbnail.assertion();
    }

    @Override
    public EmbedImage getImage() {
        return image.assertion();
    }

    public EmbedVideo getVideo() {
        return video.assertion();
    }

    @Override
    public EmbedFooter getFooter() {
        return footer.assertion();
    }

    @Override
    public List<EmbedField> getFields() {
        return getComputedReference(FIELDS).assertion();
    }

    public EmbedProvider getProvider() {
        return provider.assertion();
    }

    public MessageEmbed(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }

}
