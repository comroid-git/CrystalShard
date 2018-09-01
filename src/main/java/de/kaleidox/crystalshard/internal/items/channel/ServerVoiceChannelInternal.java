package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.channel.ChannelType;
import de.kaleidox.crystalshard.main.items.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.logging.Logger;

import java.util.Optional;

public class ServerVoiceChannelInternal implements ServerVoiceChannel {
    private final static Logger logger = new Logger(ServerVoiceChannelInternal.class);

    public ServerVoiceChannelInternal(Discord discord, Server server, JsonNode data) {
        logger.deeptrace("Creating SVC object for data: " + data.toString());
    }

    @Override
    public Server getServer() {
        return null;
    }

    @Override
    public Optional<ChannelCategory> getCategory() {
        return Optional.empty();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public ChannelType getType() {
        return null;
    }

    @Override
    public PermissionList getListFor(DiscordItem scope) {
        return null;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public Discord getDiscord() {
        return null;
    }
}
