package de.kaleidox.crystalshard.internal.handling.event.server.other;

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.handling.event.server.other.ServerWebhookUpdateEvent;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;

public class ServerWebhookUpdateEventInternal extends EventBase implements ServerWebhookUpdateEvent {
    private final Server server;
    private final Channel channel;

    public ServerWebhookUpdateEventInternal(DiscordInternal discordInternal, Server server, Channel channel) {
        super(discordInternal);
        this.server = server;
        this.channel = channel;
    }

    // Override Methods
    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }
}
