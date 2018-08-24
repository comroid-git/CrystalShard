package de.kaleidox.crystalshard.main.items.channel;

import java.util.List;

public interface ChannelStructure extends List<ServerChannel> {
    int getPosition(ServerChannel channel);

    int getRealPosition(ServerChannel channel);
}
