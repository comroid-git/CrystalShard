package de.comroid.crystalshard.impl.entity.channel;

import java.util.Collection;
import java.util.Optional;
import java.util.OptionalInt;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.GuildChannelCategory;
import de.comroid.crystalshard.api.entity.channel.GuildTextChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.model.permission.PermissionOverride;
import de.comroid.crystalshard.core.annotation.JsonData;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

public class GuildTextChannelImpl extends AbstractTextChannel<GuildTextChannelImpl> implements GuildTextChannel {
    protected final Guild guild;
    protected final @Nullable GuildChannelCategory category;
    protected @JsonData("topic") String topic;
    protected @JsonData("nsfw") boolean nsfw;
    protected @JsonData("guild_id") long guildId;
    protected @JsonData("position") int position;
    protected @JsonData(value = "permission_overwrites", type = PermissionOverride.class) Collection<PermissionOverride> permissionOverrides;
    protected @JsonData("name") String name;
    protected @JsonData("parent_id") long categoryId;

    public GuildTextChannelImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElseThrow(() -> new AssertionError("No valid Guild ID was sent with this GuildChannel!"));
        category = api.getCacheManager()
                .getChannelByID(categoryId)
                .flatMap(Channel::asGuildChannelCategory)
                .orElse(null);
    }

    @Override
    public Optional<String> getTopic() {
        return Optional.ofNullable(topic);
    }

    @Override
    public boolean isNSFW() {
        return nsfw;
    }

    @Override
    public OptionalInt getMessageRatelimit() {
        return rateLimitPerUser == -1 ? OptionalInt.empty() : OptionalInt.of(rateLimitPerUser);
    }
}
