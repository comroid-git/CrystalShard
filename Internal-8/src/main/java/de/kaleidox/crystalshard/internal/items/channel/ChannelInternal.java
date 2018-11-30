package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.ListenerManagerInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelType;
import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.util.helpers.FutureHelper;
import de.kaleidox.util.objects.functional.Evaluation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class ChannelInternal implements Channel {
    final Discord discord;
    final ChannelType type;
    final boolean isPrivate;
    final long id;
    final List<ListenerManager<? extends ChannelAttachableListener>> listenerManagers;

    ChannelInternal(Discord discord, JsonNode data) {
        this.discord = discord;
        this.id = data.get("id")
                .asLong();
        this.type = ChannelType.getFromId(data.get("type")
                .asInt());
        this.isPrivate = (type == ChannelType.DM || type == ChannelType.GROUP_DM);
        this.listenerManagers = new ArrayList<>();
    }

    public abstract Set<EditTrait<Channel>> updateData(JsonNode data);

    // Override Methods
    @Override
    public Discord getDiscord() {
        return discord;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public <C extends ChannelAttachableListener> ListenerManager<C> attachListener(C listener) {
        ListenerManagerInternal<C> manager = ListenerManagerInternal.getInstance((DiscordInternal) discord, listener);
        listenerManagers.add(manager);
        return manager;
    }

    @Override
    public Evaluation<Boolean> detachListener(ChannelAttachableListener listener) {
        ListenerManagerInternal<? extends ChannelAttachableListener> manager = ListenerManagerInternal.getInstance((DiscordInternal) discord, listener);
        return Evaluation.of(listenerManagers.remove(manager));
    }

    @Override
    public Collection<ListenerManager<? extends ChannelAttachableListener>> getListenerManagers() {
        return listenerManagers;
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
    public CompletableFuture<Void> delete() {
        if (!hasPermission(discord.getSelf(), Permission.MANAGE_CHANNELS))
            return FutureHelper.failedFuture(new DiscordPermissionException(
                    "Cannot delete channel!",
                    Permission.MANAGE_CHANNELS));
        return CoreInjector.webRequest(discord)
                .setMethod(HttpMethod.DELETE)
                .setUri(DiscordEndpoint.CHANNEL.createUri(id))
                .executeAsVoid();
    }

    @Override
    public Cache<Channel, Long, Long> getCache() {
        return discord.getChannelCache();
    }
}
