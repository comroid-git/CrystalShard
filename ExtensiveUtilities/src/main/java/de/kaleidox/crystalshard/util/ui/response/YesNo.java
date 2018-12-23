package de.kaleidox.crystalshard.util.ui.response;

import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionAddListener;
import de.kaleidox.crystalshard.main.items.Mentionable;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.util.ui.DialogueEndpoint;
import de.kaleidox.util.markers.NamedItem;

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
    private EmbedDraft.Field field;

    /**
     * Creates a new Yes-No Question.
     *
     * @param name              The name of the current response value. Will be stored with the value for
     *                          {@link DialogueEndpoint}
     * @param parent            The messageable the YesNo Question should be sent to.
     * @param embedBaseSupplier A @Nullable Supplier to provide a basic embed structure.
     * @param userCanRespond    A @Nullable Predicate to check whether a user may Respond to the question.
     */
    public YesNo(String name, MessageReciever parent, Supplier<Embed.Builder> embedBaseSupplier, Predicate<User> userCanRespond) {
        super(name, parent, embedBaseSupplier, userCanRespond);
    }

    // Override Methods
    @Override
    public CompletableFuture<NamedItem<Boolean>> build() {
        Embed.Builder embed = embedBaseSupplier.get();
        embed.setDescription("Yes/No question:")
                .setTimestampNow();
        if (field != null) embed.addField(field);

        CompletableFuture<NamedItem<Boolean>> future = new CompletableFuture<>();
        parent.sendMessage(embed.build())
                .thenAcceptAsync(message -> {
                    affiliateMessages.add(message);
                    message.addReaction(EMOJI_YES);
                    message.addReaction(EMOJI_NO);
                    message.attachListener((ReactionAddListener) event -> {
                        affiliateMessages.add(event.getMessage());
                        Emoji emoji = event.getEmoji();
                        User user = event.getUser();

                        if (!user.isYourself()) {
                            if (userCanRespond.test(user) && emoji.toUnicodeEmoji()
                                    .isPresent()) {
                                //noinspection OptionalGetWithoutIsPresent
                                switch (emoji.toUnicodeEmoji()
                                        .map(Mentionable::getMentionTag)
                                        .get()) {
                                    case EMOJI_YES:
                                        future.complete(new NamedItem<>(super.name, true));
                                        break;
                                    case EMOJI_NO:
                                        future.complete(new NamedItem<>(super.name, false));
                                        break;
                                    default:
                                        event.getMessage()
                                                .removeReactionsByEmoji(emoji);
                                        break;
                                }
                            } else {
                                event.getMessage()
                                        .removeReactionsByEmoji(emoji);
                            }
                        }
                    });
                    parent.getDiscord()
                            .getThreadPool()
                            .getScheduler()
                            .schedule(() -> future.complete(new NamedItem<>(super.name, false)), duration, timeUnit);
                    future.thenAcceptAsync(result -> {
                        message.removeAllReactions();
                        message.edit((field != null ? embedBaseSupplier.get()
                                .addField(field)
                                .addField("Answer:", (result.getItem() ? "Yes" : "No")) :
                                embedBaseSupplier.get()
                                        .addField("Answer:", (result.getItem() ? "Yes" : "No"))).build());
                        message.detachAllListeners();
                    })
                            .exceptionally(Logger::handle);
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
    public YesNo setQuestion(String title, String text) {
        field = EmbedDraft.Field.BUILD(title, text, false);
        return this;
    }
}
