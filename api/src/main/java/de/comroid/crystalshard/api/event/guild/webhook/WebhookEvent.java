package de.comroid.crystalshard.api.event.guild.webhook;

import de.comroid.crystalshard.api.entity.channel.GuildTextChannel;
import de.comroid.crystalshard.api.entity.guild.webhook.Webhook;
import de.comroid.crystalshard.api.event.channel.ChannelEvent;
import de.comroid.crystalshard.api.event.guild.GuildEvent;
import de.comroid.crystalshard.api.event.model.Event;

public interface WebhookEvent extends Event, GuildEvent {
    Webhook getTriggeringWebhook();
}
