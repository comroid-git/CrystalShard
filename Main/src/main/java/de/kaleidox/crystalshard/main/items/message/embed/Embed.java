package de.kaleidox.crystalshard.main.items.message.embed;

import de.kaleidox.crystalshard.internal.InternalInjector;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.main.util.Castable;

import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public interface Embed extends Castable<Embed> {
    EmbedDraft toEmbedDraft();

    Builder toBuilder();

    Optional<SentEmbed> toSentEmbed();

    static Builder BUILDER() {
        return InternalInjector.newInstance(Embed.Builder.class);
    }

    interface Builder {
        Builder setTitle(String title);

        Builder setDescription(String description);

        Builder setUrl(String url);

        Builder setColor(Color color);

        Builder addField(EmbedDraft.Field field);

        Builder removeAllFields();

        Collection<EmbedDraft.Field> getFields();

        EmbedDraft build();

        CompletableFuture<Message> send(MessageReciever sendTo);

        default Builder setTimestampNow() {
            return setTimestamp(Instant.now());
        }

        Builder setTimestamp(Instant time);

        default Builder setFooter(String footerText) {
            return setFooter(EmbedDraft.Footer.BUILD(footerText, null));
        }

        Builder setFooter(EmbedDraft.Footer footer);

        default Builder setFooter(String footerText, String iconUrl) {
            return setFooter(EmbedDraft.Footer.BUILD(footerText, null));
        }

        default Builder setImage(String imageUrl) {
            return setImage(EmbedDraft.Image.BUILD(imageUrl));
        }

        Builder setImage(EmbedDraft.Image image);

        default Builder setThumbnail(String thumbnailUrl) {
            return setThumbnail(EmbedDraft.Thumbnail.BUILD(thumbnailUrl));
        }

        Builder setThumbnail(EmbedDraft.Thumbnail thumbnail);

        default Builder setAuthor(User user) {
            return setAuthor(user, null);
        }

        default Builder setAuthor(User user, String url) {
            return setAuthor(EmbedDraft.Author.BUILD(user.getName(), url, user.getAvatarUrl()
                    .map(URL::toExternalForm)
                    .orElse(null)));
        }

        Builder setAuthor(EmbedDraft.Author author);

        default Builder setAuthor(String name) {
            return setAuthor(name, null, null);
        }

        default Builder setAuthor(String name, String url, String iconUrl) {
            return setAuthor(EmbedDraft.Author.BUILD(name, url, iconUrl));
        }

        default Builder addField(String title, String text) {
            return addField(title, text, false);
        }

        Builder addField(String title, String text, boolean inline);

        default Builder addInlineField(String title, String text) {
            return addField(title, text, true);
        }

        default Builder updateAllFields(Function<EmbedDraft.EditableField, EmbedDraft.Field> updater) {
            return updateFields(field -> true, updater);
        }

        Builder updateFields(Predicate<EmbedDraft.Field> predicate, Function<EmbedDraft.EditableField, EmbedDraft.Field> updater);
    }

    interface Boundaries {
        int TITLE_LENGTH = 256;

        int DESCRIPTION_LENGTH = 2048;

        int FIELD_COUNT = 25;

        int FIELD_TITLE_LENGTH = 256;

        int FIELD_TEXT_LENGTH = 1024;

        int FOOTER_LENGTH = 2048;

        int AUTHOR_NAME_LENGTH = 256;

        int TOTAL_CHAR_COUNT = 6000;
    }
}
