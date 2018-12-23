package de.kaleidox.crystalshard.api.entity.user;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.exception.IllegalThreadException;
import de.kaleidox.crystalshard.api.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.api.handling.listener.user.UserAttachableListener;
import de.kaleidox.crystalshard.api.entity.DiscordItem;
import de.kaleidox.crystalshard.api.entity.Mentionable;
import de.kaleidox.crystalshard.api.entity.Nameable;
import de.kaleidox.crystalshard.api.entity.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.api.entity.message.MessageReciever;
import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.util.Castable;

import java.net.URL;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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

    static User getFromId(long id) throws IllegalThreadException {
        return getFromId(ThreadPool.getThreadDiscord(), id);
    }

    static User getFromId(Discord discord, long id) {
        return discord.getUserCache()
                .get(id);
    }
}
