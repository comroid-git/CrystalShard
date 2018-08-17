package de.kaleidox.crystalshard.main.items.message.embed;

import de.kaleidox.crystalshard.internal.items.message.embed.EmbedDraftInternal;
import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.util.Castable;

import java.awt.*;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
public interface EmbedDraft extends Embed {
    Optional<String> getTitle();

    Optional<String> getDescription();

    Optional<URL> getUrl();

    Optional<Instant> getTimestamp();

    Optional<Color> getColor();

    Optional<Footer> getFooter();

    Optional<Image> getImage();

    Optional<Thumbnail> getThumbnail();

    Optional<Author> getAuthor();

    List<Field> getFields();

    interface Footer {
        static Footer BUILD(String text,
                            String iconUrl) {
            return new EmbedDraftInternal.Footer(text, iconUrl);
        }

        String getText();

        Optional<URL> getUrl();
    }

    interface Image {
        static Image BUILD(String url) {
            return new EmbedDraftInternal.Image(url);
        }

        Optional<URL> getUrl();
    }

    interface Author extends Nameable {
        static Author BUILD(String name,
                            String url,
                            String iconUrl) {
            return new EmbedDraftInternal.Author(name, url, iconUrl);
        }
    }

    interface Thumbnail {
        static Thumbnail BUILD(String url) {
            return new EmbedDraftInternal.Thumbnail(url);
        }

        Optional<URL> getUrl();
    }

    interface Field extends Castable<Field> {
        static Field BUILD(String name,
                           String text,
                           boolean inline) {
            return new EmbedDraftInternal.Field(name, text, (Objects.nonNull(inline) && inline));
        }

        String getTitle();

        String getText();

        boolean isInline();

        default Optional<EditableField> toEditableField() {
            return Optional.of(new EmbedDraftInternal.EditableField(this));
        }
    }

    interface EditableField extends Field {
        static EditableField BUILD(Field fromField) {
            return new EmbedDraftInternal.EditableField(fromField);
        }
    }
}
