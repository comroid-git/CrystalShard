package de.kaleidox.crystalshard.internal.items.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.core.net.request.Endpoint;
import de.kaleidox.crystalshard.internal.core.net.request.Method;
import de.kaleidox.crystalshard.internal.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.channel.ChannelCategoryInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelStructureInternal;
import de.kaleidox.crystalshard.internal.items.channel.ServerTextChannelInternal;
import de.kaleidox.crystalshard.internal.items.channel.ServerVoiceChannelInternal;
import de.kaleidox.crystalshard.internal.items.permission.PermissionListInternal;
import de.kaleidox.crystalshard.internal.items.role.RoleInternal;
import de.kaleidox.crystalshard.internal.items.server.emoji.CustomEmojiInternal;
import de.kaleidox.crystalshard.internal.items.user.ServerMemberInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.internal.items.user.presence.PresenceStateInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.ChannelStructure;
import de.kaleidox.crystalshard.main.items.channel.ChannelType;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerTextChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.DefaultMessageNotificationLevel;
import de.kaleidox.crystalshard.main.items.server.ExplicitContentFilterLevel;
import de.kaleidox.crystalshard.main.items.server.MFALevel;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.VerificationLevel;
import de.kaleidox.crystalshard.main.items.server.VoiceRegion;
import de.kaleidox.crystalshard.main.items.server.VoiceState;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.main.items.user.ServerMember;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.main.items.user.presence.PresenceState;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.helpers.UrlHelper;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ServerInternal implements Server {
    private final static Logger logger = new Logger(ServerInternal.class);
    private final DiscordInternal discord;
    private final long id;
    private final String name;
    private final URL iconUrl;
    private final URL splashUrl;
    private final User owner;
    private final PermissionList ownPermissions;
    private final VoiceRegion voiceRegion;
    private final ServerVoiceChannel afkChannel;
    private final int afkTimeout;
    private final boolean embedEnabled;
    private final ServerChannel embedChannel;
    private final VerificationLevel verificationLevel;
    private final DefaultMessageNotificationLevel defaultMessageNotificationLevel;
    private final ExplicitContentFilterLevel explicitContentFilterLevel;
    private final MFALevel mfaLevel;
    private final boolean widgetable;
    private final ServerChannel widgetChannel;
    private final ServerTextChannel systemChannel;
    private final boolean large;
    private final boolean unavailable;
    private final int memberCount;
    private final ArrayList<Role> roles = new ArrayList<>();
    private final ArrayList<CustomEmoji> emojis = new ArrayList<>();
    private final ArrayList<String> features = new ArrayList<>();
    private final ArrayList<VoiceState> voiceStates = new ArrayList<>();
    private final ArrayList<ServerMember> members = new ArrayList<>();
    private final ArrayList<ServerChannel> channels = new ArrayList<>();
    private final ArrayList<PresenceState> presenceStates = new ArrayList<>();
    private final ChannelStructureInternal structure;

    public ServerInternal(Discord discord, JsonNode data) {
        this.discord = (DiscordInternal) discord;
        id = data.get("id").asLong();
        name = data.get("name").asText();
        iconUrl = UrlHelper.orNull(data.get("icon").asText());
        splashUrl = UrlHelper.orNull(data.get("splashUrl").asText());
        owner = getOwnerPrivate(data);
        ownPermissions = data.has("permissions") ?
                new PermissionListInternal(data.get("permissions").asInt()) : PermissionList.EMPTY_LIST;
        voiceRegion = VoiceRegion.getFromRegionKey(data.get("region").get("id").asText());
        afkChannel = data.has("afk_channel_id") ?
                ServerVoiceChannel.of(discord, data.get("afk_channel_id").asLong()).join() : null;
        afkTimeout = data.get("afk_timeout").asInt(-1);
        embedEnabled = data.path("embed_enabled").asBoolean(false);
        embedChannel = data.has("embed_channel_id") ?
                ServerChannel.of(discord, data.get("embed_channel_id").asLong()).join() : null;
        verificationLevel = VerificationLevel.getFromId(data.get("verification_level").asInt(-1));
        defaultMessageNotificationLevel = DefaultMessageNotificationLevel.getFromId(
                data.get("default_message_notifications").asInt(-1));
        explicitContentFilterLevel = ExplicitContentFilterLevel.getFromId(
                data.get("explicit_content_filter").asInt(-1));
        mfaLevel = MFALevel.getFromId(data.get("mfa_level").asInt(-1));
        widgetable = data.path("widget_enabled").asBoolean(false);
        widgetChannel = data.has("widget_channel_id") ?
                ServerChannel.of(discord, data.get("widget_channel_id").asLong()).join() : null;
        systemChannel = data.has("system_channel_id") ?
                ServerTextChannel.of(discord, data.path("system_channel_id").asLong()).join() : null;
        large = data.path("large").asBoolean(false);
        unavailable = data.path("unavailable").asBoolean(false);
        memberCount = data.path("member_count").asInt(-1);

        data.get("roles").forEach(role -> roles.add(new RoleInternal(discord, this, role)));
        data.get("emojis").forEach(emoji -> emojis.add(new CustomEmojiInternal(getDiscord(), this, emoji)));
        data.get("features").forEach(feature -> features.add(feature.asText()));
        data.path("voice_states").forEach(state -> voiceStates.add(new VoiceState(state)));
        data.path("members").forEach(member -> members.add(new ServerMemberInternal((DiscordInternal) discord, this, member)));
        data.path("channels").forEach(channel -> {
            ChannelType type = ChannelType.getFromId(channel.get("type").asInt(-1));
            switch (type) {
                case UNKNOWN:
                case DM:
                case GROUP_DM:
                    break;
                case GUILD_TEXT:
                    channels.add(new ServerTextChannelInternal(discord, this, channel));
                    break;
                case GUILD_VOICE:
                    channels.add(new ServerVoiceChannelInternal(discord, this, channel));
                    break;
                case GUILD_CATEGORY:
                    channels.add(new ChannelCategoryInternal(discord, this, channel));
                    break;
            }
        });
        data.path("presenceStates").forEach(presence -> presenceStates.add(new PresenceStateInternal(discord, this, presence)));

        structure = new ChannelStructureInternal(channels);
    }

    private User getOwnerPrivate(JsonNode data) {
        if (data.has("application_id")) {
            //return new UserInternal(discord, data.get("application_id").asLong());
            return null;
        } else {
            return ServerMember.of(this, data.get("owner_id").asLong());
        }
    }

    @Override
    public Optional<URL> getIconUrl() {
        return Optional.ofNullable(iconUrl);
    }

    @Override
    public Optional<URL> getSplashUrl() {
        return Optional.ofNullable(splashUrl);
    }

    @Override
    public ServerMember getOwner() {
        return owner.toServerMember(this);
    }

    @Override
    public PermissionList getOwnPermissions() {
        return ownPermissions;
    }

    @Override
    public VoiceRegion getVoiceRegion() {
        return voiceRegion;
    }

    @Override
    public Optional<ServerVoiceChannel> getAfkChannel() {
        return Optional.ofNullable(afkChannel);
    }

    @Override
    public int getAfkTimeout() {
        return afkTimeout;
    }

    @Override
    public boolean isEmbeddable() {
        return embedEnabled;
    }

    @Override
    public Optional<ServerChannel> getEmbedChannel() {
        return Optional.ofNullable(embedChannel);
    }

    @Override
    public boolean isWidgetable() {
        return widgetable;
    }

    @Override
    public Optional<ServerChannel> getWidgetChannel() {
        return Optional.ofNullable(widgetChannel);
    }

    @Override
    public Optional<ServerTextChannel> getSystemChannel() {
        return Optional.ofNullable(systemChannel);
    }

    @Override
    public VerificationLevel getVerificationLevel() {
        return verificationLevel;
    }

    @Override
    public DefaultMessageNotificationLevel getDefaultMessageNotificationLevel() {
        return defaultMessageNotificationLevel;
    }

    @Override
    public ExplicitContentFilterLevel getExplicitContentFilterLevel() {
        return explicitContentFilterLevel;
    }

    @Override
    public Collection<Role> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    @Override
    public Collection<CustomEmoji> getCustomEmojis() {
        return Collections.unmodifiableList(emojis);
    }

    @Override
    public Collection<String> getFeatures() {
        return Collections.unmodifiableList(features);
    }

    @Override
    public MFALevel getMFALevel() {
        return mfaLevel;
    }

    @Override
    public boolean isLarge() {
        return large;
    }

    @Override
    public boolean isUnavailable() {
        return unavailable;
    }

    @Override
    public int getMemberCount() {
        return memberCount;
    }

    @Override
    public Collection<VoiceState> getVoiceStates() {
        return Collections.unmodifiableList(voiceStates);
    }

    @Override
    public Collection<ServerMember> getMembers() {
        return Collections.unmodifiableList(members);
    }

    @Override
    public Collection<ServerChannel> getChannels() {
        return Collections.unmodifiableList(channels);
    }

    @Override
    public ChannelStructure getChannelStructure() {
        return structure;
    }

    @Override
    public Collection<PresenceState> getPresenceStates() {
        return Collections.unmodifiableList(presenceStates);
    }

    @Override
    public Optional<User> getUserById(long id) {
        return Optional.empty();
    }

    @Override
    public CompletableFuture<Void> leave() {
        return new WebRequest<Void>(discord)
                .method(Method.DELETE)
                .endpoint(Endpoint.of(Endpoint.Location.SELF_GUILD, this))
                .execute(node -> null);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Discord getDiscord() {
        return discord;
    }

    @Override
    public String getName() {
        return name;
    }
}
