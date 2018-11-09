package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.CoreDelegate;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.items.message.SendableInternal;
import de.kaleidox.crystalshard.internal.items.message.embed.EmbedDraftInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.Sendable;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.util.helpers.FutureHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static de.kaleidox.util.helpers.JsonHelper.*;

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
