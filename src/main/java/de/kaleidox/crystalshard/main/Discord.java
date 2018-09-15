package de.kaleidox.crystalshard.main;

import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelCreateListener;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageCreateListener;
import de.kaleidox.crystalshard.main.handling.listener.server.generic.ServerCreateListener;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.Self;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.main.util.ChannelContainer;
import de.kaleidox.crystalshard.main.util.UserContainer;
import de.kaleidox.crystalshard.util.DiscordUtils;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

public interface Discord extends UserContainer, ChannelContainer, ListenerAttachable<DiscordAttachableListener> {
    String getPrefixedToken();

    int getShardId();

    int getShards();

    ListenerManager<ServerCreateListener> attachServerCreateListener(ServerCreateListener listener);

    DiscordUtils getUtilities();

    Optional<Channel> getChannelById(long id);

    Optional<User> getUserById(long id);

    Self getSelf();

    Optional<Server> getServerById(long id);

    Executor getExecutor();

    ThreadPool getThreadPool();

    default ScheduledExecutorService getScheduler() {
        return getThreadPool().getScheduler();
    }

    Collection<Server> getServers();

    Collection<User> getUsers();

    int getServerCount();

    int getUserCount();

    ListenerManager<MessageCreateListener> attachMessageCreateListener(MessageCreateListener listener);

    ListenerManager<ChannelCreateListener> attachChannelCreateListener(ChannelCreateListener listener);
}
