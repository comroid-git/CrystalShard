package de.kaleidox.crystalshard.api.model.message;

import java.util.Collection;
import java.util.stream.Stream;

import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.model.channel.MessagePrintStream;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

public interface Messageable {
    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#get-channel-messages")
    Collection<Message> getMessages(int limit);

    @IntroducedBy(PRODUCTION)
    Stream<Message> getLatestMessagesAsStream();

    @IntroducedBy(PRODUCTION)
    Message.Composer composeMessage();

    @IntroducedBy(PRODUCTION)
    MessagePrintStream openPrintStream();
}
