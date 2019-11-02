package de.comroid.crystalshard.impl.entity.channel;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.JsonNode;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.GuildStoreChannel;
import de.comroid.crystalshard.api.model.guild.invite.Invite;

public class GuildStoreChannelImpl extends ChannelAbst<GuildStoreChannelImpl> implements GuildStoreChannel {
    public GuildStoreChannelImpl(Discord api, JsonNode data) {
        super(api, data);
        
    }
//todo
    @Override public GuildStoreChannel.Updater createUpdater() {
        return null;
    }

    @Override public CompletableFuture<Collection<Invite>> requestInvites() {
        return null;
    }
}
