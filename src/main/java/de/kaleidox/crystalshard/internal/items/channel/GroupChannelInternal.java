package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageCreateListener;
import de.kaleidox.crystalshard.main.items.channel.GroupChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.Sendable;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.util.objects.Evaluation;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class GroupChannelInternal extends ChannelInternal implements GroupChannel {
    final static ConcurrentHashMap<Long, GroupChannel> instances = new ConcurrentHashMap<>();
    private final long id;
    private List<? extends ChannelAttachableListener> listeneners;

    GroupChannelInternal(Discord discord, JsonNode data) {
        super(discord, data);
        this.id = data.get("id").asLong();
        instances.put(id, this);
    }

    @Override
    public List<? extends ChannelAttachableListener> getListeners() {
        return listeneners;
    }

    @Override
    public void attachMessageCreateListener(MessageCreateListener listener) {

    }

    @Override
    public CompletableFuture<Message> sendMessage(Sendable content) {
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

    @Override
    public <C extends ChannelAttachableListener> ListenerManager<C> attachListener(C listener) {
        return null;
    }

    @Override
    public Evaluation<Boolean> detachListener(ChannelAttachableListener listener) {
        return null;
    }

    public static GroupChannel getInstance(Discord discord, JsonNode data) {
        long id = data.get("id").asLong(-1);
        if (id == -1) throw new NoSuchElementException("No valid ID found.");
        if (instances.containsKey(id))
            return instances.get(id);
        else
            return new GroupChannelInternal(discord, data);
    }
}
