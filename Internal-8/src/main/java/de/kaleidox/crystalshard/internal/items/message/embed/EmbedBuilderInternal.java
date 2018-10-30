package de.kaleidox.crystalshard.internal.items.message.embed;

import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class EmbedBuilderInternal implements Embed.Builder {
    private final static Logger logger = new Logger(EmbedBuilderInternal.class);
    private String title;
    private String description;
    private URL url;
    private Instant timestamp;
    private Color color;
    private EmbedDraft.Footer footer;
    private EmbedDraft.Image image;
    private EmbedDraft.Thumbnail thumbnail;
    private EmbedDraft.Author author;
    private ArrayList<EmbedDraft.Field> fields = new ArrayList<>();
    private int charCounter = 0;

    // Override Methods
    @Override
    public Embed.Builder setTitle(String title) {
        charCounter = charCounter + title.length();
        testCharCounter();
        if (title.length() > Embed.Boundaries.TITLE_LENGTH) {
            throw new IllegalArgumentException("Title length must not exceed " + Embed.Boundaries.TITLE_LENGTH + " Characters!");
        }
        this.title = title;
        return this;
    }

    @Override
    public Embed.Builder setDescription(String description) {
        charCounter = charCounter + description.length();
        testCharCounter();
        if (description.length() > Embed.Boundaries.DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("Description length must not exceed " + Embed.Boundaries.DESCRIPTION_LENGTH + "Characters!");
        }
        this.description = description;
        return this;
    }

    @Override
    public Embed.Builder setUrl(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            logger.exception(e);
            this.url = null;
        }
        return this;
    }

    @Override
    public Embed.Builder setTimestamp(Instant time) {
        this.timestamp = time;
        return this;
    }

    @Override
    public Embed.Builder setColor(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public Embed.Builder setFooter(EmbedDraft.Footer footer) {
        charCounter = charCounter + footer.getText()
                .length();
        testCharCounter();
        if (footer.getText()
                .length() > Embed.Boundaries.FOOTER_LENGTH) {
            throw new IllegalArgumentException("Footer text must not exceed " + Embed.Boundaries.FOOTER_LENGTH + " characters!");
        }
        this.footer = footer;
        return this;
    }

    @Override
    public Embed.Builder setImage(EmbedDraft.Image image) {
        this.image = image;
        return this;
    }

    @Override
    public Embed.Builder setThumbnail(EmbedDraft.Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    @Override
    public Embed.Builder setAuthor(EmbedDraft.Author author) {
        charCounter = charCounter + author.getName()
                .length();
        testCharCounter();
        if (author.getName()
                .length() > Embed.Boundaries.AUTHOR_NAME_LENGTH) {
            throw new IllegalArgumentException("Author name must not exceed " + Embed.Boundaries.AUTHOR_NAME_LENGTH + " characters!");
        }
        this.author = author;
        return this;
    }

    @Override
    public Embed.Builder addField(String title, String text, boolean inline) {
        Objects.requireNonNull(title, "Embed field title must not be null.");
        Objects.requireNonNull(text, "Embed field text must not be null.");
        return addField(new EmbedDraftInternal.Field(title, text, inline));
    }

    @Override
    public Embed.Builder addField(EmbedDraft.Field field) {
        Objects.requireNonNull(field);
        charCounter = charCounter + field.getText()
                .length() + field.getTitle()
                .length();
        testCharCounter();
        if (fields.size() >= Embed.Boundaries.FIELD_COUNT) {
            throw new IllegalArgumentException("Field amount must not exceed " + Embed.Boundaries.FIELD_COUNT + "!");
        }
        if (field.getTitle()
                .isEmpty()) {
            throw new IllegalArgumentException("Field title must not be blank!");
        }
        if (field.getTitle()
                .length() > Embed.Boundaries.FIELD_TITLE_LENGTH) {
            throw new IllegalArgumentException("Field title must not exceed " + Embed.Boundaries.FIELD_TITLE_LENGTH + " characters!");
        }
        if (field.getText()
                .isEmpty()) {
            throw new IllegalArgumentException("Field text must not be blank!");
        }
        if (field.getText()
                .length() > Embed.Boundaries.FIELD_TEXT_LENGTH) {
            throw new IllegalArgumentException("Field text must not exceed " + Embed.Boundaries.FIELD_TEXT_LENGTH + " characters!");
        }
        this.fields.add(field);
        return this;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public Embed.Builder updateFields(Predicate<EmbedDraft.Field> predicate, Function<EmbedDraft.EditableField, EmbedDraft.Field> updater) {
        for (int i = 0; i < fields.size(); i++) {
            EmbedDraft.Field field = fields.get(i);
            if (predicate.test(field)) {
                fields.set(i, updater.apply(field.toEditableField()
                        .get()));
            }
        }
        return this;
    }

    @Override
    public Embed.Builder removeAllFields() {
        fields.clear();
        return this;
    }

    @Override
    public Collection<EmbedDraft.Field> getFields() {
        return Collections.unmodifiableCollection(fields);
    }

    @Override
    public EmbedDraft build() {
        return new EmbedDraftInternal(title, description, url, timestamp, color, footer, image, thumbnail, author, fields);
    }

    @Override
    public CompletableFuture<Message> send(MessageReciever sendTo) {
        return sendTo.sendMessage(this.build());
    }

    private void testCharCounter() {
        if (charCounter > Embed.Boundaries.TOTAL_CHAR_COUNT) {
            throw new IllegalArgumentException("Total embed characters must not exceed " + Embed.Boundaries.TOTAL_CHAR_COUNT + " characters!");
        }
    }
}
