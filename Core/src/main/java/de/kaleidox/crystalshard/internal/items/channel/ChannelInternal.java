package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.intellij.lang.annotations.MagicConstant;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.permission.Permission;
import de.kaleidox.crystalshard.api.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.api.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.api.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.ListenerManagerInternal;
import de.kaleidox.util.functional.Evaluation;
import de.kaleidox.util.helpers.FutureHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class ChannelInternal implements Channel {
    final Discord discord;
    @MagicConstant(valuesFromClass = Channel.Type.class)
    final int type;
    final boolean isPrivate;
    final long id;
    final List<ListenerManager<? extends ChannelAttachableListener>> listenerManagers;

    @SuppressWarnings("MagicConstant")
    ChannelInternal(Discord discord, JsonNode data) {
        this.discord = discord;
        this.id = data.get("id")
                .asLong();
        this.type = data.get("type").asInt();
        this.isPrivate = (type == Type.DIRECT_MESSAGE || type == Type.GROUP_DM);
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
    public int getType() {
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
