package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.channel.ChannelType;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.util.objects.Evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelCategoryInternal extends ArrayList<Channel> implements ChannelCategory {
    private final static ConcurrentHashMap<Long, ChannelCategory> instances = new ConcurrentHashMap<>();
    private final Discord discord;
    private final Server server;
    private final String name;
    private final long id;
    private List<? extends ChannelAttachableListener> listeners;

    private ChannelCategoryInternal(Discord discord, Server server, JsonNode data) {
        this.discord = discord;
        this.server = server;
        this.id = data.get("id").asLong();
        this.name = data.path("name").asText("undefined");
        listeners = new ArrayList<>();
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Optional<ChannelCategory> getCategory() {
        return Optional.empty();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ChannelType getType() {
        return ChannelType.GUILD_CATEGORY;
    }

    @Override
    public PermissionList getListFor(DiscordItem scope) {
        return null;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Discord getDiscord() {
        return discord;
    }

    public List<? extends ChannelAttachableListener> getListeners() {
        return listeners;
    }

    @Override
    public <C extends ChannelAttachableListener> ListenerManager<C> attachListener(C listener) {
        return null;
    }

    @Override
    public Evaluation<Boolean> detachListener(ChannelAttachableListener listener) {
        return null;
    }

    @Override
    public String toString() {
        return "ChannelCategory with ID [" + id + "]";
    }

    public static ChannelCategory getInstance(Discord discord, Server server, JsonNode data) {
        long id = data.get("id").asLong(-1);
        if (id == -1) throw new NoSuchElementException("No valid ID found.");
        if (instances.containsKey(id))
            return instances.get(id);
        else
            return new ChannelCategoryInternal(discord, server, data);
    }
}
