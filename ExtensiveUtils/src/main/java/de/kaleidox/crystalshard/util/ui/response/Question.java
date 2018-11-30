package de.kaleidox.crystalshard.util.ui.response;

import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionAddListener;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;
import de.kaleidox.crystalshard.main.items.server.emoji.UnicodeEmoji;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.util.ui.DialogueEndpoint;
import de.kaleidox.util.objects.markers.NamedItem;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This class represents a Question that can only get one response.
 *
 * @param <ResultType> The Type that is asked for. You will later get a CompletableFuture of this type, that will contain the response object.
 */
@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
public class Question<ResultType> extends ResponseElement<ResultType> {
    private final ArrayList<Option> optionsOrdered;

    /**
     * Creates a new Question.
     *
     * @param name              The name of the current response value. Will be stored with the value for
     *                          {@link DialogueEndpoint}
     * @param parent            The Messageable that the question should be sent in.
     * @param embedBaseSupplier A @Nullable Supplier to provide a basic embed, must not contain any fields.
     * @param userCanRespond    A @Nullable Predicate to check if a user is allowed to respond; if null; all are accepted.
     */
    public Question(String name, MessageReciever parent, Supplier<Embed.Builder> embedBaseSupplier, Predicate<User> userCanRespond) {
        super(name, parent, embedBaseSupplier, userCanRespond);

        this.optionsOrdered = new ArrayList<>();
    }

    // Override Methods
    @SuppressWarnings("unchecked")
    @Override
    public CompletableFuture<NamedItem<ResultType>> build() {
        if (optionsOrdered.isEmpty()) {
            throw new NullPointerException("No options registered!");
        } else {
            Embed.Builder embed = embedBaseSupplier.get();
            embed.setDescription("Voting will continue for " + duration + " " + timeUnit.name()
                    .toLowerCase() + ", beginning from the timestamp.")
                    .setTimestampNow();
            optionsOrdered.forEach(option -> embed.addField(option.getEmoji() + " -> " + option.getName(), option.getDescription()));

            // send the message, but separately save the future for async listener registration
            CompletableFuture<NamedItem<ResultType>> future = new CompletableFuture<>();
            parent.sendMessage(embed.build())
                    .thenAcceptAsync(message -> {
                        affiliateMessages.add(message);
                        optionsOrdered.forEach(option -> message.addReaction(option.getEmoji()));
                        message.attachListener((ReactionAddListener) event -> {
                            affiliateMessages.add(event.getMessage());
                            Emoji emoji = event.getEmoji();
                            User user = event.getUser();

                            if (!user.isYourself() && userCanRespond.test(user)) {
                                Optional<Option> any = optionsOrdered.stream()
                                        .filter(option -> option.emoji.equals(emoji))
                                        .findAny();
                                if (any.isPresent()) {
                                    future.complete(new NamedItem<>(name, any.get()
                                            .getValue()));
                                } else {
                                    future.cancel(true);
                                }
                            }
                        });
                        parent.getDiscord()
                                .getThreadPool()
                                .getScheduler()
                                .schedule(() -> {
                                    message.removeAllReactions();
                                    message.detachAllListeners();
                                }, duration, timeUnit);
                        if (deleteLater) future.thenRunAsync(() -> affiliateMessages.forEach(Message::delete))
                                .exceptionally(Logger::handle);
                    })
                    .exceptionally(Logger::handle);
            return future;
        }
    }

    /**
     * Adds a new option to the Question.
     *
     * @param emoji          The representative emoji of the Option.
     * @param description    A short description for the Option.
     * @param representation The representative value for this option. In this implementation, the class of {@code ResultType representation} needs to either
     *                       override the method {@link Object#toString()} or be an {@link Enum}.
     * @return The instance of the Question for chaining methods.
     * @throws RuntimeException    When the representation neither does override {@link Object#toString()}; nor is an {@link Enum}.
     * @throws ArrayStoreException If there already is an option with the specified emoji.
     * @throws RuntimeException    If there already are 25 Options.
     */
    public Question<ResultType> addOption(String emoji, String description, ResultType representation) {
        try {
            if (representation.getClass() == Enum.class || representation.getClass()
                    .getMethod("toString")
                    .getDeclaringClass() == representation.getClass()) {
                return addOption(emoji, representation.toString(), description, representation);
            } else {
                throw new RuntimeException("The Representation [" + representation + "] has to manually override " +
                        "the method \"toString()\"; or you have to use the implementation of " + "\"addOption(String, " +
                        "String, String, ResultType)\".");
            }
        } catch (NoSuchMethodException ignored) { // this will never occur because everything has "toString"
            throw new AssertionError("Fatal internal error.");
        }
    }

    /**
     * Adds a new option to the Question.
     *
     * @param emoji          The representative emoji of the Option.
     * @param name           The name of the Option.
     * @param description    A short description for the Option.
     * @param representation The representative value for this option.
     * @return The instance of the Question for chaining methods.
     * @throws RuntimeException    If there already are 25 Options.
     * @throws ArrayStoreException If there already is an option with the specified emoji.
     */
    public Question<ResultType> addOption(String emoji, String name, String description, ResultType representation) {
        return addOption(new Option(UnicodeEmoji.of(parent.getDiscord(), emoji), name, description, representation));
    }

    /**
     * Adds a new option to the Question.
     *
     * @param option The option to add.
     * @return The instance of the Question for chaining methods.
     * @throws RuntimeException    If there already are 25 Options.
     * @throws ArrayStoreException If there already is an option with the specified emoji.
     */
    public Question<ResultType> addOption(Option option) {
        if (optionsOrdered.stream()
                .anyMatch(optionS -> optionS.getEmoji()
                        .equals(option.emoji))) {
            throw new ArrayStoreException("Option Emojis can not duplicate!");
        } else if (optionsOrdered.size() == 25) {
            throw new RuntimeException("Only 25 optionsOrdered are allowed.");
        } else {
            optionsOrdered.add(option);
        }

        return this;
    }

    /**
     * This subclass represents a Question Option.
     */
    public class Option {
        private final Emoji emoji;
        private final String description;
        private final ResultType value;
        private final String name;

        /**
         * Builds a new Option. Can be used in {@link Question#addOption(Question.Option)}.
         *
         * @param emoji       The representative emoji.
         * @param name        The name of the Option.
         * @param description A short description for the Option.
         * @param value       The representative Value of the Option.
         */
        public Option(Emoji emoji, String name, String description, ResultType value) {
            this.emoji = emoji;
            this.name = name;
            this.description = description;
            this.value = value;
        }

        // Override Methods
        @Override
        public String toString() {
            return "[" + emoji + "|" + name + "] with description [" + description + "]";
        }

        public Emoji getEmoji() {
            return emoji;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public ResultType getValue() {
            return value;
        }
    }
}
