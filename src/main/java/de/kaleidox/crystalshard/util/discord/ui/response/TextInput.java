package de.kaleidox.crystalshard.util.discord.ui.response;

import de.kaleidox.util.listeners.MessageListeners;
import de.kaleidox.util.objects.NamedItem;

import javax.annotation.Nullable;
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

    private EmbedFieldRepresentative field;

    /**
     * Creates a new Text input object.
     *
     * @param name              The name of the current response value. Will be stored with the value for {@link de.kaleidox.crystalshard.util.discord.ui.DialogueEndpoint}
     * @param parent            The Messageable the input should be sent to.
     * @param embedBaseSupplier A @Nullable Supplier to provide a basic embed structure.
     * @param userCanRespond    A @Nullable Predicate to check whether a user may Respond to the question.
     */
    public TextInput(
            String name,
            Messageable parent,
            @Nullable Supplier<EmbedBuilder> embedBaseSupplier,
            @Nullable Predicate<User> userCanRespond) {
        super(name, parent, embedBaseSupplier, userCanRespond);
    }

    /**
     * Sets a question field to the embeds structure.
     *
     * @param title The title of the question.
     * @param text  The text of the question.
     * @return The Instance for chaining methods.
     */
    public TextInput setQuestion(String title, String text) {
        field = new EmbedFieldRepresentative(title, text, false);
        return this;
    }

    /**
     * Sets the text Style the response should be in.
     * Default style is {@code Style.FULL}.
     *
     * @param style The style to respond with.
     * @return The Instance for chaining methods.
     * @see Style
     */
    public TextInput setStyle(Style style) {
        this.style = style;
        return this;
    }

    @Override
    public CompletableFuture<NamedItem<String>> build() {
        CompletableFuture<NamedItem<String>> future = new CompletableFuture<>();
        EmbedBuilder embed = embedBaseSupplier.get();
        embed.setDescription("Please type in your response:")
                .setTimestampToNow();
        if (field != null) field.fillBuilder(embed);

        parent.sendMessage(embed)
                .thenAcceptAsync(message -> {
                    affiliateMessages.add(message);
                    message.getChannel()
                            .addMessageCreateListener(event -> {
                                affiliateMessages.add(event.getMessage());
                                Message msg = event.getMessage();
                                User user = msg.getUserAuthor().get();

                                if (!user.isYourself() && userCanRespond.test(user)) {
                                    future.complete(new NamedItem<>(
                                            name,
                                            (style == Style.READABLE ? msg.getReadableContent() : msg.getContent())
                                    ));
                                }
                            });
                    future.thenAcceptAsync(result -> {
                        if (result != null) {
                            message.edit(field != null ?
                                    field.fillBuilder(embedBaseSupplier.get())
                                            .addField("Answer:", result.getItem()) :
                                    embedBaseSupplier.get()
                                            .addField("Answer:", result.getItem())
                            );
                        } else {
                            message.edit(
                                    field.fillBuilder(embedBaseSupplier.get())
                                            .addField("Nobody typed anything.", "There was no valid answer.")
                            );
                        }
                    }).exceptionally(ExceptionLogger.get());
                    message.addMessageDeleteListener(MessageListeners::deleteCleanup)
                            .removeAfter(duration, timeUnit)
                            .addRemoveHandler(() -> {
                                future.complete(null);
                                message.getMessageAttachableListeners().forEach((k, v) -> message.removeMessageAttachableListener(k));
                            });
                    if (deleteLater)
                        future.thenRunAsync(() -> affiliateMessages.forEach(Message::delete)).exceptionally(ExceptionLogger.get());
                });

        return future;
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
