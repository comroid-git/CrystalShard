package de.kaleidox.crystalshard.main.items.message.embed;

import de.kaleidox.crystalshard.internal.InternalDelegate;
import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.util.FileContainer;

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

    interface Footer extends FileContainer.Containable {
        static Footer BUILD(String text, String iconUrl) {
            return InternalDelegate.newInstance(Footer.class, text, iconUrl);
        }

        String getText();

        Optional<URL> getIconUrl();
    }

    interface Image extends FileContainer.Containable {
        static Image BUILD(String url) {
            return InternalDelegate.newInstance(Image.class, url);
        }

        Optional<URL> getUrl();
    }

    interface Author extends Nameable, FileContainer.Containable {
        static Author BUILD(String name, String url, String iconUrl) {
            return InternalDelegate.newInstance(Author.class, name, url, iconUrl);
        }

        Optional<URL> getUrl();

        Optional<URL> getIconUrl();
    }

    interface Thumbnail extends FileContainer.Containable {
        static Thumbnail BUILD(String url) {
            return InternalDelegate.newInstance(Thumbnail.class, url);
        }

        Optional<URL> getUrl();
    }

    interface Field {
        static Field BUILD(String title, String text, boolean inline) {
            return InternalDelegate.newInstance(Field.class, title, text, (Objects.nonNull(inline) && inline));
        }

        String getTitle();

        String getText();

        boolean isInline();

        int getTotalCharCount();

        default Optional<EditableField> toEditableField() {
            return Optional.of(InternalDelegate.newInstance(EditableField.class, this));
        }
    }

    interface EditableField extends Field {
        static EditableField BUILD(Field fromField) {
            return InternalDelegate.newInstance(EditableField.class, fromField);
        }
    }
}
