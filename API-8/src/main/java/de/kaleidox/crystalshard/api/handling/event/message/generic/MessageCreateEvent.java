package de.kaleidox.crystalshard.api.handling.event.message.generic;

import de.kaleidox.crystalshard.api.handling.event.channel.ChannelEvent;
import de.kaleidox.crystalshard.api.handling.event.message.MessageEvent;
import de.kaleidox.crystalshard.api.handling.event.server.OptionalServerEvent;
import de.kaleidox.crystalshard.api.entity.user.Author;
import de.kaleidox.crystalshard.api.entity.user.AuthorUser;

import java.util.Optional;

public interface MessageCreateEvent extends MessageEvent, ChannelEvent, OptionalServerEvent {
    Author getMessageAuthor();

    Optional<AuthorUser> getMessageAuthorUser();

    String getMessageContent();
}
