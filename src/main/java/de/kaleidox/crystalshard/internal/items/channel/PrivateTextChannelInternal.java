package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.main.items.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.Sendable;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.listener.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.listener.MessageCreateListener;
import de.kaleidox.logging.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PrivateTextChannelInternal extends ChannelInternal implements PrivateTextChannel {
    private final static Logger logger = new Logger(PrivateTextChannelInternal.class);
    private final List<Message> messages = new ArrayList<>();
    private final List<ChannelAttachableListener> listeners = new ArrayList<>();

    public PrivateTextChannelInternal(DiscordInternal discord, JsonNode data) {
        super(discord, data);
        logger.deeptrace("Creating PTC object for data: " + data.toString());
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
        MessageInternal messageInternal = new MessageInternal(getDiscord(), null, data);
        this.messages.add(messageInternal);
        return messageInternal;
    }

    @Override
    public void attachMessageCreateListener(MessageCreateListener listener) {
        listeners.add(listener);
    }

    public Collection<ChannelAttachableListener> getListeners() {
        return null;
    }
}
