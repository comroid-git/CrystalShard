package de.kaleidox.crystalshard.main;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.core.net.request.Endpoint;
import de.kaleidox.crystalshard.internal.core.net.request.Method;
import de.kaleidox.crystalshard.internal.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.channel.ServerTextChannelInternal;
import de.kaleidox.crystalshard.main.exception.UncachedItemException;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ServerTextChannel;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.Self;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.main.listener.ServerCreateListener;
import de.kaleidox.crystalshard.util.DiscordUtils;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
}
