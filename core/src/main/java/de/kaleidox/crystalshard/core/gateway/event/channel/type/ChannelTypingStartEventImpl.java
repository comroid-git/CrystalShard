package de.kaleidox.crystalshard.core.gateway.event.channel.type;

import java.time.Instant;
import java.util.Optional;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.channel.type.ChannelTypingStartEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#typing-start")
public class ChannelTypingStartEventImpl extends AbstractGatewayEvent implements ChannelTypingStartEvent {
    protected @JsonData("channel_id") long channelId;
    protected @JsonData("guild_id") long guildId;
    protected @JsonData("user_id") long userId;
    protected @JsonData("timestamp") int typingStartInSec;

    private @Nullable Guild guild;
    private TextChannel channel;
    private User user;
    private Instant typingStartTimestamp;

    public ChannelTypingStartEventImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElse(null);
        user = api.getCacheManager()
                .getUserByID(userId)
                .orElseThrow(() -> new AssertionError("No valid User ID was sent with this TypingStartEvent!"));
        typingStartTimestamp = Instant.ofEpochSecond(typingStartInSec);

        getGuild().ifPresent(guild -> {
            affects(guild);

            user.asGuildMember(guild)
                    .ifPresent(member -> {
                        affects(member);

                        member.getRoles().forEach(this::affects);
                    });
        });
        affects(channel);
        affects(user);
    }

    @Override
    public TextChannel getChannel() {
        return channel;
    }

    @Override
    public Optional<Guild> getGuild() {
        return Optional.ofNullable(guild);
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Instant getStartedTimestamp() {
        return typingStartTimestamp;
    }
}
