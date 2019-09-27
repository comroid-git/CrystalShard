package de.kaleidox.crystalshard.core.gateway.event.message;

import java.util.Optional;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.user.GuildMember;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.message.MessageDeleteEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#message-delete")
public class MessageDeleteEventImpl extends AbstractGatewayEvent implements MessageDeleteEvent {
    protected @JsonData("id") long messageId;
    protected @JsonData("channel_id") long channelId;
    protected @JsonData("guild_id") long guildId;

    private @Nullable Guild guild;
    private TextChannel channel;
    private Message message;

    public MessageDeleteEventImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElse(null);
        channel = api.getCacheManager()
                .getChannelByID(channelId)
                .flatMap(Channel::asTextChannel)
                .orElseThrow(() -> new AssertionError("No Channel ID was sent with this MessageDeleteEvent!"));
        message = api.getCacheManager()
                .getMessageByID(channelId, messageId)
                .orElseThrow(() -> new AssertionError("No Message ID was sent with this MessageDeleteEvent!"));

        getGuild().ifPresent(this::affects);
        affects(channel);
        affects(message);
        message.getAuthor()
                .castAuthorToUser()
                .ifPresent(usr -> {
                    affects(usr);

                    getGuild().flatMap(usr::asGuildMember)
                            .map(GuildMember::getRoles)
                            .ifPresent(roles -> roles.forEach(this::affects));
                });
        message.getAuthor()
                .castAuthorToWebhook()
                .ifPresent(this::affects);
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public TextChannel getChannel() {
        return channel;
    }

    @Override
    public Optional<Guild> getGuild() {
        return Optional.ofNullable(guild);
    }
}
