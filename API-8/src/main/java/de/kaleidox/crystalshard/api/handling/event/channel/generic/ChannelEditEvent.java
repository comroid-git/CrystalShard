package de.kaleidox.crystalshard.api.handling.event.channel.generic;

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.api.handling.event.channel.ChannelEvent;

public interface ChannelEditEvent extends ChannelEvent, EditEvent<Channel> {
}
