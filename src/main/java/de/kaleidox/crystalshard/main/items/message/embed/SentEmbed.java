package de.kaleidox.crystalshard.main.items.message.embed;

import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.items.message.Message;
import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface SentEmbed extends Embed {
    /**
     * Gets the message that this embed is part of.
     *
     * @return The parenting message.
     */
    Message getMessage();
    
    /**
     * Gets the title of the embed.
     *
     * @return The optional title of the embed.
     */
    Optional<String> getTitle();
    
    /**
     * Gets the description of the embed.
     *
     * @return The optional description of the embed.
     */
    Optional<String> getDescription();
    
    /**
     * Gets the URL that the Embed's title is pointing at.
     *
     * @return The optional url of the embed.
     */
    Optional<URL> getUrl();
    
    /**
     * Gets the timestamp that was set to the embed.
     *
     * @return The optional timestamp of the embed.
     */
    Optional<Instant> getTimestamp();
    
    /**
     * Gets the color of the embed.
     *
     * @return The optional color of the embed.
     */
    Optional<Color> getColor();
    
    /**
     * Gets the footer of the embed.
     *
     * @return The optional footer of the embed.
     */
    Optional<Footer> getFooter();
    
    /**
     * Gets the image of the embed.
     *
     * @return The optional image of the embed.
     */
    Optional<Image> getImage();
    
    /**
     * Gets the author of the embed.
     *
     * @return The optional author of the embed.
     */
    Optional<Author> getAuthor();
    
    /**
     * Gets the thumbnail of the embed.
     *
     * @return The optional thumbnail of the embed.
     */
    Optional<Thumbnail> getThumbail();
    
    /**
     * Gets the video of the embed. Currently, videos may only be embedded by Discord themselves, e.g. when sending a
     * YouTube link.
     *
     * @return The optional video of the embed.
     */
    Optional<Video> getVideo();
    
    /**
     * Gets the provider of the embed.
     *
     * @return The optional provider of the embed.
     */
    Optional<Provider> getProvider();
    
    /**
     * Gets a list of all embed fields. This list may be Empty.
     *
     * @return A list of all field.
     */
    Collection<Field> getFields();
    
    /**
     * Gets an updater to modify several fields of this embed.
     *
     * @return An Updater to update this embed.
     * @throws IllegalAccessException If the embed was not sent by you, as you may never other people's messages.
     */
    Updater getUpdater() throws IllegalAccessException;
    
    /**
     * Modifies the title of the sent embed.
     * If this embed was not sent by the bot, the returned future will complete exceptionally with a
     * {@link IllegalAccessException}, as you may never edit someone else's messages.
     * <p>
     * If you are modifying several aspects of an embed, please consider using the {@link Updater}, which you can
     * acquire with {@link #getUpdater()}.
     *
     * @param title The new title to set.
     * @return A future that completes with the new embed when the embed was edited.
     * @see Message#edit(EmbedDraft)
     */
    CompletableFuture<SentEmbed> setTitle(String title);
    
    /**
     * Modifies the description of the sent embed.
     * If this embed was not sent by the bot, the returned future will complete exceptionally with a
     * {@link IllegalAccessException}, as you may never edit someone else's messages.
     * <p>
     * If you are modifying several aspects of an embed, please consider using the {@link Updater}, which you can
     * acquire with {@link #getUpdater()}.
     *
     * @param description The new description to set.
     * @return A future that completes with the new embed when the embed was edited.
     * @see Message#edit(EmbedDraft)
     */
    CompletableFuture<SentEmbed> setDescription(String description);
    
    /**
     * Modifies the URL of the sent embed.
     * If this embed was not sent by the bot, the returned future will complete exceptionally with a
     * {@link IllegalAccessException}, as you may never edit someone else's messages.
     * <p>
     * If you are modifying several aspects of an embed, please consider using the {@link Updater}, which you can
     * acquire with {@link #getUpdater()}.
     *
     * @param url The new URL to set.
     * @return A future that completes with the new embed when the embed was edited.
     * @see Message#edit(EmbedDraft)
     */
    CompletableFuture<SentEmbed> setUrl(String url);
    
    /**
     * Modifies the timestamp of the sent embed.
     * If this embed was not sent by the bot, the returned future will complete exceptionally with a
     * {@link IllegalAccessException}, as you may never edit someone else's messages.
     * <p>
     * If you are modifying several aspects of an embed, please consider using the {@link Updater}, which you can
     * acquire with {@link #getUpdater()}.
     *
     * @param timestamp The new timestamp to set.
     * @return A future that completes with the new embed when the embed was edited.
     * @see Message#edit(EmbedDraft)
     */
    CompletableFuture<SentEmbed> setTimestamp(Instant timestamp);
    
    /**
     * Modifies the color of the sent embed.
     * If this embed was not sent by the bot, the returned future will complete exceptionally with a
     * {@link IllegalAccessException}, as you may never edit someone else's messages.
     * <p>
     * If you are modifying several aspects of an embed, please consider using the {@link Updater}, which you can
     * acquire with {@link #getUpdater()}.
     *
     * @param color The new color to set.
     * @return A future that completes with the new embed when the embed was edited.
     * @see Message#edit(EmbedDraft)
     */
    CompletableFuture<SentEmbed> setColor(Color color);
    
    /**
     * 7
     * Modifies the fields within the embed after the given modifying consumer. If this embed was not sent by the bot,
     * the returned future will complete exceptionally with a {@link IllegalAccessException}, as you may never edit
     * someone else's messages.7
     * <p>
     * If you are modifying several aspects of an embed, please consider using the {@link Updater}, which you can
     * acquire with {@link #getUpdater()}.
     *
     * @param fieldModifier The modifier for the collection of fields.
     * @return A future that completes with the new embed when the embed was edited.
     * @see Message#edit(EmbedDraft)
     */
    CompletableFuture<SentEmbed> modifyFields(Consumer<Collection<EmbedDraft.EditableField>> fieldModifier);
    
    /**
     * 7
     * Modifies all fields that match the given predicate.
     * The modifier will only be applied to the fields that test TRUE.
     * If this embed was not sent by the bot, the returned future will complete exceptionally with a
     * {@link IllegalAccessException}, as you may never edit someone else's messages.7
     * <p>
     * If you are modifying several aspects of an embed, please consider using the {@link Updater}, which you can
     * acquire with {@link #getUpdater()}.
     *
     * @param tester        The predicate to filter fields.
     * @param fieldModifier A consumer to modify the field.
     * @return A future that completes with the new embed when the embed was edited.
     * @see Message#edit(EmbedDraft)
     */
    CompletableFuture<SentEmbed> modifyFields(Predicate<Field> tester,
                                              Consumer<EmbedDraft.EditableField> fieldModifier);
    
    // Override Methods
    @Override
    default Optional<SentEmbed> toSentEmbed() {
        return Optional.of(this);
    }
    
    interface Updater {
        /**
         * Modifies the title of the sent embed.
         *
         * @param title The new title to set.
         * @return The updater for chaining calls.
         */
        Updater setTitle(String title);
        
        /**
         * Modifies the description of the sent embed.
         *
         * @param description The new description to set.
         * @return The updater for chaining calls.
         */
        Updater setDescription(String description);
        
        /**
         * Modifies the URL of the sent embed.
         *
         * @param url The new URL to set.
         * @return The updater for chaining calls.
         */
        Updater setUrl(String url);
        
        /**
         * Modifies the timestamp of the sent embed.
         *
         * @param timestamp The new timestamp to set.
         * @return The updater for chaining calls.
         */
        Updater setTimestamp(Instant timestamp);
        
        /**
         * Modifies the color of the sent embed.
         *
         * @param color The new color to set.
         * @return The updater for chaining calls.
         */
        Updater setColor(Color color);
        
        /**
         * Modifies the fields within the embed after the given modifying consumer.
         *
         * @param fieldModifier The modifier for the collection of fields.
         * @return The updater for chaining calls.
         */
        Updater modifyFields(Consumer<Collection<EmbedDraft.EditableField>> fieldModifier);
        
        /**
         * Modifies all fields that match the given predicate.
         * The modifier will only be applied to the fields that test TRUE.
         *
         * @param tester        The predicate to filter fields.
         * @param fieldModifier A consumer to modify the field.
         * @return The updater for chaining calls.
         */
        Updater modifyFields(Predicate<EmbedDraft.Field> tester, Consumer<EmbedDraft.EditableField> fieldModifier);
        
        /**
         * Applies made changes to the embed.
         *
         * @return A future that completes with the new, updated embed when the edit was made.
         * @see Message#edit(EmbedDraft)
         */
        CompletableFuture<SentEmbed> update();
    }
    
    /**
     * This interface represents a SentEmbed's footer.
     */
    interface Footer {
        /**
         * Gets the footer text.
         *
         * @return The footer text.
         */
        String getText();
        
        /**
         * Gets the URL to the icon image of the footer.
         *
         * @return The URL to the footer icon.
         */
        Optional<URL> getIconUrl();
        
        /**
         * This method converts this SentEmbed's footer into an EmbedDraft's footer, which then can be attached to an
         * {@link Embed.Builder}.
         *
         * @return The converted version of the footer.
         */
        default EmbedDraft.Footer toDraft() {
            return EmbedDraft.Footer.BUILD(getText(),
                                           getIconUrl().map(URL::toExternalForm)
                                                   .orElse(null));
        }
    }
    
    /**
     * This interface represents a SentEmbed's Image.
     */
    interface Image {
        /**
         * Gets the URL to the image.
         *
         * @return The URL to the image.
         */
        Optional<URL> getUrl();
        
        /**
         * This method converts this SentEmbed's author object to its EmbedDraft counterpart, which can be then attached
         * to an {@link Embed.Builder}.
         *
         * @return The converted image object.
         */
        default EmbedDraft.Image toDraft() {
            return EmbedDraft.Image.BUILD(getUrl().map(URL::toExternalForm)
                                                  .orElse(null));
        }
    }
    
    /**
     * This interface represents a sent embed's author.
     */
    interface Author extends Nameable {
        /**
         * Gets the URL the author is pointing to.
         *
         * @return The URL of the author.
         */
        Optional<URL> getUrl();
        
        /**
         * This method gets the URL of the author's icon.
         *
         * @return The URL to the icon image.
         */
        Optional<URL> getIconUrl();
        
        /**
         * This method converts this SentEmbed's author object into the embedDraft counterpart, which can be then
         * attached to an {@link Embed.Builder}.
         *
         * @return The converted author object.
         */
        default EmbedDraft.Author toDraft() {
            return EmbedDraft.Author.BUILD(getName(),
                                           getUrl().map(URL::toExternalForm)
                                                   .orElse(null),
                                           getIconUrl().map(URL::toExternalForm)
                                                   .orElse(null));
        }
    }
    
    /**
     * This innterface represents a sent embed's thumbnail.
     */
    interface Thumbnail {
        /**
         * Gets the URL of the thumbnail image.
         *
         * @return The URL of the thumbnail image.
         */
        Optional<URL> getUrl();
        
        /**
         * This method converts the SentEmbed's thumbnail object into it's EmbedDraft counterpart, which then can be
         * added to an {@link Embed.Builder}.
         *
         * @return The converted thumbnail.
         */
        default EmbedDraft.Thumbnail toDraft() {
            return EmbedDraft.Thumbnail.BUILD(getUrl().map(URL::toExternalForm)
                                                      .orElse(null));
        }
    }
    
    /**
     * This interface represents a sent embed's field.
     */
    interface Field {
        /**
         * Gets the title value of the embed field.
         *
         * @return The title of the field.
         */
        String getTitle();
        
        /**
         * Gets the text value of the embed field.
         *
         * @return The text of the field.
         */
        String getText();
        
        /**
         * Gets whether the field is marked as inline.
         *
         * @return Whether the field is inline.
         */
        boolean isInline();
        
        /**
         * Converts this SentEmbed's field into a {@link EmbedDraft.Field} that then can be attached to an {@link
         * Embed.Builder}.
         *
         * @return The converted field.
         */
        default EmbedDraft.Field toDraft() {
            return EmbedDraft.Field.BUILD(getTitle(), getText(), isInline());
        }
    }
    
    /**
     * This interface represents a video that was embedded in the embed. Currently videos can only be embedded by
     * discord themselves; e.g. when sending a YouTube video link.
     */
    interface Video {
        /**
         * Gets the URL of the embedded video.
         *
         * @return The URL of the video.
         */
        Optional<URL> getUrl();
    }
    
    /**
     * This interface represents an embed's provider.
     */
    interface Provider {
        /**
         * Gets the name of the embed's provider.
         *
         * @return The name of the provider.
         */
        String getName();
        
        /**
         * Gets the URL of the embed's provider.
         *
         * @return The url of the provider.
         */
        Optional<URL> getUrl();
    }
}
