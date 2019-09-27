package de.kaleidox.crystalshard.core.gateway.event.message.reaction;

import java.util.Optional;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.message.reaction.MessageReactionRemoveAllEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#message-reaction-remove-all")
public class MessageReactionRemoveAllEventImpl extends AbstractGatewayEvent implements MessageReactionRemoveAllEvent {
    protected @JsonData("channel_id") long channelId;
    protected @JsonData("message_id") long messageId;
    protected @JsonData("guild_id") long guildId;

    private @Nullable Guild guild;
    private TextChannel channel;
    private Message message;

    public MessageReactionRemoveAllEventImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElse(null);
        channel = api.getCacheManager()
                .getChannelByID(channelId)
                .flatMap(Channel::asTextChannel)
                .orElseThrow(() -> new AssertionError("No valid Channel ID was sent with this MessageReactionRemoveAllEvent!"));
        message = api.getCacheManager()
                .getMessageByID(channelId, messageId)
                .orElseThrow(() -> new AssertionError("No valid Message ID was sent with this MessageReactionRemoveAllEvent!"));

        getGuild().ifPresent(this::affects);
        affects(channel);
        affects(message);
    }

    @Override
    public TextChannel getChannel() {
        return channel;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public Optional<Guild> getGuild() {
        return Optional.ofNullable(guild);
    }
}
