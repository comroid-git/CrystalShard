package de.kaleidox.crystalshard.util.input;

import de.kaleidox.crystalshard.api.entity.message.MessageReciever;
import de.kaleidox.crystalshard.api.entity.message.Embed;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.handling.event.message.reaction.ReactionAddEvent;
import de.kaleidox.crystalshard.api.handling.listener.message.reaction.ReactionAddListener;
import de.kaleidox.util.functional.Evaluation;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BooleanInput extends Base<Boolean> {
    public final static String YES_EMOJI = "✅";
    public final static String NO_EMOJI = "❌";

    protected BooleanInput(MessageReciever parent, String name, Boolean defaultValue) {
        super(parent, name, defaultValue);
    }

    protected BooleanInput(MessageReciever parent, Consumer<Embed.Builder> embedModifier, String name, Boolean defaultValue) {
        super(parent, embedModifier, name, defaultValue);
    }

    protected BooleanInput(MessageReciever parent, Consumer<Embed.Builder> embedModifier, String name, long time, TimeUnit unit, Boolean defaultValue) {
        super(parent, embedModifier, name, time, unit, defaultValue);
    }

    protected BooleanInput(MessageReciever parent, User user, String name, Boolean defaultValue) {
        super(parent, user, name, defaultValue);
    }

    protected BooleanInput(MessageReciever parent, Consumer<Embed.Builder> embedModifier, User user, String name, Boolean defaultValue) {
        super(parent, embedModifier, user, name, defaultValue);
    }

    protected BooleanInput(MessageReciever parent,
                           Consumer<Embed.Builder> embedModifier,
                           User user,
                           String name,
                           long time,
                           TimeUnit unit,
                           Boolean defaultValue) {
        super(parent, embedModifier, user, name, time, unit, defaultValue);
    }

    protected BooleanInput(MessageReciever parent,
                           Consumer<Embed.Builder> embedModifier,
                           Predicate<User> participantTester,
                           String name,
                           long time,
                           TimeUnit unit,
                           Boolean defaultValue,
                           boolean deleteWhenDone) {
        super(parent, embedModifier, participantTester, name, time, unit, defaultValue, deleteWhenDone);
    }

    public CompletableFuture<Evaluation<Boolean>> buildAsEvaluation() {
        return build().thenApply(Evaluation::of);
    }

    @Override
    public CompletableFuture<Boolean> build() {
        CompletableFuture<Boolean> future = createFuture();
        Embed.Builder embed = createEmbed();

        parent.sendMessage(embed.build())
                .exceptionally(logger::exception)
                .thenAcceptAsync(message -> {
                    addAffiliate(message);
                    message.addReaction(YES_EMOJI, NO_EMOJI);
                    message.attachListener(new ReactionAddListener() {
                        @Override
                        public void onReactionAdd(ReactionAddEvent event) {
                            if (!event.getUser()
                                    .isYourself() && participantTester.test(event.getUser())) {
                                switch (event.getEmoji()
                                        .toDiscordPrintable()) {
                                    case YES_EMOJI:
                                        future.complete(true);
                                        message.detachListener(this)
                                                .onFailure(bFalse -> logger.error("Error detaching listener: " + this));
                                        cleanup();
                                        break;
                                    case NO_EMOJI:
                                        future.complete(false);
                                        message.detachListener(this)
                                                .onFailure(bFalse -> logger.error("Error detaching listener: " + this));
                                        cleanup();
                                        break;
                                    default:
                                        event.getReaction()
                                                .remove();
                                        break;
                                }
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
