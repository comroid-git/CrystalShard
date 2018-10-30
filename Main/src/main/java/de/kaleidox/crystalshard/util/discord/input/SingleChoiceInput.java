package de.kaleidox.crystalshard.util.discord.input;

import de.kaleidox.crystalshard.main.handling.event.message.reaction.ReactionAddEvent;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionAddListener;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.util.objects.CompletableFutureExtended;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SingleChoiceInput<T> extends Base<T> {
    private List<Choice<T>> choices = new ArrayList<>();

    protected SingleChoiceInput(MessageReciever parent, String name, T defaultValue) {
        super(parent, name, defaultValue);
    }

    protected SingleChoiceInput(MessageReciever parent, Consumer<Embed.Builder> embedModifier, String name, T defaultValue) {
        super(parent, embedModifier, name, defaultValue);
    }

    protected SingleChoiceInput(MessageReciever parent, Consumer<Embed.Builder> embedModifier, String name, long time, TimeUnit unit, T defaultValue) {
        super(parent, embedModifier, name, time, unit, defaultValue);
    }

    protected SingleChoiceInput(MessageReciever parent, User user, String name, T defaultValue) {
        super(parent, user, name, defaultValue);
    }

    protected SingleChoiceInput(MessageReciever parent, Consumer<Embed.Builder> embedModifier, User user, String name, T defaultValue) {
        super(parent, embedModifier, user, name, defaultValue);
    }

    protected SingleChoiceInput(MessageReciever parent,
                                Consumer<Embed.Builder> embedModifier,
                                User user,
                                String name,
                                long time,
                                TimeUnit unit,
                                T defaultValue) {
        super(parent, embedModifier, user, name, time, unit, defaultValue);
    }

    protected SingleChoiceInput(MessageReciever parent,
                                Consumer<Embed.Builder> embedModifier,
                                Predicate<User> participantTester,
                                String name,
                                long time,
                                TimeUnit unit,
                                T defaultValue,
                                boolean deleteWhenDone) {
        super(parent, embedModifier, participantTester, name, time, unit, defaultValue, deleteWhenDone);
    }

    public SingleChoiceInput<T> addChoice(String emojiRepresentation, String name, String description, T value) {
        choices.add(new Choice<>(emojiRepresentation, name, description, value));
        return this;
    }

    @Override
    public CompletableFuture<T> build() {
        CompletableFutureExtended<T> future = createFuture();
        Embed.Builder embed = createEmbed();
        if (embed.getFields()
                .size() + choices.size() > Embed.Boundaries.FIELD_COUNT)
            throw new IllegalArgumentException("Too many fields in the embed!");
        for (Choice<T> choice : choices)
            embed.addField(choice.getEmojiRepresentation() + " -> " + choice.getName(), choice.getDescription());

        parent.sendMessage(embed.build())
                .exceptionally(logger::exception)
                .thenAcceptAsync(message -> {
                    addAffiliate(message);
                    for (Choice<T> choice : choices) message.addReaction(choice.getEmojiRepresentation());
                    message.attachListener(new ReactionAddListener() {
                        @Override
                        public void onReactionAdd(ReactionAddEvent event) {
                            if (!event.getUser()
                                    .isYourself() &&
                                    participantTester.test(event.getUser()) &&
                                    choices.stream()
                                            .map(Choice::getEmojiRepresentation)
                                            .collect(Collectors.toSet())
                                            .contains(event.getEmoji()
                                                    .toDiscordPrintable())) {
                                Optional<Choice<T>> any = choices.stream()
                                        .filter(choice -> choice.getEmojiRepresentation()
                                                .equals(event.getEmoji()
                                                        .toDiscordPrintable()))
                                        .findAny();
                                if (!any.isPresent()) {
                                    event.getReaction()
                                            .remove();
                                    assert false : "This point should never be reached.";
                                }
                                any.ifPresent(choice -> {
                                    future.complete(choice.getValue());
                                    message.detachListener(this);
                                    cleanup();
                                });
                            } else event.getReaction()
                                    .remove();
                        }
                    })
                            .detachIn(time, unit)
                            .onDetach(() -> {
                                if (!future.isDone()) future.complete(defaultValue);
                            });
                });

        return future;
    }
}
