package de.kaleidox.crystalshard.main;

import de.kaleidox.crystalshard.internal.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.main.event.channel.MessageCreateEvent;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.Self;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.main.listener.MessageCreateListener;
import de.kaleidox.crystalshard.main.listener.ServerCreateListener;
import de.kaleidox.crystalshard.util.DiscordUtils;

import java.util.Optional;
import java.util.concurrent.Executor;

public interface Discord extends UserContainer, ChannelContainer {
    String getPrefixedToken();

    int getShardId();

    int getShards();

    void addServerCreateListener(ServerCreateListener listener);

    DiscordUtils getUtilities();

    Optional<Channel> getChannelById(long id);

    Optional<User> getUserById(long id);

    Self getSelf();

    Optional<Server> getServerById(long id);

    Executor getExecutor();

    ThreadPool getThreadPool();

    void attachMessageCreateListener(MessageCreateListener listener);
}
