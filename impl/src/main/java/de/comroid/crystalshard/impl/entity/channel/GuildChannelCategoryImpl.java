package de.comroid.crystalshard.impl.entity.channel;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.JsonNode;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.GuildChannelCategory;
import de.comroid.crystalshard.api.model.guild.invite.Invite;

public class GuildChannelCategoryImpl extends ChannelAbst<GuildChannelCategoryImpl> implements GuildChannelCategory {
    public GuildChannelCategoryImpl(Discord api, JsonNode data) {
        super(api, data);
    }

    @Override
    public CompletableFuture<Collection<Invite>> requestInvites() {
        return null;
    }
}
