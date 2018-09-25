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
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelType;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.util.annotations.NotNull;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static de.kaleidox.crystalshard.core.net.request.Method.*;

public class ChannelCache extends Cache<Channel, Long, Long> {
    private final DiscordInternal discordInternal;
    
    public ChannelCache(DiscordInternal discordInternal) {
        super(ChannelInternal.class, TimeUnit.HOURS.toMillis(12), Discord.class, JsonNode.class);
        this.discordInternal = discordInternal;
    }
    
    @NotNull
    @Override
    public CompletableFuture<Object[]> requestConstructorParameters(Long requestIdent) {
        return new WebRequest<Object[]>(discordInternal)
                .method(GET)
                .endpoint(Endpoint.Location.CHANNEL.toEndpoint(requestIdent))
                .execute(node -> new Object[]{discordInternal, node});
    }
    
    @NotNull
    @Override
    public Channel construct(Object... parameters) {
        Discord discord = (Discord) parameters[0];
        JsonNode data = (JsonNode) parameters[1];
        Server server = data.has("guild_id") ? ServerInternal.getInstance(discord, data.get("guild_id").asLong()) :
                        null;
        switch (ChannelType.getFromId(data.get("type").asInt())) {
            case GUILD_TEXT:
                return ServerTextChannelInternal.getInstance(discord, server, data);
            case DM:
                return PrivateTextChannelInternal.getInstance(discord, data);
            case GUILD_VOICE:
                return ServerVoiceChannelInternal.getInstance(discord, server, data);
            case GROUP_DM:
                return GroupChannelInternal.getInstance(discord, data);
            case GUILD_CATEGORY:
                return ChannelCategoryInternal.getInstance(discord, server, data);
            default:
                throw new NoSuchElementException("Unknown or no channel Type.");
        }
    }
}
