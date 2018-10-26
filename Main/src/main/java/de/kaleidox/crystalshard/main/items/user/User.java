package de.kaleidox.crystalshard.main.items.user;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.CoreDelegate;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.internal.InternalDelegate;
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
import de.kaleidox.crystalshard.util.annotations.Nullable;

import java.net.URL;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static de.kaleidox.crystalshard.core.net.request.Method.*;

public interface User
        extends DiscordItem, Nameable, Mentionable, MessageReciever, Castable<User>, ListenerAttachable<UserAttachableListener>, Cacheable<User, Long, Long> {
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
    
    ServerMember toServerMember(Server server, JsonNode memberData);
    
    Collection<Role> getRoles(Server server);
    
    CompletableFuture<PrivateTextChannel> openPrivateChannel();
    
    default Optional<ServerMember> toServerMember() {
        return castTo(ServerMember.class);
    }
    
    Optional<ServerMember> toServerMember(Server server);
    
    // Static membe
    static CompletableFuture<User> of(UserContainer in, long id) {
        CompletableFuture<User> userFuture;
        
        if (in instanceof Server) {
            Server srv = (Server) in;
            Discord discord = srv.getDiscord();
            
            userFuture = srv.getUserById(id).map(CompletableFuture::completedFuture).orElseGet(() -> CoreDelegate.webRequest(discord).method(GET)
                    .endpoint(Endpoint.of(Endpoint.Location.USER, id))
                    .execute(node -> {
                        if (node.isObject()) {
                            return InternalDelegate.newInstance(User.class, discord, node);
                        } else throw new NoSuchElementException("No User with ID " + id + " found!");
                    }));
        } else if (in instanceof Discord) {
            Discord discord = (Discord) in;
            
            userFuture = discord.getUserById(id).map(CompletableFuture::completedFuture).orElseGet(() -> CoreDelegate.webRequest(discord).method(GET)
                    .endpoint(Endpoint.of(Endpoint.Location.USER, id))
                    .execute(node -> {
                        if (node.isObject()) {
                            return InternalDelegate.newInstance(User.class, discord, node);
                        } else throw new NoSuchElementException("No User with ID " + id + " found!");
                    }));
        } else {
            throw new IllegalArgumentException(in.getClass().getSimpleName() + " is not a valid UserContainer!");
        }
        
        return userFuture;
    }
}
