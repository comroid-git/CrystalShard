package de.comroid.crystalshard.impl.entity.channel;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.GuildTextChannel;
import de.comroid.crystalshard.api.entity.guild.webhook.Webhook;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.api.model.channel.MessagePrintStream;
import de.comroid.crystalshard.api.model.guild.invite.Invite;

public class GuildTextChannelImpl extends TextChannelAbst<GuildTextChannelImpl> implements GuildTextChannel {
    public GuildTextChannelImpl(Discord api, JsonNode data) {
        super(api, data);
    }

    @Override public CompletableFuture<Collection<Webhook>> requestWebhooks() {
        return null;
    }

    @Override public GuildTextChannel.Updater createUpdater() {
        return null;
    }

    @Override public CompletableFuture<Collection<Invite>> requestInvites() {
        return null;
    }

    // todo
}
