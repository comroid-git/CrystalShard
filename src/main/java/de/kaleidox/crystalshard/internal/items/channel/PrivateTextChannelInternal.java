package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageCreateListener;
import de.kaleidox.crystalshard.main.items.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.Sendable;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.objects.Evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class PrivateTextChannelInternal extends ChannelInternal implements PrivateTextChannel {
    final static ConcurrentHashMap<Long, PrivateTextChannel> instances = new ConcurrentHashMap<>();
    private final static Logger logger = new Logger(PrivateTextChannelInternal.class);
    private final List<Message> messages = new ArrayList<>();
    private final List<ChannelAttachableListener> listeners = new ArrayList<>();
    private long id;

    private PrivateTextChannelInternal(DiscordInternal discord, JsonNode data) {
        super(discord, data);
        logger.deeptrace("Creating PTC object for data: " + data.toString());
        this.id = data.get("id").asLong();
    }

    @Override
    public CompletableFuture<Message> sendMessage(Sendable content) {
        return null;
    }

    @Override
    public CompletableFuture<Message> sendMessage(Consumer<Embed.Builder> defaultEmbedModifier) {
        return null;
    }

    @Override
    public CompletableFuture<Message> sendMessage(EmbedDraft embedDraft) {
        return null;
    }

    @Override
    public CompletableFuture<Message> sendMessage(String content) {
        return null;
    }

    @Override
    public CompletableFuture<Void> typing() {
        return null;
    }

    public Message craftMessage(JsonNode data) {
        MessageInternal messageInternal = (MessageInternal) MessageInternal.getInstance(getDiscord(), null, data);
        this.messages.add(messageInternal);
        return messageInternal;
    }

    @Override
    public void attachMessageCreateListener(MessageCreateListener listener) {
        listeners.add(listener);
    }

    @Override
    public List<? extends ChannelAttachableListener> getListeners() {
        return listeners;
    }

    @Override
    public <C extends ChannelAttachableListener> ListenerManager<C> attachListener(C listener) {
        return null;
    }

    @Override
    public Evaluation<Boolean> detachListener(ChannelAttachableListener listener) {
        return null;
    }

    @Override
    public String toString() {
        return "PrivateTextChannel with ID [" + id + "]";
    }

    public static PrivateTextChannel getInstance(Discord discord, JsonNode data) {
        long id = data.get("id").asLong(-1);
        if (id == -1) throw new NoSuchElementException("No valid ID found.");
        if (instances.containsKey(id))
            return instances.get(id);
        else
            return new PrivateTextChannelInternal((DiscordInternal) discord, data);
    }
}
