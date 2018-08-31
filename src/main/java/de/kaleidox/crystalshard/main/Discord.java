package de.kaleidox.crystalshard.main;

import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.listener.ServerCreateListener;
import de.kaleidox.crystalshard.util.DiscordUtils;

import java.util.Optional;

public interface Discord extends UserContainer, ChannelContainer {
    String getPrefixedToken();

    int getShardId();

    int getShards();

    void addServerCreateListener(ServerCreateListener listener);

    DiscordUtils getUtilities();

    Optional<Channel> getChannelById(long id);
}
