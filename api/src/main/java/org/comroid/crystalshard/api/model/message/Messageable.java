package org.comroid.crystalshard.api.model.message;

import java.util.Collection;
import java.util.stream.Stream;

import org.comroid.crystalshard.api.entity.message.Message;
import org.comroid.crystalshard.api.model.channel.MessagePrintStream;
import org.comroid.crystalshard.util.annotation.IntroducedBy;

import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

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
