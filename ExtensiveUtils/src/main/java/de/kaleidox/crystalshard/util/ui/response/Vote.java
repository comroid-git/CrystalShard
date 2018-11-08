package de.kaleidox.crystalshard.util.ui.response;

import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.handling.event.message.reaction.ReactionAddEvent;
import de.kaleidox.crystalshard.main.handling.event.message.reaction.ReactionRemoveEvent;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionAddListener;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionRemoveListener;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;
import de.kaleidox.crystalshard.main.items.server.emoji.UnicodeEmoji;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.util.ui.DialogueEndpoint;
import de.kaleidox.util.objects.markers.NamedItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This class represents a Vote for any Discord Messageable.
 *
 * @param <ResultType> The Type that is voted for. You will later get a CompletableFuture of this type, that will contain the voted object.
 */
@SuppressWarnings({"unused", "ConstantConditions", "WeakerAccess"})
public class Vote<ResultType> extends ResponseElement<ResultType> {
    private final ArrayList<Option> optionsOrdered;
    private final HashMap<Option, Integer> rankingMap;

    /**
     * Creates a new Vote.
     *
     * @param name              The name of the current response value. Will be stored with the value for
     *                          {@link DialogueEndpoint}
     * @param parent            The Messageable to send the vote to.
     * @param embedBaseSupplier A @Nullable Supplier to provide a basic embed, must not contain any fields.
     * @param userCanRespond    A @Nullable Predicate to check if a user is allowed to respond; if null; all are accepted.
     */
    public Vote(String name, MessageReciever parent, Supplier<Embed.Builder> embedBaseSupplier, Predicate<User> userCanRespond) {
        super(name, parent, embedBaseSupplier, userCanRespond);

        this.optionsOrdered = new ArrayList<>();
        this.rankingMap = new HashMap<>();
    }

    // Override Methods
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
                        message.attachListener((ReactionAddListener) this::reactionAdd);
                        message.attachListener((ReactionRemoveListener) this::reactionRemove);
                        parent.getDiscord()
                                .getThreadPool()
                                .getScheduler()
                                .schedule(() -> {
                                    Optional<ResultType> representationOptional =
                                            rankingMap.entrySet()
                                                    .stream()
                                                    .max(Comparator.comparingInt(Entry::getValue))
                                                    .map(Entry::getKey)
                                                    .map(Option::getValue);
                                    if (representationOptional.isPresent()) {
                                        future.complete(new NamedItem<>(name, representationOptional.get()));
                                    } else {
                                        future.cancel(true);
                                    }
                                    message.removeAllReactions();
                                    Embed.Builder resultEmbed = embedBaseSupplier.get();
                                    message.edit(populateResultEmbed(resultEmbed).build());
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
     * Adds a new option to the Vote.
     *
     * @param emoji          The representative emoji of the Option.
     * @param description    A short description for the Option.
     * @param representation The representative value for this option. In this implementation, the class of {@code ResultType representation} needs to either
     *                       override the method {@link Object#toString()} or be an {@link Enum}.
     * @return The instance of the Vote for chaining methods.
     * @throws RuntimeException    When the representation neither does override {@link Object#toString()}; nor is an {@link Enum}.
     * @throws ArrayStoreException If there already is an option with the specified emoji.
     * @throws RuntimeException    If there already are 25 Options.
     */
    public Vote<ResultType> addOption(String emoji, String description, ResultType representation) {
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
     * Adds a new option to the Vote.
     *
     * @param emoji          The representative emoji of the Option.
     * @param name           The name of the Option.
     * @param description    A short description for the Option.
     * @param representation The representative value for this option.
     * @return The instance of the Vote for chaining methods.
     * @throws RuntimeException    If there already are 25 Options.
     * @throws ArrayStoreException If there already is an option with the specified emoji.
     */
    public Vote<ResultType> addOption(String emoji, String name, String description, ResultType representation) {
        return addOption(new Option(UnicodeEmoji.of(parent.getDiscord(), emoji), name, description, representation));
    }

    /**
     * Adds a new option to the Vote.
     *
     * @param option The option to add.
     * @return The instance of the Vote for chaining methods.
     * @throws RuntimeException    If there already are 25 Options.
     * @throws ArrayStoreException If there already is an option with the specified emoji.
     */
    public Vote<ResultType> addOption(Option option) {
        if (optionsOrdered.stream()
                .anyMatch(optionS -> optionS.getEmoji()
                        .equals(option.getEmoji()))) {
            throw new ArrayStoreException("Option Emojis can not duplicate!");
        } else if (optionsOrdered.size() == 25) {
            throw new RuntimeException("Only 25 options are allowed.");
        } else {
            optionsOrdered.add(option);
            rankingMap.put(option, 0);
        }

        return this;
    }

    private Embed.Builder populateResultEmbed(Embed.Builder embed) {
        rankingMap.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(optionIntegerEntry -> optionIntegerEntry.getValue() * -1))
                .forEachOrdered(entry -> {
                    Option option = entry.getKey();
                    embed.setDescription("Results are:");
                    embed.addField(option.getEmoji() + " -> " + entry.getValue() + " Votes:", "**" + option.getName() + ":**\n" + option.getDescription());
                });

        return embed;
    }

    @SuppressWarnings("unchecked")
    private void reactionAdd(ReactionAddEvent event) {
        affiliateMessages.add(event.getMessage());
        User user = event.getUser();
        Emoji emoji = event.getEmoji();

        if (!user.isYourself()) {
            if (userCanRespond.test(user)) {
                optionsOrdered.stream()
                        .filter(option -> emoji.equals(option.getEmoji()))
                        .findAny()
                        .ifPresent(option -> rankingMap.put(option,
                                rankingMap.getOrDefault(
                                        option,
                                        0) + 1));
            } else {
                event.getMessage()
                        .removeReactionsByEmoji(emoji);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void reactionRemove(ReactionRemoveEvent event) {
        affiliateMessages.add(event.getMessage());
        User user = event.getUser();
        Emoji emoji = event.getEmoji();

        if (!user.isYourself()) {
            if (userCanRespond.test(user)) {
                optionsOrdered.stream()
                        .filter(option -> emoji.equals(option.getEmoji()))
                        .findAny()
                        .ifPresent(option -> rankingMap.put(option,
                                rankingMap.getOrDefault(
                                        option,
                                        1) - 1));
            } else {
                event.getMessage()
                        .removeReactionsByEmoji(emoji);
            }
        }
    }

    /**
     * This subclass represents a Voting Option.
     */
    public class Option {
        private final Emoji emoji;
        private final String description;
        private final ResultType value;
        private final String name;

        /**
         * Builds a new Option. Can be used in {@code Vote#addOption(Option)}.
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
