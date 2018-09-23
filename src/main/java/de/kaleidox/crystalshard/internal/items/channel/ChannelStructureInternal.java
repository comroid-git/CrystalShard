package de.kaleidox.crystalshard.internal.items.channel;

import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.channel.ChannelStructure;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerTextChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerVoiceChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChannelStructureInternal extends ArrayList<ServerChannel> implements ChannelStructure {
    public ChannelStructureInternal(ArrayList<ServerChannel> channels) {
        super(channels);
    }
    
    // Override Methods
    @Override
    public int getPosition(ServerChannel channel) {
        return indexOf(channel);
    }
    
    @Override
    public ServerChannel getChannel(int fromPosition) {
        return get(fromPosition);
    }
    
    @Override
    public List<ServerTextChannel> getAllTextChannels() {
        return stream().filter(channel -> channel instanceof ServerTextChannel)
                .map(ServerTextChannel.class::cast)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ServerVoiceChannel> getAllVoiceChannels() {
        return stream().filter(channel -> channel instanceof ServerVoiceChannel)
                .map(ServerVoiceChannel.class::cast)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ChannelCategory> getAllCategories() {
        return stream().filter(channel -> channel instanceof ChannelCategory)
                .map(ChannelCategory.class::cast)
                .collect(Collectors.toList());
    }
}
