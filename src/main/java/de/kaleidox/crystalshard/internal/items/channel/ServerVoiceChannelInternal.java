package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.channel.ChannelType;
import de.kaleidox.crystalshard.main.items.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.objects.Evaluation;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServerVoiceChannelInternal extends ChannelInternal implements ServerVoiceChannel {
    private final static ConcurrentHashMap<Long, ServerVoiceChannel> instances = new ConcurrentHashMap<>();
    private final static Logger logger = new Logger(ServerVoiceChannelInternal.class);
    private final long id;
    private List<? extends ChannelAttachableListener> listeners;

    public ServerVoiceChannelInternal(Discord discord, Server server, JsonNode data) {
        super(discord, data);
        logger.deeptrace("Creating SVC object for data: " + data.toString());
        this.id = data.get("id").asLong();
        instances.put(id, this);
    }

    @Override
    public Server getServer() {
        return null;
    }

    @Override
    public Optional<ChannelCategory> getCategory() {
        return Optional.empty();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public ChannelType getType() {
        return ChannelType.GUILD_VOICE;
    }

    @Override
    public List<? extends ChannelAttachableListener> getListeners() {
        return listeners;
    }

    @Override
    public PermissionList getListFor(DiscordItem scope) {
        return null;
    }

    @Override
    public <C extends ChannelAttachableListener> ListenerManager<C> attachListener(C listener) {
        return null;
    }

    @Override
    public Evaluation<Boolean> detachListener(ChannelAttachableListener listener) {
        return null;
    }

    public static ServerVoiceChannel getInstance(Discord discord, JsonNode data) {
        long id = data.get("id").asLong(-1);
        if (id == -1) throw new NoSuchElementException("No valid ID found.");
        if (instances.containsKey(id))
            return instances.get(id);
        else
            return new ServerVoiceChannelInternal(
                    discord, ServerInternal.getInstance(discord, data.get("guild_id").asLong()), data);
    }
}
