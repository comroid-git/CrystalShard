package de.comroid.crystalshard.core.gateway.event.message;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.EntityType;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.api.entity.user.GuildMember;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.message.MessageDeleteBulkEvent;
import de.comroid.crystalshard.util.Util;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#message-delete-bulk")
public class MessageDeleteBulkEventImpl extends AbstractGatewayEvent implements MessageDeleteBulkEvent {
    protected @JsonData(value = "ids", type = Long.class) Collection<Long> messageIds;
    protected @JsonData("channel_id") long channelId;
    protected @JsonData("guild_id") long guildId;

    private @Nullable Guild guild;
    private TextChannel channel;

    public MessageDeleteBulkEventImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElse(null);
        channel = api.getCacheManager()
                .getChannelByID(channelId)
                .flatMap(Channel::asTextChannel)
                .orElseThrow(() -> new AssertionError("No Channel ID was sent with this MessageDeleteBulkEvent!"));

        getGuild().ifPresent(this::affects);
        affects(channel);
        Util.quickStream(50, messageIds)
                .flatMap(id -> api.getCacheManager()
                        .streamSnowflakesByID(id)
                        .stream())
                .filter(flake -> flake.getEntityType() == EntityType.MESSAGE)
                .map(Message.class::cast)
                .forEach(msg -> {
                    affects(msg);

                    msg.getAuthor()
                            .castAuthorToUser()
                            .ifPresent(usr -> {
                                affects(usr);

                                getGuild().flatMap(usr::asGuildMember)
                                        .map(GuildMember::getRoles)
                                        .ifPresent(roles -> roles.forEach(this::affects));
                            });
                });
    }

    @Override
    public Collection<Long> getIDs() {
        return Collections.unmodifiableCollection(messageIds);
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
