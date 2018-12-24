package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.message.Message;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class TextChannelInternal extends ChannelInternal implements TextChannel {
    final ConcurrentHashMap<Long, Message> messages;
    final ConcurrentHashMap<Long, Message> pinned;

    TextChannelInternal(Discord discord, JsonNode data) {
        super(discord, data);

        messages = new ConcurrentHashMap<>();
        pinned = new ConcurrentHashMap<>();
    }

    @Override
    public Collection<Message> getPinnedMessages() {
        return pinned.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public void updatePinned(Message message) {
        if (message.isPinned()) pinned.put(message.getId(), message);
        else pinned.remove(message.getId());
    }
}
