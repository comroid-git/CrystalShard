package de.kaleidox.crystalshard.main.items.message.embed;

import de.kaleidox.crystalshard.main.items.Nameable;

import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface SentEmbed extends Embed {
    Optional<String> getTitle();
    
    Optional<String> getDescription();
    
    Optional<URL> getUrl();
    
    Optional<Instant> getTimestamp();
    
    Optional<Color> getColor();
    
    Optional<Footer> getFooter();
    
    Optional<Image> getImage();
    
    Optional<Author> getAuthor();
    
    Optional<Thumbnail> getThumbail();
    
    Optional<Video> getVideo();
    
    Optional<Provider> getProvider();
    
    Collection<Field> getFields();
    
    CompletableFuture<SentEmbed> setTitle(String title);
    
    CompletableFuture<SentEmbed> setDescription(String description);
    
    CompletableFuture<SentEmbed> setUrl(String url);
    
    CompletableFuture<SentEmbed> setTimestamp(Instant timestamp);
    
    CompletableFuture<SentEmbed> setColor(Color color);
    
    CompletableFuture<SentEmbed> modifyFields(Consumer<Collection<EmbedDraft.EditableField>> fieldModifier);
    
    CompletableFuture<SentEmbed> modifyFields(Predicate<Field> tester,
                                              Consumer<EmbedDraft.EditableField> fieldModifier);
    
// Override Methods
    @Override
    default Optional<SentEmbed> toSentEmbed() {
        return Optional.of(this);
    }
    
    default CompletableFuture<SentEmbed> setTimestampToNow() {
        return setTimestamp(Instant.now());
    }
    
    interface Footer {
        String getText();
        
        Optional<URL> getIconUrl();
        
        default EmbedDraft.Footer toDraft() {
            return EmbedDraft.Footer.BUILD(getText(), getIconUrl().map(URL::toExternalForm).orElse(null));
        }
    }
    
    interface Image {
        Optional<URL> getUrl();
        
        default EmbedDraft.Image toDraft() {
            return EmbedDraft.Image.BUILD(getUrl().map(URL::toExternalForm).orElse(null));
        }
    }
    
    interface Author extends Nameable {
        Optional<URL> getUrl();
        
        Optional<URL> getIconUrl();
        
        default EmbedDraft.Author toDraft() {
            return EmbedDraft.Author.BUILD(getName(),
                                           getUrl().map(URL::toExternalForm).orElse(null),
                                           getIconUrl().map(URL::toExternalForm).orElse(null));
        }
    }
    
    interface Thumbnail {
        Optional<URL> getUrl();
        
        default EmbedDraft.Thumbnail toDraft() {
            return EmbedDraft.Thumbnail.BUILD(getUrl().map(URL::toExternalForm).orElse(null));
        }
    }
    
    interface Field {
        String getTitle();
        
        String getText();
        
        boolean isInline();
        
        default EmbedDraft.Field toDraft() {
            return EmbedDraft.Field.BUILD(getTitle(), getText(), isInline());
        }
    }
    
    interface Video {
        Optional<URL> getUrl();
    }
    
    interface Provider {
        String getName();
        
        Optional<URL> getUrl();
    }
}
