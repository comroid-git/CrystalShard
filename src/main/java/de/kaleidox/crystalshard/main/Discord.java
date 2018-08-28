package de.kaleidox.crystalshard.main;

import de.kaleidox.crystalshard.main.listener.ServerCreateListener;
import de.kaleidox.crystalshard.util.DiscordUtils;

public interface Discord {
    String getPrefixedToken();

    int getShardId();

    int getShards();

    void addServerCreateListener(ServerCreateListener listener);

    DiscordUtils getUtilities();
}
