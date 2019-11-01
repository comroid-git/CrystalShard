package de.comroid.crystalshard.core.gateway.event.message;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.GuildChannel;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.message.MessageCreateEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#message-create")
public class MessageCreateEventImpl extends AbstractGatewayEvent implements MessageCreateEvent {
    protected @JsonData Message message;

    public MessageCreateEventImpl(Discord api, JsonNode data) {
        super(api, data);

        message.getChannel()
                .as(GuildChannel.class)
                .map(GuildChannel::getGuild)
                .ifPresent(this::affects);
        affects(message.getChannel());
        message.getAuthor()
                .castAuthorToUser()
                .ifPresent(user -> {
                    affects(user);

                    message.getChannel()
                            .as(GuildChannel.class)
                            .map(GuildChannel::getGuild)
                            .flatMap(user::asGuildMember)
                            .ifPresent(member -> {
                                affects(member);

                                member.getRoles().forEach(this::affects);
                            });
                });
        message.getAuthor()
                .castAuthorToWebhook()
                .ifPresent(this::affects);
    }

    @Override
    public Message getMessage() {
        return message;
    }
}
