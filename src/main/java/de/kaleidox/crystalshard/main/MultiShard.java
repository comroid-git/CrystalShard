package de.kaleidox.crystalshard.main;

import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelCreateListener;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageCreateListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerCreateListener;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.Self;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.util.DiscordUtils;
import de.kaleidox.util.objects.Evaluation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

public class MultiShard extends ArrayList<Discord> implements Discord {
    MultiShard(List<Discord> loggedIn) {
        super(loggedIn);
    }

    @Override
    public String getPrefixedToken() {
        return get(0).getPrefixedToken();
    }

    @Override
    public int getShardId() {
        throw new IllegalStateException("Can not get the shard id of several shards!");
    }

    @Override
    public int getShards() {
        return get(0).getShards();
    }

    @Override
    public ListenerManager<ServerCreateListener> attachServerCreateListener(ServerCreateListener listener) {
        return attachListener(listener);
    }

    @Override
    public DiscordUtils getUtilities() {
        return null;
    }

    @Override
    public Optional<Channel> getChannelById(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(long id) {
        return Optional.empty();
    }

    @Override
    public Self getSelf() {
        return null;
    }

    @Override
    public Optional<Server> getServerById(long id) {
        return Optional.empty();
    }

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public ThreadPool getThreadPool() {
        return null;
    }

    @Override
    public Collection<Server> getServers() {
        return null;
    }

    @Override
    public Collection<User> getUsers() {
        return null;
    }

    @Override
    public int getServerCount() {
        return 0;
    }

    @Override
    public int getUserCount() {
        return 0;
    }

    @Override
    public ListenerManager<MessageCreateListener> attachMessageCreateListener(MessageCreateListener listener) {
        return null;
    }

    @Override
    public ListenerManager<ChannelCreateListener> attachChannelCreateListener(ChannelCreateListener listener) {
        return attachListener(listener);
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public Discord getDiscord() {
        return null;
    }

    @Override
    public <C extends DiscordAttachableListener> ListenerManager<C> attachListener(C listener) {
        return null;
    }

    @Override
    public Evaluation<Boolean> detachListener(DiscordAttachableListener listener) {
        return null;
    }
}
