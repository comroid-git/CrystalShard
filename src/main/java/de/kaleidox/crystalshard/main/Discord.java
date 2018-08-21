package de.kaleidox.crystalshard.main;

import de.kaleidox.crystalshard.main.listener.ServerCreateListener;

public interface Discord {
    String getPrefixedToken();

    int getShardId();

    int getShards();

    void addServerCreateListener(ServerCreateListener listener);
}
