package de.kaleidox.crystalshard.main.items.message.embed;

import de.kaleidox.crystalshard.internal.items.message.embed.EmbedBuilderInternal;
import de.kaleidox.crystalshard.internal.items.message.embed.EmbedDraftInternal;
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
    
    interface Builder {
        Builder setTitle(String title);
        
        Builder setDescription(String description);
        
        Builder setUrl(String url);
        
        Builder setTimestamp(Instant time);
        
        Builder setColor(Color color);
        
        Builder setFooter(EmbedDraft.Footer footer);
        
        Builder setImage(EmbedDraft.Image image);
        
        Builder setThumbnail(EmbedDraft.Thumbnail thumbnail);
        
        Builder setAuthor(EmbedDraft.Author author);
        
        Builder addField(String title, String text, boolean inline);
        
        Builder addField(EmbedDraft.Field field);
        
        Builder updateFields(Predicate<EmbedDraft.Field> predicate, Function<EmbedDraft.EditableField,
                EmbedDraft.Field> updater);
        
        Builder removeAllFields();
        
        Collection<EmbedDraft.Field> getFields();
        
        EmbedDraft build();
        
        CompletableFuture<Message> send(MessageReciever sendTo);
        
        default Builder setTimestampNow() {
            return setTimestamp(Instant.now());
        }
        
        default Builder setFooter(String footerText) {
            return setFooter(new EmbedDraftInternal.Footer(footerText, null));
        }
        
        default Builder setFooter(String footerText, String iconUrl) {
            return setFooter(new EmbedDraftInternal.Footer(footerText, iconUrl));
        }
        
        default Builder setImage(String imageUrl) {
            return setImage(new EmbedDraftInternal.Image(imageUrl));
        }
        
        default Builder setThumbnail(String thumbnailUrl) {
            return setThumbnail(new EmbedDraftInternal.Thumbnail(thumbnailUrl));
        }
        
        default Builder setAuthor(User user, String url) {
            return setAuthor(EmbedDraft.Author.BUILD(user.getName(),
                                                     url,
                                                     user.getAvatarUrl()
                                                             .map(URL::toExternalForm)
                                                             .orElse(null)));
        }
        
        default Builder setAuthor(User user) {
            return setAuthor(user, null);
        }
        
        default Builder setAuthor(String name) {
            return setAuthor(name, null, null);
        }
        
        default Builder setAuthor(String name, String url, String iconUrl) {
            return setAuthor(new EmbedDraftInternal.Author(name, url, iconUrl));
        }
        
        default Builder addField(String title, String text) {
            return addField(title, text, false);
        }
        
        default Builder addInlineField(String title, String text) {
            return addField(title, text, true);
        }
        
        default Builder updateAllFields(Function<EmbedDraft.EditableField, EmbedDraft.Field> updater) {
            return updateFields(field -> true, updater);
        }
    }
    
    interface Boundaries {
        // Static Fields
        int TITLE_LENGTH       = 256;
        
        int DESCRIPTION_LENGTH = 2048;
        
        int FIELD_COUNT        = 25;
        
        int FIELD_TITLE_LENGTH = 256;
        
        int FIELD_TEXT_LENGTH  = 1024;
        
        int FOOTER_LENGTH      = 2048;
        
        int AUTHOR_NAME_LENGTH = 256;
        
        int TOTAL_CHAR_COUNT   = 6000;
    }
    
    // Static members
    // Static membe
    static Builder BUILDER() {
        return new EmbedBuilderInternal();
    }
}
