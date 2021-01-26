package org.comroid.crystalshard.model.message.embed;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.Context;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniArrayNode;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.container.DataContainer;

import java.awt.*;
import java.time.Instant;
import java.util.function.Predicate;

public class EmbedBuilder implements Embed {
    private final Context context;
    private final Span<EmbedField> fields = new Span<>();
    private Type type = Type.rich;
    private String title;
    private String description;
    private String url;
    private Instant timestamp;
    private Color color;
    private EmbedAuthor author;
    private EmbedThumbnail thumbnail;
    private EmbedImage image;
    private EmbedFooter footer;

    @Override
    public Type getType() {
        return type;
    }

    public EmbedBuilder setType(Type type) {
        this.type = type;
        return this;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public EmbedBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public EmbedBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public EmbedBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    public EmbedBuilder(Context context) {
        this.context = context;
    }

    public EmbedBuilder setTimeToNow() {
        return setTimestamp(Instant.now());
    }

    public EmbedBuilder setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public Color getColor() {
        return color;
    }

    public EmbedBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public EmbedAuthor getAuthor() {
        return author;
    }

    public EmbedBuilder setAuthor(String name) {
        return setAuthor(name, null);
    }

    public EmbedBuilder setAuthor(String name, String url) {
        return setAuthor(name, url, null);
    }

    public EmbedBuilder setAuthor(String name, String url, String iconUrl) {
        return setAuthor(new EmbedAuthor(this, name, url, iconUrl));
    }

    public EmbedBuilder setAuthor(EmbedAuthor author) {
        this.author = author;
        return this;
    }

    @Override
    public EmbedThumbnail getThumbnail() {
        return thumbnail;
    }

    public EmbedBuilder setThumbnail(String url) {
        return setThumbnail(new EmbedThumbnail(this, url));
    }

    public EmbedBuilder setThumbnail(EmbedThumbnail thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    @Override
    public EmbedImage getImage() {
        return image;
    }

    public EmbedBuilder setImage(String url) {
        return setImage(new EmbedImage(this, url));
    }

    public EmbedBuilder setImage(EmbedImage image) {
        this.image = image;
        return this;
    }

    @Override
    public EmbedFooter getFooter() {
        return footer;
    }

    public EmbedBuilder setFooter(String text) {
        return setFooter(text, null);
    }

    public EmbedBuilder setFooter(String text, String iconUrl) {
        return setFooter(new EmbedFooter(this, text, iconUrl));
    }

    public EmbedBuilder setFooter(EmbedFooter footer) {
        this.footer = footer;
        return this;
    }

    @Override
    public Span<EmbedField> getFields() {
        return fields;
    }

    public EmbedBuilder addField(String name, String value) {
        return addField(name, value, false);
    }

    public EmbedBuilder addInlineField(String name, String value) {
        return addField(name, value, true);
    }

    public EmbedBuilder addField(String name, String value, boolean inline) {
        return addField(new EmbedField(this, name, value, inline));
    }

    public EmbedBuilder addField(EmbedField field) {
        fields.add(field);
        return this;
    }

    public EmbedBuilder removeFieldIf(Predicate<EmbedField> filter) {
        fields.removeIf(filter);
        return this;
    }

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return context;
    }

    @Override
    public UniNode toUniNode() {
        final UniObjectNode embed = context.getSerializer().createUniObjectNode();

        embed.put(EMBED_TYPE, type.getName());

        if (title != null)
            embed.put(TITLE, title);
        if (description != null)
            embed.put(DESCRIPTION, description);
        if (url != null)
            embed.put(TARGET_URL, url);
        if (timestamp != null)
            embed.put(TIMESTAMP, timestamp.toString());
        if (color != null)
            embed.put(COLOR, color.getRGB());
        if (footer != null)
            footer.toObjectNode(embed.putObject(FOOTER));
        if (image != null)
            image.toObjectNode(embed.putObject(IMAGE));
        if (thumbnail != null)
            thumbnail.toObjectNode(embed.putObject(THUMBNAIL));
        if (author != null)
            author.toObjectNode(embed.putObject(AUTHOR));
        if (fields.size() > 0) {
            final UniArrayNode fields = embed.putArray(FIELDS);
            this.fields.stream()
                    .map(DataContainer::toUniNode)
                    .forEach(fields::add);
        }

        return embed;
    }
}
