package de.kaleidox.crystalshard.main.items.server;

import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.items.channel.ChannelStructure;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerTextChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.main.items.user.ServerMember;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.main.items.user.presence.Presence;
import de.kaleidox.crystalshard.main.util.ChannelContainer;
import de.kaleidox.crystalshard.main.util.UserContainer;
import de.kaleidox.util.CompletableFutureExtended;

import java.net.URL;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Server extends DiscordItem, Nameable, UserContainer, ChannelContainer,
        ListenerAttachable<ServerAttachableListener> {
    static CompletableFuture<Server> of(Discord discord, long id) {
        CompletableFutureExtended<Server> future = new CompletableFutureExtended<>(discord.getThreadPool());
        discord.getServerById(id).ifPresentOrElse(future::complete,
                () -> future.completeExceptionally(new NoSuchElementException("Server is not available.")));
        return future;
    }

    Optional<URL> getIconUrl();

    Optional<URL> getSplashUrl();

    ServerMember getOwner();

    PermissionList getOwnPermissions();

    VoiceRegion getVoiceRegion();

    Optional<ServerVoiceChannel> getAfkChannel();

    int getAfkTimeout();

    boolean isEmbeddable();

    Optional<ServerChannel> getEmbedChannel();

    boolean isWidgetable();

    Optional<ServerChannel> getWidgetChannel();

    Optional<ServerTextChannel> getSystemChannel();

    VerificationLevel getVerificationLevel();

    DefaultMessageNotificationLevel getDefaultMessageNotificationLevel();

    ExplicitContentFilterLevel getExplicitContentFilterLevel();

    Collection<Role> getRoles();

    Collection<CustomEmoji> getCustomEmojis();

    Collection<String> getFeatures();

    MFALevel getMFALevel();

    boolean isLarge();

    boolean isUnavailable();

    int getMemberCount();

    Role getEveryoneRole();

    Collection<VoiceState> getVoiceStates();

    Collection<ServerMember> getMembers();

    Collection<ServerChannel> getChannels();

    ChannelStructure getChannelStructure();

    Collection<Presence> getPresenceStates();

    Optional<User> getUserById(long id);

    CompletableFuture<Void> leave();
}
