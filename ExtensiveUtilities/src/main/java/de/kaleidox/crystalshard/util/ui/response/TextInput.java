package de.kaleidox.crystalshard.util.ui.response;

import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.message.MessageReciever;
import de.kaleidox.crystalshard.api.entity.message.embed.Embed;
import de.kaleidox.crystalshard.api.entity.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.util.ui.DialogueEndpoint;
import de.kaleidox.util.markers.NamedItem;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This class represents a Simple String input.
 */
@SuppressWarnings({"FieldCanBeLocal", "unused", "WeakerAccess", "UnusedReturnValue", "ConstantConditions"})
public class TextInput extends ResponseElement<String> {
    // Default Settings
    private Style style = Style.FULL;
    private EmbedDraft.Field field;

    /**
     * Creates a new Text input object.
     *
     * @param name              The name of the current response value. Will be stored with the value for
     *                          {@link DialogueEndpoint}
     * @param parent            The Messageable the input should be sent to.
     * @param embedBaseSupplier A @Nullable Supplier to provide a basic embed structure.
     * @param userCanRespond    A @Nullable Predicate to check whether a user may Respond to the question.
     */
    public TextInput(String name, MessageReciever parent, Supplier<Embed.Builder> embedBaseSupplier, Predicate<User> userCanRespond) {
        super(name, parent, embedBaseSupplier, userCanRespond);
    }

    // Override Methods
    @Override
    public CompletableFuture<NamedItem<String>> build() {
        CompletableFuture<NamedItem<String>> future = new CompletableFuture<>();
        Embed.Builder embed = embedBaseSupplier.get();
        embed.setDescription("Please type in your response:")
                .setTimestampNow();
        if (field != null) embed.addField(field);

        parent.sendMessage(embed.build())
                .thenAcceptAsync(message -> {
                    affiliateMessages.add(message);
                    message.getChannel()
                            .attachMessageCreateListener(event -> {
                                affiliateMessages.add(event.getMessage());
                                Message msg = event.getMessage();
                                User user = msg.getAuthorAsUser()
                                        .get();

                                if (!user.isYourself() && userCanRespond.test(user)) {
                                    future.complete(new NamedItem<>(name, (style == Style.READABLE ? msg.getReadableContent() : msg.getContent())));
                                }
                            });
                    future.thenAcceptAsync(result -> {
                        if (result != null) {
                            message.edit((field != null ? embedBaseSupplier.get()
                                    .addField(field)
                                    .addField("Answer:", result.getItem()) :
                                    embedBaseSupplier.get()
                                            .addField("Answer:", result.getItem())).build());
                        } else {
                            message.edit(embedBaseSupplier.get()
                                    .addField(field)
                                    .addField("Nobody typed anything.", "There was no valid answer.")
                                    .build());
                        }
                    })
                            .exceptionally(Logger::handle);
                    parent.getDiscord()
                            .getThreadPool()
                            .getScheduler()
                            .schedule(() -> future.complete(null), duration, timeUnit);
                    if (deleteLater) future.thenRunAsync(() -> affiliateMessages.forEach(Message::delete))
                            .exceptionally(Logger::handle);
                });

        return future;
    }

    /**
     * Sets a question field to the embeds structure.
     *
     * @param title The title of the question.
     * @param text  The text of the question.
     * @return The Instance for chaining methods.
     */
    public TextInput setQuestion(String title, String text) {
        field = EmbedDraft.Field.BUILD(title, text, false);
        return this;
    }

    /**
     * Sets the text Style the response should be in. Default style is {@code Style.FULL}.
     *
     * @param style The style to respond with.
     * @return The Instance for chaining methods.
     * @see Style
     */
    public TextInput setStyle(Style style) {
        this.style = style;
        return this;
    }

    /**
     * This enum represents different text styles available from a Message.
     */
    public enum Style {
        /**
         * Replace full mention Tags with their "fancy" alternatives.
         *
         * @see Message#getReadableContent()
         */
        READABLE,

        /**
         * The plain recieved message text.
         *
         * @see Message#getContent()
         */
        FULL
    }
}
