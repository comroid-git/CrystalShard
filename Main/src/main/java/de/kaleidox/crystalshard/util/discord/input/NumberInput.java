package de.kaleidox.crystalshard.util.discord.input;

import de.kaleidox.crystalshard.main.handling.event.message.generic.MessageCreateEvent;
import de.kaleidox.crystalshard.main.handling.listener.message.generic.MessageCreateListener;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.util.discord.util.InfoReaction;
import de.kaleidox.crystalshard.util.helpers.StringHelper;

import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class NumberInput extends Base<Long> {
    private final long min;
    private final long max;

    protected NumberInput(long min, long max, MessageReciever parent, String name, Long defaultValue) {
        super(parent, name, defaultValue);
        this.min = min;
        this.max = max;
    }

    protected NumberInput(long min, long max, MessageReciever parent, Consumer<Embed.Builder> embedModifier, String name, Long defaultValue) {
        super(parent, embedModifier, name, defaultValue);
        this.min = min;
        this.max = max;
    }

    protected NumberInput(long min,
                          long max,
                          MessageReciever parent,
                          Consumer<Embed.Builder> embedModifier,
                          String name,
                          long time,
                          TimeUnit unit,
                          Long defaultValue) {
        super(parent, embedModifier, name, time, unit, defaultValue);
        this.min = min;
        this.max = max;
    }

    protected NumberInput(long min, long max, MessageReciever parent, User user, String name, Long defaultValue) {
        super(parent, user, name, defaultValue);
        this.min = min;
        this.max = max;
    }

    protected NumberInput(long min, long max, MessageReciever parent, Consumer<Embed.Builder> embedModifier, User user, String name, Long defaultValue) {
        super(parent, embedModifier, user, name, defaultValue);
        this.min = min;
        this.max = max;
    }

    protected NumberInput(long min,
                          long max,
                          MessageReciever parent,
                          Consumer<Embed.Builder> embedModifier,
                          User user,
                          String name,
                          long time,
                          TimeUnit unit,
                          Long defaultValue) {
        super(parent, embedModifier, user, name, time, unit, defaultValue);
        this.min = min;
        this.max = max;
    }

    protected NumberInput(long min,
                          long max,
                          MessageReciever parent,
                          Consumer<Embed.Builder> embedModifier,
                          Predicate<User> participantTester,
                          String name,
                          long time,
                          TimeUnit unit,
                          Long defaultValue,
                          boolean deleteWhenDone) {
        super(parent, embedModifier, participantTester, name, time, unit, defaultValue, deleteWhenDone);
        this.min = min;
        this.max = max;
    }

    @Override
    public CompletableFuture<Long> build() {
        CompletableFuture<Long> future = createFuture();
        Embed.Builder embed = createEmbed();

        parent.sendMessage(embed.build())
                .exceptionally(logger::exception)
                .thenAcceptAsync(message -> {
                    addAffiliate(message);
                    message.getChannel()
                            .attachListener(new MessageCreateListener() {
                                @Override
                                public void onMessageCreate(MessageCreateEvent event) {
                                    event.getMessageAuthorUser()
                                            .filter(participantTester::test)
                                            .filter(usr -> !usr.isYourself())
                                            .ifPresent(author -> {
                                                if (StringHelper.isNumeric(event.getMessageContent())) {
                                                    long val = Long.valueOf(event.getMessageContent());
                                                    if (val >= min && val <= max) {
                                                        future.complete(val);
                                                        message.getChannel()
                                                                .detachListener(this)
                                                                .onFailure(bFalse -> logger.error("Error detaching listener: " + this));
                                                        cleanup();
                                                    } else {
                                                        Embed.Builder builder = Embed.BUILDER();
                                                        builder.setColor(Color.RED)
                                                                .setTitle("Error")
                                                                .addField("Error with the number!",
                                                                        "The given number ```" + val + "``` is too " +
                                                                                (val < min ? "small" : "big") + "!\n\nIt must be at least `" + min +
                                                                                "` and at last `" + max + "`!");
                                                        InfoReaction.add(event.getMessage(), builder);
                                                    }
                                                }
                                            });
                                }
                            })
                            .detachIn(time, unit)
                            .onDetach(() -> {
                                if (!future.isDone()) future.complete(defaultValue);
                            });
                });

        return future;
    }

    public CompletableFuture<Integer> buildInteger() {
        return build().thenApply(lng -> {
            if (lng <= Integer.MAX_VALUE && lng >= Integer.MIN_VALUE) return lng.intValue();
            else
                throw new IndexOutOfBoundsException("The value is too " + (lng > Integer.MAX_VALUE ? "large" : "small") + "!");
        });
    }
}
