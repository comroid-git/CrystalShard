package de.kaleidox.crystalshard.util.discord.ui.response;

import de.kaleidox.util.Utils;
import de.kaleidox.util.interfaces.Subclass;
import de.kaleidox.util.listeners.MessageListeners;
import de.kaleidox.util.objects.NamedItem;

import javax.annotation.Nullable;
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
 * @param <ResultType> The Type that is voted for. You will later get a
 *                     CompletableFuture of this type, that will contain the voted object.
 */
@SuppressWarnings({"unused", "ConstantConditions", "WeakerAccess"})
public class Vote<ResultType> extends ResponseElement<ResultType> {
    private final ArrayList<Option> optionsOrdered;
    private final HashMap<Option, Integer> rankingMap;

    /**
     * Creates a new Vote.
     *
     * @param name              The name of the current response value. Will be stored with the value for {@link de.kaleidox.crystalshard.util.discord.ui.DialogueEndpoint}
     * @param parent            The Messageable to send the vote to.
     * @param embedBaseSupplier A @Nullable Supplier to provide a basic embed, must not contain any fields.
     * @param userCanRespond    A @Nullable Predicate to check if a user is allowed to respond; if null; all are accepted.
     */
    public Vote(
            String name,
            Messageable parent,
            @Nullable Supplier<EmbedBuilder> embedBaseSupplier,
            @Nullable Predicate<User> userCanRespond) {
        super(name, parent, embedBaseSupplier, userCanRespond);

        this.optionsOrdered = new ArrayList<>();
        this.rankingMap = new HashMap<>();
    }

    /**
     * Adds a new option to the Vote.
     *
     * @param emoji          The representative emoji of the Option.
     * @param description    A short description for the Option.
     * @param representation The representative value for this option.
     *                       In this implementation, the class of {@code ResultType representation} needs
     *                       to either override the method {@link Object#toString()} or be an {@link Enum}.
     * @return The instance of the Vote for chaining methods.
     * @throws RuntimeException    When the representation neither does override {@link Object#toString()}; nor is an {@link Enum}.
     * @throws ArrayStoreException If there already is an option with the specified emoji.
     * @throws RuntimeException    If there already are 25 Options.
     */
    public Vote<ResultType> addOption(String emoji, String description, ResultType representation) {
        try {
            if (representation.getClass() == Enum.class ||
                    representation.getClass().getMethod("toString").getDeclaringClass() == representation.getClass()) {
                return addOption(emoji, representation.toString(), description, representation);
            } else {
                throw new RuntimeException("The Representation [" + representation + "] has to manually override " +
                        "the method \"toString()\"; or you have to use the implementation of \"addOption(String, " +
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
        return addOption(new Option(emoji, name, description, representation));
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
                .anyMatch(optionS -> optionS
                        .getEmoji()
                        .equalsIgnoreCase(option.getEmoji()))) {
            throw new ArrayStoreException("Option Emojis can not duplicate!");
        } else if (optionsOrdered.size() == 25) {
            throw new RuntimeException("Only 25 options are allowed.");
        } else {
            optionsOrdered.add(option);
            rankingMap.put(option, 0);
        }

        return this;
    }

    @Override
    public CompletableFuture<NamedItem<ResultType>> build() {
        if (optionsOrdered.isEmpty()) {
            throw new NullPointerException("No options registered!");
        } else {
            EmbedBuilder embed = embedBaseSupplier.get();
            embed.setDescription("Voting will continue for " + duration + " " + timeUnit.name().toLowerCase() +
                    ", beginning from the timestamp.")
                    .setTimestampToNow();
            optionsOrdered.forEach(option -> embed.addField(
                    option.getEmoji() + " -> " + option.getName(),
                    option.getDescription()
            ));

            // send the message, but separately save the future for async listener registration
            CompletableFuture<NamedItem<ResultType>> future = new CompletableFuture<>();
            parent.sendMessage(embed).thenAcceptAsync(message -> {
                affiliateMessages.add(message);
                optionsOrdered.forEach(option -> message.addReaction(option.getEmoji()));
                message.addReactionAddListener(this::reactionAdd);
                message.addReactionRemoveListener(this::reactionRemove);
                message.addMessageDeleteListener(MessageListeners::deleteCleanup)
                        .removeAfter(this.duration, this.timeUnit)
                        .addRemoveHandler(() -> {
                            Optional<ResultType> representationOptional = rankingMap
                                    .entrySet()
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
                            EmbedBuilder resultEmbed = embedBaseSupplier.get();
                            message.edit(populateResultEmbed(resultEmbed));
                            message.getMessageAttachableListeners()
                                    .forEach((key, value) -> message.removeMessageAttachableListener(key));
                        });
                if (deleteLater)
                    future.thenRunAsync(() -> affiliateMessages.forEach(Message::delete)).exceptionally(ExceptionLogger.get());
            }).exceptionally(ExceptionLogger.get());

            return future;
        }
    }

    private EmbedBuilder populateResultEmbed(EmbedBuilder embed) {
        rankingMap.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(optionIntegerEntry -> optionIntegerEntry.getValue() * -1))
                .forEachOrdered(entry -> {
                    Option option = entry.getKey();
                    embed.setDescription("Results are:");
                    embed.addField(
                            option.getEmoji() + " -> " + entry.getValue() + " Votes:",
                            "**" + option.getName() + ":**\n" + option.getDescription()
                    );
                });

        return embed;
    }

    @SuppressWarnings("unchecked")
    private void reactionAdd(ReactionAddEvent event) {
        event.requestMessage().thenAcceptAsync(affiliateMessages::add);
        User user = event.getUser();
        Emoji emoji = event.getEmoji();

        if (!user.isYourself()) {
            if (userCanRespond.test(user)) {
                optionsOrdered.stream()
                        .filter(option -> Utils.compareAnyEmoji(emoji, option.getEmoji()))
                        .findAny()
                        .ifPresent(
                                option -> rankingMap.put(
                                        option,
                                        rankingMap.getOrDefault(option, 0) + 1
                                )
                        );
            } else {
                event.requestMessage()
                        .thenAcceptAsync(message -> message.removeReactionByEmoji(
                                user,
                                emoji
                        ));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void reactionRemove(ReactionRemoveEvent event) {
        event.requestMessage().thenAcceptAsync(affiliateMessages::add);
        User user = event.getUser();
        Emoji emoji = event.getEmoji();

        if (!user.isYourself()) {
            if (userCanRespond.test(user)) {
                optionsOrdered.stream()
                        .filter(option -> Utils.compareAnyEmoji(emoji, option.getEmoji()))
                        .findAny()
                        .ifPresent(
                                option -> rankingMap.put(
                                        option,
                                        rankingMap.getOrDefault(option, 1) - 1
                                )
                        );
            } else {
                event.requestMessage()
                        .thenAcceptAsync(message -> message.removeReactionByEmoji(
                                user,
                                emoji
                        ));
            }
        }
    }

    /**
     * This subclass represents a Voting Option.
     */
    public class Option implements Subclass {
        private final String emoji;
        private final String description;
        private final ResultType value;
        private final String name;

        /**
         * Builds a new Option.
         * Can be used in {@code Vote#addOption(Option)}.
         *
         * @param emoji       The representative emoji.
         * @param name        The name of the Option.
         * @param description A short description for the Option.
         * @param value       The representative Value of the Option.
         */
        public Option(
                String emoji,
                String name,
                String description,
                ResultType value) {
            this.emoji = emoji;
            this.name = name;
            this.description = description;
            this.value = value;
        }

        public String getEmoji() {
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

        @Override
        public String toString() {
            return "[" + emoji + "|" + name + "] with description [" + description + "]";
        }
    }
}
