package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.ListenerManagerInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelType;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.util.objects.Evaluation;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ChannelInternal implements Channel {
    final Discord discord;
    final ChannelType type;
    final boolean isPrivate;
    final long id;
    final List<ListenerManager<? extends ChannelAttachableListener>> listenerManagers;

    ChannelInternal(Discord discord, JsonNode data) {
        this.discord = discord;
        this.id = data.get("id").asLong();
        this.type = ChannelType.getFromId(data.get("type").asInt());
        this.isPrivate = (type == ChannelType.DM || type == ChannelType.GROUP_DM);
        this.listenerManagers = new ArrayList<>();
    }

    public static Channel getInstance(Discord discord, long id) {
        return collectInstances()
                .stream()
                .filter(channel -> channel.getId() == id)
                .findAny()
                .orElseGet(() -> new WebRequest<Channel>(discord)
                        .method(Method.GET)
                        .endpoint(Endpoint.Location.CHANNEL.toEndpoint(id))
                        .execute(node -> getInstance(discord, node))
                        .join());
    }

    public static Channel getInstance(Discord discord, JsonNode data) {
        Server server = data.has("guild_id") ?
                ServerInternal.getInstance(discord, data.get("guild_id").asLong()) : null;
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

    private static Collection<Channel> collectInstances() {
        List<Channel> collect = new ArrayList<>();
        ChannelCategoryInternal.instances
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .forEachOrdered(collect::add);
        GroupChannelInternal.instances
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .forEachOrdered(collect::add);
        PrivateTextChannelInternal.instances
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .forEachOrdered(collect::add);
        ServerTextChannelInternal.instances
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .forEachOrdered(collect::add);
        ServerVoiceChannelInternal.instances
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .forEachOrdered(collect::add);
        return collect;
    }

    @Override
    public Discord getDiscord() {
        return discord;
    }

    @Override
    public <C extends ChannelAttachableListener> ListenerManager<C> attachListener(C listener) {
        ListenerManagerInternal<C> manager = ListenerManagerInternal.getInstance((DiscordInternal) discord, listener);
        listenerManagers.add(manager);
        return manager;
    }

    @Override
    public Evaluation<Boolean> detachListener(ChannelAttachableListener listener) {
        ListenerManagerInternal<? extends ChannelAttachableListener> manager =
                ListenerManagerInternal.getInstance((DiscordInternal) discord, listener);
        return Evaluation.of(listenerManagers.remove(manager));
    }

    @Override
    public Collection<ChannelAttachableListener> getAttachedListeners() {
        return listenerManagers.stream()
                .map(ListenerManager::getListener)
                .collect(Collectors.toList());
    }

    @Override
    public ChannelType getType() {
        return type;
    }

    @Override
    public long getId() {
        return id;
    }
}
