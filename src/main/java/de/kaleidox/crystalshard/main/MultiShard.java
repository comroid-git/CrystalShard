package de.kaleidox.crystalshard.main;

import de.kaleidox.crystalshard.internal.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.main.handling.listener.listener.MessageCreateListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerCreateListener;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.Self;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.util.DiscordUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Executor;

public class MultiShard extends ArrayList<Discord> implements Discord {
    @Override
    public String getPrefixedToken() {
        return null;
    }

    @Override
    public int getShardId() {
        return 0;
    }

    @Override
    public int getShards() {
        return 0;
    }

    @Override
    public void addServerCreateListener(ServerCreateListener listener) {

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
    public void attachMessageCreateListener(MessageCreateListener listener) {

    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public Discord getDiscord() {
        return null;
    }
}
