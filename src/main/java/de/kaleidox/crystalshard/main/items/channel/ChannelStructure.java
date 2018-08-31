package de.kaleidox.crystalshard.main.items.channel;

import java.util.List;

public interface ChannelStructure extends List<ServerChannel> {
    int getPosition(ServerChannel channel);

    ServerChannel getChannel(int fromPosition);

    List<ServerTextChannel> getAllTextChannels();

    List<ServerVoiceChannel> getAllVoiceChannels();

    List<ChannelCategory> getAllCategories();
}
