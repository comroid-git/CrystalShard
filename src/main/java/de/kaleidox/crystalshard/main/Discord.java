package de.kaleidox.crystalshard.main;

import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.Self;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.main.util.ChannelContainer;
import de.kaleidox.crystalshard.main.util.UserContainer;
import de.kaleidox.crystalshard.util.DiscordUtils;
import de.kaleidox.util.objects.markers.IDPair;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

public interface Discord extends UserContainer, ChannelContainer, ListenerAttachable<DiscordAttachableListener> {
    String getPrefixedToken();
    
    int getShardId();
    
    int getShards();
    
    DiscordUtils getUtilities();
    
    Optional<Channel> getChannelById(long id);
    
    Optional<User> getUserById(long id);
    
    Self getSelf();
    
    Optional<Server> getServerById(long id);
    
    Executor getExecutor();
    
    ThreadPool getThreadPool();
    
    Collection<Server> getServers();
    
    Collection<User> getUsers();
    
    int getServerCount();
    
    int getUserCount();
    
    Cache<Message, Long, IDPair> getMessageCache();
    
    default ScheduledExecutorService getScheduler() {
        return getThreadPool().getScheduler();
    }
}
