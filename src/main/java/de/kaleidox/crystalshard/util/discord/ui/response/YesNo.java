package de.kaleidox.crystalshard.util.discord.ui.response;


import de.kaleidox.util.listeners.MessageListeners;
import de.kaleidox.util.objects.NamedItem;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This class represents a Yes-No question.
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "WeakerAccess", "FieldCanBeLocal"})
public class YesNo extends ResponseElement<Boolean> {
    private final static String EMOJI_YES = "✅";
    private final static String EMOJI_NO = "❌";

    private EmbedFieldRepresentative field;

    /**
     * Creates a new Yes-No Question.
     *
     * @param name              The name of the current response value. Will be stored with the value for {@link de.kaleidox.crystalshard.util.discord.ui.DialogueEndpoint}
     * @param parent            The messageable the YesNo Question should be sent to.
     * @param embedBaseSupplier A @Nullable Supplier to provide a basic embed structure.
     * @param userCanRespond    A @Nullable Predicate to check whether a user may Respond to the question.
     */
    public YesNo(
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
    public YesNo setQuestion(String title, String text) {
        field = new EmbedFieldRepresentative(title, text, false);
        return this;
    }

    @Override
    public CompletableFuture<NamedItem<Boolean>> build() {
        EmbedBuilder embed = embedBaseSupplier.get();
        embed.setDescription("Yes/No question:")
                .setTimestampToNow();
        if (field != null) field.fillBuilder(embed);

        CompletableFuture<NamedItem<Boolean>> future = new CompletableFuture<>();
        parent.sendMessage(embed)
                .thenAcceptAsync(message -> {
                    affiliateMessages.add(message);
                    message.addReaction(EMOJI_YES);
                    message.addReaction(EMOJI_NO);
                    message.addReactionAddListener(event -> {
                        event.requestMessage().thenAcceptAsync(affiliateMessages::add);
                        Emoji emoji = event.getEmoji();
                        User user = event.getUser();

                        if (!user.isYourself()) {
                            if (userCanRespond.test(user) && emoji.asUnicodeEmoji().isPresent()) {
                                switch (emoji.asUnicodeEmoji().get()) {
                                    case EMOJI_YES:
                                        future.complete(new NamedItem<>(super.name, true));
                                        break;
                                    case EMOJI_NO:
                                        future.complete(new NamedItem<>(super.name, false));
                                        break;
                                    default:
                                        event.requestMessage()
                                                .thenAcceptAsync(msg -> msg.removeReactionByEmoji(
                                                        user,
                                                        emoji
                                                ));
                                }
                            } else {
                                event.requestMessage()
                                        .thenAcceptAsync(msg -> msg.removeReactionByEmoji(
                                                user,
                                                emoji
                                        ));
                            }
                        }
                    });
                    message.addMessageDeleteListener(MessageListeners::deleteCleanup)
                            .removeAfter(duration, timeUnit)
                            .addRemoveHandler(() -> future.complete(new NamedItem<>(super.name, false)));
                    future.thenAcceptAsync(result -> {
                        message.removeAllReactions();
                        message.edit(field != null ?
                                field.fillBuilder(embedBaseSupplier.get())
                                        .addField("Answer:", (result.getItem() ? "Yes" : "No")) :
                                embedBaseSupplier.get()
                                        .addField("Answer:", (result.getItem() ? "Yes" : "No"))
                        );
                        message.getMessageAttachableListeners().forEach((k, v) -> message.removeMessageAttachableListener(k));
                    }).exceptionally(ExceptionLogger.get());
                    if (deleteLater)
                        future.thenRunAsync(() -> affiliateMessages.forEach(Message::delete)).exceptionally(ExceptionLogger.get());
                });

        return future;
    }
}
