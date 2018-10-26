package de.kaleidox.crystalshard.main.items.server;

import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.main.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.items.channel.ChannelStructure;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerTextChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.main.items.permission.PermissionApplyable;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.main.items.server.interactive.Integration;
import de.kaleidox.crystalshard.main.items.user.ServerMember;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.main.items.user.presence.Presence;
import de.kaleidox.crystalshard.main.util.ChannelContainer;
import de.kaleidox.crystalshard.main.util.UserContainer;
import de.kaleidox.crystalshard.util.annotations.Range;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Server
        extends DiscordItem, Nameable, UserContainer, ChannelContainer, ListenerAttachable<ServerAttachableListener>, Cacheable<Server, Long, Long>,
        PermissionApplyable {
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
    
    Optional<ServerMember> getServerMember(User ofUser);
    
    CompletableFuture<Void> delete();
    
    CompletableFuture<Void> prune(@Range(min = 1, max = 365) int days);
    
    CompletableFuture<Collection<Integration>> requestIntegrations();
    
    CompletableFuture<URL> getVanityUrl();
    
    ServerMember.Updater getMemberUpdater(ServerMember member);
    
    default Optional<ServerMember.Updater> getMemberUpdater(User user) {
        if (user instanceof ServerMember) return Optional.of(getMemberUpdater((ServerMember) user));
        else if (getServerMember(user).isPresent()) return Optional.of(getMemberUpdater(getServerMember(user).get()));
        return Optional.empty();
    }
    
    interface Builder {
        Builder setName(String name);
        
        Builder setRegion(VoiceRegion region);
        
        Builder setIcon(File icon);
        
        Builder setVerificationLevel(VerificationLevel level);
        
        Builder setDefaultNotificationLevel(DefaultMessageNotificationLevel level);
        
        Builder setExplicitContentFilter(ExplicitContentFilterLevel level);
        
        Builder addRole(Role.Builder roleBuilder);
        
        Builder addChannel(ServerChannel.Builder channelBuilder);
        
        Builder add(ServerComponent component);
        
        CompletableFuture<Server> build();
    }
}
