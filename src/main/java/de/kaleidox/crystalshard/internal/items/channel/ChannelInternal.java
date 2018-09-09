package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelType;
import de.kaleidox.crystalshard.main.items.channel.GroupChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.main.items.server.Server;

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

    public static Channel getInstance(Discord discord, JsonNode data) {
        if (data.has("guild_id")) {
            if (data.has("bitrate")) {
                return ServerVoiceChannelInternal.getInstance(discord, data);
            } else {
                return ServerTextChannelInternal.getInstance(discord, data);
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
