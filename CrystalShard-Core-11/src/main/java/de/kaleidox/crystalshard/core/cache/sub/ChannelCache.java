package de.kaleidox.crystalshard.core.cache.sub;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelCategoryInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelInternal;
import de.kaleidox.crystalshard.internal.items.channel.GroupChannelInternal;
import de.kaleidox.crystalshard.internal.items.channel.PrivateTextChannelInternal;
import de.kaleidox.crystalshard.internal.items.channel.ServerTextChannelInternal;
import de.kaleidox.crystalshard.internal.items.channel.ServerVoiceChannelInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelType;
import de.kaleidox.crystalshard.main.items.server.Server;
import util.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static de.kaleidox.crystalshard.core.net.request.Method.*;

public class ChannelCache extends Cache<Channel, Long, Long> {
    private final DiscordInternal discordInternal;
    
    public ChannelCache(DiscordInternal discordInternal) {
        super(ChannelInternal.class,
              param -> ((JsonNode) param[2]).get("id").asLong(),
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
        return new WebRequest<Object[]>(discordInternal).method(GET).endpoint(Endpoint.Location.CHANNEL.toEndpoint(requestIdent)).execute(data -> {
            long guildId = data.path("guild_id").asLong(-1);
            Server server = guildId == -1 ? null : discordInternal.getServerCache().getOrRequest(guildId, guildId);
            return new Object[]{discordInternal, server, data};
        });
    }
    
    @NotNull
    @Override
    public Channel construct(Object... param) {
        Discord discord = (Discord) param[0];
        Server server = param[1] == null ? null : (Server) param[1];
        JsonNode data = (JsonNode) param[2];
        switch (ChannelType.getFromId(data.get("type").asInt())) {
            case GUILD_TEXT:
                return new ServerTextChannelInternal(discord, server, data);
            case DM:
                return new PrivateTextChannelInternal(discord, data);
            case GUILD_VOICE:
                return new ServerVoiceChannelInternal(discord, server, data);
            case GROUP_DM:
                return new GroupChannelInternal(discord, data);
            case GUILD_CATEGORY:
                return new ChannelCategoryInternal(discord, server, data);
            default:
                throw new NoSuchElementException("Unknown or no channel Type.");
        }
    }
}
