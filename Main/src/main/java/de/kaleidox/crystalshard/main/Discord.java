package de.kaleidox.crystalshard.main;

import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.core.net.request.ratelimiting.Ratelimiter;
import de.kaleidox.crystalshard.core.net.socket.WebSocketClient;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.main.items.user.Self;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.main.util.ChannelContainer;
import de.kaleidox.crystalshard.main.util.UserContainer;
import de.kaleidox.crystalshard.util.DiscordUtils;
import de.kaleidox.util.markers.IDPair;
import de.kaleidox.util.tunnel.TunnelFramework;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

public interface Discord extends UserContainer, ChannelContainer, ListenerAttachable<DiscordAttachableListener> {
    String getPrefixedToken();

    boolean initFinished();

    int getShardId();

    int getShards();

    DiscordUtils getUtilities(); // TODO: 08.11.2018 Create interface

    Optional<Channel> getChannelById(long id);

    Optional<User> getUserById(long id);

    Self getSelf();

    Optional<Server> getServerById(long id);

    Executor getExecutor();

    WebSocketClient getWebSocket();

    Ratelimiter getRatelimiter();

    Collection<Server> getServers();

    Collection<User> getUsers();

    int getServerCount();

    int getUserCount();

    Cache<Server, Long, Long> getServerCache();

    Cache<User, Long, Long> getUserCache();

    Cache<Role, Long, IDPair> getRoleCache();

    Cache<Channel, Long, Long> getChannelCache();

    Cache<Message, Long, IDPair> getMessageCache();

    Cache<CustomEmoji, Long, IDPair> getEmojiCache();

    default ScheduledExecutorService getScheduler() {
        return getThreadPool().getScheduler();
    }

    ThreadPool getThreadPool();

    TunnelFramework getTunnelFramework();
}
