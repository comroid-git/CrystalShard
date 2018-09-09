package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelType;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.util.annotations.Nullable;

import java.util.List;

public abstract class ChannelInternal implements Channel {
    private final Discord discord;
    private final ChannelType type;
    private final long id;

    ChannelInternal(Discord discord, JsonNode data) {
        this.discord = discord;
        this.id = data.get("id").asLong();
        this.type = ChannelType.getFromId(data.get("type").asInt());
    }

    @Override
    public ChannelType getType() {
        return type;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Discord getDiscord() {
        return discord;
    }

    public abstract List<? extends ChannelAttachableListener> getListeners();

    public static Channel getInstance(Discord discord, long id) {
        return discord.getChannelById(id)
                .orElseGet(() -> new WebRequest<Channel>(discord)
                        .method(Method.GET)
                        .endpoint(Endpoint.Location.CHANNEL.toEndpoint(id))
                        .execute(node -> ChannelInternal.getInstance(discord, null, node))
                        .join());
    }

    public static Channel getInstance(Discord discord, @Nullable Server server, JsonNode data) {
        if (data.has("guild_id")) {
            if (data.has("bitrate")) {
                return ServerVoiceChannelInternal.getInstance(discord, server, data);
            } else {
                return ServerTextChannelInternal.getInstance(discord, server, data);
            }
        } else {
            if (data.has("recipients")) {
                return GroupChannelInternal.getInstance(discord, data);
            } else {
                return PrivateTextChannelInternal.getInstance(discord, data);
            }
        }
    }
}
