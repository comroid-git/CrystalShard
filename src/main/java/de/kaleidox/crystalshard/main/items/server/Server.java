package de.kaleidox.crystalshard.main.items.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.core.handlers.GUILD_CREATE;
import de.kaleidox.crystalshard.internal.core.net.request.Endpoint;
import de.kaleidox.crystalshard.internal.core.net.request.Method;
import de.kaleidox.crystalshard.internal.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.ChannelContainer;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.UserContainer;
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
import de.kaleidox.crystalshard.main.items.user.presence.PresenceState;

import java.net.URL;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface Server extends DiscordItem, Nameable, UserContainer, ChannelContainer {
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

    Collection<VoiceState> getVoiceStates();

    Collection<ServerMember> getMembers();

    Collection<ServerChannel> getChannels();

    ChannelStructure getChannelStructure();

    Collection<PresenceState> getPresenceStates();

    Optional<User> getUserById(long id);

    CompletableFuture<Void> leave();

    static CompletableFuture<Server> of(Discord discord, long id) {
        return discord.getServerById(id)
                .map(CompletableFuture::completedFuture)
                .orElseGet(() -> new WebRequest<Server>(discord)
                        .method(Method.GET)
                        .endpoint(Endpoint.of(Endpoint.Location.SERVER, id))
                        .execute(data -> new ServerInternal(discord, data)));
    }
}
