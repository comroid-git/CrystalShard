package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.PrivateTextChannel;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

public class PrivateTextChannelInternal extends TextChannelInternal implements PrivateTextChannel {
    final static ConcurrentHashMap<Long, PrivateTextChannel> instances = new ConcurrentHashMap<>();

    private PrivateTextChannelInternal(DiscordInternal discord, JsonNode data) {
        super(discord, data);

        instances.put(id, this);
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
