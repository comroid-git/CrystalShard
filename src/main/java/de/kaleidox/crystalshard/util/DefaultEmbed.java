package de.kaleidox.crystalshard.util;

import de.kaleidox.crystalshard.internal.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.Sendable;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class represents a default EmbedDraft.
 * It is used to define a default EmbedDraft that will be used by methods like
 * {@link MessageReciever#sendMessage(Consumer)}, where no embed draft is specified but an embed modifier is.
 */
public class DefaultEmbed implements Supplier<EmbedDraft> {
    public final static Supplier<EmbedDraft> EMPTY_SUPPLIER = () -> Embed.BUILDER().build();
    private final List<Consumer<Embed.Builder>> modifiers;

    /**
     * A static implementation of the {@link DefaultEmbed#get()} method.
     * This method will first check if the current thread is a {@link ThreadPool.Worker} thread, and if so,
     * will get the {@link Discord} item the Worker belongs to. Throws an exception if invoked from a wrong context.
     *
     * @implNote This method must only be invoked from "bot-own" threads.
     * @return The default EmbedDraft according to the {@link Discord} instance that this Thread belongs to.
     * @throws IllegalCallerException If the current thread does not belong to any {@link Discord} object.
     */
    public static EmbedDraft getStatic() {
        Thread thread = Thread.currentThread();

        if (thread instanceof ThreadPool.Worker) {
            return ((ThreadPool.Worker) thread)
                    .getDiscord()
                    .getUtilities()
                    .getDefaultEmbed()
                    .get();
        }
        throw new IllegalCallerException("The method DefaultEmbed#getStatic may only be called from a bot-own " +
                "thread, such as in listeners or scheduler tasks. You may not use it from contexts like " +
                "CompletableFuture#thenAcceptAsync or such.");
    }

    DefaultEmbed() {
        this.modifiers = new ArrayList<>();
    }

    public DefaultEmbed addModifier(Consumer<Embed.Builder> modifier) {
        modifiers.add(modifier);
        return this;
    }

    @Override
    public EmbedDraft get() {
        if (modifiers.isEmpty()) {
            return EMPTY_SUPPLIER.get();
        } else {
            Embed.Builder builder = Embed.BUILDER();
            modifiers.forEach(builderConsumer -> builderConsumer.accept(builder));
            return builder.build();
        }
    }
}
