package de.kaleidox.crystalshard.core.cache.sub;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.core.cache.CacheImpl;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.InternalInjector;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.channel.ChannelType;
import de.kaleidox.crystalshard.main.items.channel.GroupChannel;
import de.kaleidox.crystalshard.main.items.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerTextChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.util.annotations.NotNull;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static de.kaleidox.crystalshard.core.net.request.HttpMethod.*;

public class ChannelCacheImpl extends CacheImpl<Channel, Long, Long> {
    private final Discord discordInternal;

    public ChannelCacheImpl(Discord discordInternal) {
        super(Channel.class,
                param -> ((JsonNode) param[2]).get("id")
                        .asLong(),
                TimeUnit.HOURS.toMillis(12),
                Discord.class,
                Server.class,
                JsonNode.class);
        this.discordInternal = discordInternal;
    }

    // Override Methods
    @NotNull
    @Override
    public CompletableFuture<Object[]> requestConstructorParameters(Long requestIdent) {
        WebRequest<Object[]> request = CoreInjector.webRequest(discordInternal);
        return request.setMethod(GET)
                .setUri(DiscordEndpoint.CHANNEL.createUri(requestIdent))
                .executeAs(data -> {
                    long guildId = data.path("guild_id")
                            .asLong(-1);
                    Server server = guildId == -1 ? null : discordInternal.getServerCache()
                            .getOrRequest(guildId, guildId);
                    return new Object[]{discordInternal, server, data};
                });
    }

    @NotNull
    @Override
    public Channel construct(Object... param) {
        Discord discord = (Discord) param[0];
        Server server = param[1] == null ? null : (Server) param[1];
        JsonNode data = (JsonNode) param[2];
        switch (ChannelType.getFromId(data.get("type")
                .asInt())) {
            case GUILD_TEXT:
                return InternalInjector.newInstance(ServerTextChannel.class, discord, server, data);
            case DM:
                return InternalInjector.newInstance(PrivateTextChannel.class, discord, data);
            case GUILD_VOICE:
                return InternalInjector.newInstance(ServerVoiceChannel.class, discord, server, data);
            case GROUP_DM:
                return InternalInjector.newInstance(GroupChannel.class, discord, data);
            case GUILD_CATEGORY:
                return InternalInjector.newInstance(ChannelCategory.class, discord, server, data);
            default:
                throw new NoSuchElementException("Unknown or no channel Type.");
        }
    }
}
