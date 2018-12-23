package de.kaleidox.crystalshard.api.entity.server;

import org.intellij.lang.annotations.MagicConstant;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.DiscordItem;
import de.kaleidox.crystalshard.api.entity.Nameable;
import de.kaleidox.crystalshard.api.entity.channel.ChannelStructure;
import de.kaleidox.crystalshard.api.entity.channel.ServerChannel;
import de.kaleidox.crystalshard.api.entity.channel.ServerTextChannel;
import de.kaleidox.crystalshard.api.entity.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.api.entity.permission.PermissionApplyable;
import de.kaleidox.crystalshard.api.entity.permission.PermissionList;
import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.entity.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.api.entity.server.interactive.Integration;
import de.kaleidox.crystalshard.api.entity.user.ServerMember;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.entity.user.presence.Presence;
import de.kaleidox.crystalshard.api.exception.IllegalThreadException;
import de.kaleidox.crystalshard.api.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.api.util.ChannelContainer;
import de.kaleidox.crystalshard.api.util.UserContainer;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.util.annotations.Range;

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

    @MagicConstant(valuesFromClass = VerificationLevel.class)
    int getVerificationLevel();

    @MagicConstant(valuesFromClass = DefaultMessageNotificationLevel.class)
    int getDefaultMessageNotificationLevel();

    @MagicConstant(valuesFromClass = ExplicitContentFilterLevel.class)
    int getExplicitContentFilterLevel();

    Collection<Role> getRoles();

    Collection<CustomEmoji> getCustomEmojis();

    Collection<String> getFeatures();

    @MagicConstant(valuesFromClass = MFALevel.class)
    int getMFALevel();

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

    CompletableFuture<Void> delete();

    CompletableFuture<Void> prune(@Range(min = 1, max = 365) int days);

    CompletableFuture<Collection<Integration>> requestIntegrations();

    CompletableFuture<URL> getVanityUrl();

    default Optional<ServerMember.Updater> getMemberUpdater(User user) {
        if (user instanceof ServerMember) return Optional.of(getMemberUpdater((ServerMember) user));
        else if (getServerMember(user).isPresent()) return Optional.of(getMemberUpdater(getServerMember(user).get()));
        return Optional.empty();
    }

    Optional<ServerMember> getServerMember(User ofUser);

    ServerMember.Updater getMemberUpdater(ServerMember member);

    static Server getFromId(long id) throws IllegalThreadException {
        return getFromId(ThreadPool.getThreadDiscord(), id);
    }

    static Server getFromId(Discord discord, long id) {
        return discord.getServerCache()
                .get(id);
    }

    class ExplicitContentFilterLevel {
        public static final int DISABLED = 0;
        public static final int MEMBERS_WITHOUT_ROLES = 1;
        public static final int ALL_MEMBERS = 2;
    }

    class DefaultMessageNotificationLevel {
        public static final int ALL_MESSAGES = 0;
        public static final int ONLY_MENTIONS = 1;
    }

    class MFALevel {
        public static final int DEACTIVATED = 0;
        public static final int ACTIVATED = 1;
    }

    class VerificationLevel {
        public static final int NONE = 0; // unrestricted
        public static final int LOW = 1; // must have verified email on account
        public static final int MEDIUM = 2; // must be registered on Discord for longer than 5 minutes
        public static final int HIGH = 3; // (╯°□°）╯︵ ┻━┻ - must be a member of the server for longer than 10 minutes
        public static final int VERY_HIGH = 4; // ┻━┻ミヽ(ಠ益ಠ)ﾉ彡┻━┻ - must have a verified phone number

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
