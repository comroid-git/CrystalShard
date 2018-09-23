package de.kaleidox.crystalshard.util;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.internal.items.message.embed.SentEmbedInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This class represents a default EmbedDraft. It is used to define a default EmbedDraft that will be used by methods
 * like {@link MessageReciever#sendMessage(Consumer)}, where no embed draft is specified but an embed modifier is.
 * <p>
 * Basic DefaultEmbed traits can be set up in a settings.json configuration file at resources root.
 */
public class DefaultEmbed implements Supplier<EmbedDraft> {
    // Static Fields
    // Fields
    public final  Supplier<EmbedDraft>          EMPTY_SUPPLIER;
    public final  Supplier<Embed.Builder>       EMPTY_BUILDER;
    private final List<Consumer<Embed.Builder>> modifiers;
    private final Discord                       discord;
    
    DefaultEmbed(Discord discord, JsonNode data) {
        this.discord = discord;
        this.modifiers = new ArrayList<>();
        
        EMPTY_BUILDER = data.isNull() ? Embed::BUILDER : () -> new SentEmbedInternal(null, data).toBuilder();
        EMPTY_SUPPLIER = () -> EMPTY_BUILDER.get()
                .build();
    }
    
    // Override Methods
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
    
    public Discord getDiscord() {
        return discord;
    }
    
    public DefaultEmbed addModifier(Consumer<Embed.Builder> modifier) {
        modifiers.add(modifier);
        return this;
    }
    
    public Embed.Builder getBuilder() {
        if (modifiers.isEmpty()) {
            return EMPTY_BUILDER.get();
        } else {
            Embed.Builder builder = EMPTY_BUILDER.get();
            modifiers.forEach(builderConsumer -> builderConsumer.accept(builder));
            return builder;
        }
    }
    
    // Static membe
    
    // Static members
    
    /**
     * A static implementation of the {@link DefaultEmbed#get()} method. This method will first check if the current
     * thread is a {@link ThreadPool.Worker} thread, and if so, will get the {@link Discord} item the Worker belongs to.
     * Throws an exception if invoked from a wrong context. This method must only be invoked from "bot-own" threads.
     *
     * @param defaultEmbedModifier A modifier to be run on the default embed.
     * @return The default EmbedDraft according to the {@link Discord} instance that this Thread belongs to.
     * @throws IllegalCallerException If the current thread does not belong to any {@link Discord} object.
     * @see ThreadPool#isBotOwnThread()
     */
    public static EmbedDraft getStatic(Consumer<Embed.Builder> defaultEmbedModifier) {
        ThreadPool.requireBotOwnThread();
        Thread thread = Thread.currentThread();
        
        if (thread instanceof ThreadPool.Worker) {
            Embed.Builder builder = ((ThreadPool.Worker) thread).getDiscord()
                    .getUtilities()
                    .getDefaultEmbed()
                    .get()
                    .toBuilder();
            defaultEmbedModifier.accept(builder);
            return builder.build();
        }
        throw new IllegalCallerException("The method DefaultEmbed#getStatic may only be called from a bot-own " +
                                         "thread, such as in listeners or scheduler tasks. You may not use it from " +
                                         "contexts like " + "CompletableFuture#thenAcceptAsync or such.");
    }
    
    /**
     * A static implementation of the {@link DefaultEmbed#get()} method. This method will first check if the current
     * thread is a {@link ThreadPool.Worker} thread, and if so, will get the {@link Discord} item the Worker belongs to.
     * Throws an exception if invoked from a wrong context. This method must only be invoked from "bot-own" threads.
     *
     * @return The default EmbedDraft according to the {@link Discord} instance that this Thread belongs to.
     * @throws IllegalCallerException If the current thread does not belong to any {@link Discord} object.
     * @see ThreadPool#isBotOwnThread()
     */
    public static EmbedDraft getStatic() {
        ThreadPool.requireBotOwnThread();
        Thread thread = Thread.currentThread();
        
        if (thread instanceof ThreadPool.Worker) {
            return ((ThreadPool.Worker) thread).getDiscord()
                    .getUtilities()
                    .getDefaultEmbed()
                    .get();
        }
        throw new IllegalCallerException("The method DefaultEmbed#getStatic may only be called from a bot-own " +
                                         "thread, such as in listeners or scheduler tasks. You may not use it from " +
                                         "contexts like " + "CompletableFuture#thenAcceptAsync or such.");
    }
    
    /**
     * A static implementation of the {@link DefaultEmbed#get()} method. This method will first check if the current
     * thread is a {@link ThreadPool.Worker} thread, and if so, will get the {@link Discord} item the Worker belongs to.
     * Throws an exception if invoked from a wrong context. This method must only be invoked from "bot-own" threads.
     *
     * @return The default EmbedDraft according to the {@link Discord} instance that this Thread belongs to.
     * @throws IllegalCallerException If the current thread does not belong to any {@link Discord} object.
     * @see ThreadPool#isBotOwnThread()
     */
    public static Embed.Builder getBuilderStatic() {
        ThreadPool.requireBotOwnThread();
        Thread thread = Thread.currentThread();
        
        if (thread instanceof ThreadPool.Worker) {
            return ((ThreadPool.Worker) thread).getDiscord()
                    .getUtilities()
                    .getDefaultEmbed()
                    .getBuilder();
        }
        throw new IllegalCallerException("The method DefaultEmbed#getStatic may only be called from a bot-own " +
                                         "thread, such as in listeners or scheduler tasks. You may not use it from " +
                                         "contexts like " + "CompletableFuture#thenAcceptAsync or such.");
    }
    
    /**
     * A static implementation to acquire a Thread-Fitting DefaultEmbed object. This method must only be invoked from
     * "bot-own" threads.
     *
     * @return The according DefaultEmbed object.
     * @throws IllegalCallerException If the current thread does not belong to any {@link Discord} object.
     * @see ThreadPool#isBotOwnThread()
     */
    public static DefaultEmbed getInstance() {
        ThreadPool.requireBotOwnThread();
        Thread thread = Thread.currentThread();
        
        if (thread instanceof ThreadPool.Worker) {
            return ((ThreadPool.Worker) thread).getDiscord()
                    .getUtilities()
                    .getDefaultEmbed();
        }
        throw new IllegalCallerException("The method DefaultEmbed#getStatic may only be called from a bot-own " +
                                         "thread, such as in listeners or scheduler tasks. You may not use it from " +
                                         "contexts like " + "CompletableFuture#thenAcceptAsync or such.");
    }
}
