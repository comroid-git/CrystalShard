package de.kaleidox.crystalshard.main.handling.event.message;

import de.kaleidox.crystalshard.main.handling.event.channel.ChannelEvent;
import de.kaleidox.crystalshard.main.handling.event.server.OptionalServerEvent;
import de.kaleidox.crystalshard.main.items.user.Author;
import de.kaleidox.crystalshard.main.items.user.AuthorUser;

import java.util.Optional;

public interface MessageCreateEvent extends MessageEvent, ChannelEvent, OptionalServerEvent {
    Author getMessageAuthor();

    Optional<AuthorUser> getMessageAuthorUser();

    String getMessageContent();
}
