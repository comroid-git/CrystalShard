package de.kaleidox.crystalshard.main.items.user;

import de.kaleidox.crystalshard.core.cache.CacheStorable;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.main.handling.listener.user.UserAttachableListener;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.Mentionable;
import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.items.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.util.Castable;
import de.kaleidox.crystalshard.main.util.UserContainer;
import de.kaleidox.util.annotations.Nullable;
import java.net.URL;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static de.kaleidox.crystalshard.core.net.request.Method.*;

public interface User extends DiscordItem, Nameable, Mentionable, MessageReciever, Castable<User>,
        ListenerAttachable<UserAttachableListener>, Cacheable<User, Long, Long>, CacheStorable {
    String getDiscriminatedName();
    
    String getDiscriminator();
    
    Optional<String> getNickname(Server inServer);
    
    String getDisplayName(@Nullable Server inServer);
    
    String getNicknameMentionTag();
    
    Optional<URL> getAvatarUrl();
    
    boolean isBot();
    
    boolean isVerified();
    
    boolean hasMultiFactorAuthorization();
    
    boolean isYourself();
    
    Optional<String> getLocale();
    
    Optional<String> getEmail();
    
    ServerMember toServerMember(Server server);
    
    Collection<Role> getRoles(Server server);
    
    CompletableFuture<PrivateTextChannel> openPrivateChannel();
    
    default Optional<ServerMember> toServerMember() {
        return castTo(ServerMember.class);
    }
    
// Static members
    // Static membe
    static CompletableFuture<User> of(UserContainer in, long id) {
        CompletableFuture<User> userFuture;
        
        if (in instanceof Server) {
            Server srv = (Server) in;
            Discord discord = srv.getDiscord();
            
            userFuture =
                    srv.getUserById(id).map(CompletableFuture::completedFuture).orElseGet(() -> new WebRequest<User>(
                            discord).method(GET).endpoint(Endpoint.of(Endpoint.Location.USER, id)).execute(node -> {
                        if (node.isObject()) {
                            return new UserInternal(discord, node);
                        } else throw new NoSuchElementException("No User with ID " + id + " found!");
                    }));
        } else if (in instanceof Discord) {
            Discord discord = (Discord) in;
            
            userFuture = discord.getUserById(id)
                    .map(CompletableFuture::completedFuture)
                    .orElseGet(() -> new WebRequest<User>(discord).method(GET)
                            .endpoint(Endpoint.of(Endpoint.Location.USER, id))
                            .execute(node -> {
                                if (node.isObject()) {
                                    return new UserInternal(discord, node);
                                } else throw new NoSuchElementException("No User with ID " + id + " found!");
                            }));
        } else {
            throw new IllegalArgumentException(in.getClass().getSimpleName() + " is not a valid UserContainer!");
        }
        
        return userFuture;
    }
    
    /**
     * Gets a user from only an ID. This method requires to be run from a Bot-Own Thread, see {@link
     * ThreadPool#isBotOwnThread()}. This method must only be invoked from "bot-own" threads.
     *
     * @param id The ID to get a user for.
     * @return A future to contain a user with the given id.
     * @throws IllegalCallerException If the current thread does not belong to any {@link Discord} object.
     */
    static CompletableFuture<User> of(long id) {
        CompletableFuture<User> userFuture = new CompletableFuture<>();
        
        if (ThreadPool.isBotOwnThread()) {
            DiscordInternal discord = ((ThreadPool.Worker) Thread.currentThread()).getDiscord();
            userFuture = new WebRequest<User>(discord).method(GET)
                    .endpoint(Endpoint.of(Endpoint.Location.USER, id))
                    .execute(node -> {
                        if (node.isObject()) {
                            return new UserInternal(discord, node);
                        } else throw new NoSuchElementException("No User with ID " + id + " found!");
                    });
        } else userFuture.completeExceptionally(ThreadPool.BOT_THREAD_EXCEPTION.get());
        
        return userFuture;
    }
}
