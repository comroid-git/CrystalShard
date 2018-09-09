package de.kaleidox.crystalshard.main.items.message.embed;

import de.kaleidox.crystalshard.internal.items.message.embed.EmbedDraftInternal;
import de.kaleidox.crystalshard.internal.util.Container;
import de.kaleidox.crystalshard.main.items.Nameable;

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

    interface Footer extends Container.Interface {
        String getText();

        Optional<URL> getIconUrl();

        static Footer BUILD(String text,
                            String iconUrl) {
            return new EmbedDraftInternal.Footer(text, iconUrl);
        }
    }

    interface Image extends Container.Interface {
        Optional<URL> getUrl();

        static Image BUILD(String url) {
            return new EmbedDraftInternal.Image(url);
        }
    }

    interface Author extends Nameable, Container.Interface {
        Optional<URL> getUrl();

        Optional<URL> getIconUrl();

        static Author BUILD(String name,
                            String url,
                            String iconUrl) {
            return new EmbedDraftInternal.Author(name, url, iconUrl);
        }
    }

    interface Thumbnail extends Container.Interface {
        Optional<URL> getUrl();

        static Thumbnail BUILD(String url) {
            return new EmbedDraftInternal.Thumbnail(url);
        }
    }

    interface Field {
        String getTitle();

        String getText();

        boolean isInline();

        default Optional<EditableField> toEditableField() {
            return Optional.of(new EmbedDraftInternal.EditableField(this));
        }

        int getTotalCharCount();

        static Field BUILD(String title,
                           String text,
                           boolean inline) {
            return new EmbedDraftInternal.Field(title, text, (Objects.nonNull(inline) && inline));
        }
    }

    interface EditableField extends Field {
        static EditableField BUILD(Field fromField) {
            return new EmbedDraftInternal.EditableField(fromField);
        }
    }
}
