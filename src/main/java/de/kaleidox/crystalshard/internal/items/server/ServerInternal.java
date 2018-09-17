package de.kaleidox.crystalshard.internal.items.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.ListenerManagerInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelCategoryInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelStructureInternal;
import de.kaleidox.crystalshard.internal.items.channel.ServerTextChannelInternal;
import de.kaleidox.crystalshard.internal.items.channel.ServerVoiceChannelInternal;
import de.kaleidox.crystalshard.internal.items.permission.PermissionListInternal;
import de.kaleidox.crystalshard.internal.items.role.RoleInternal;
import de.kaleidox.crystalshard.internal.items.server.emoji.CustomEmojiInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.internal.items.user.presence.PresenceInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;
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
import de.kaleidox.crystalshard.main.items.user.presence.Presence;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.helpers.UrlHelper;
import de.kaleidox.util.objects.Evaluation;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static de.kaleidox.crystalshard.main.handling.editevent.enums.ServerEditTrait.*;

public class ServerInternal implements Server {
    private final static ConcurrentHashMap<Long, ServerInternal>                   instances      =
            new ConcurrentHashMap<>();
    private final static Logger                                                    logger         = new Logger(
            ServerInternal.class);
    private final        DiscordInternal                                           discord;
    private final        long                                                      id;
    private final        Role                                                      everyoneRole;
    private final        ArrayList<Role>                                           roles          = new ArrayList<>();
    private final        ArrayList<CustomEmoji>                                    emojis         = new ArrayList<>();
    private final        ArrayList<String>                                         features       = new ArrayList<>();
    private final        ArrayList<VoiceState>                                     voiceStates    = new ArrayList<>();
    private final        ArrayList<User>                                           members        = new ArrayList<>();
    private final        ArrayList<ServerChannel>                                  channels       = new ArrayList<>();
    private final        ArrayList<Presence>                                       presenceStates = new ArrayList<>();
    private final        List<ListenerManager<? extends ServerAttachableListener>> listenerManangers;
    private              String                                                    name;
    private              URL                                                       iconUrl;
    private              URL                                                       splashUrl;
    private              User                                                      owner;
    private              PermissionList                                            ownPermissions;
    private              VoiceRegion                                               voiceRegion;
    private              ServerVoiceChannel                                        afkChannel;
    private              int                                                       afkTimeout;
    private              boolean                                                   embedEnabled;
    private              ServerChannel                                             embedChannel;
    private              VerificationLevel                                         verificationLevel;
    private              DefaultMessageNotificationLevel                           defaultMessageNotificationLevel;
    private              ExplicitContentFilterLevel                                explicitContentFilterLevel;
    private              MFALevel                                                  mfaLevel;
    private              boolean                                                   widgetEnabled;
    private              ServerChannel                                             widgetChannel;
    private              ServerTextChannel                                         systemChannel;
    private              boolean                                                   large;
    private              boolean                                                   unavailable;
    private              int                                                       memberCount;
    private              ChannelStructureInternal                                  structure;
    
    private ServerInternal(Discord discord, JsonNode data) {
        logger.deeptrace("Creating server object for data: " + data.toString());
        this.discord = (DiscordInternal) discord;
        id = data.get("id").asLong();
        name = data.get("name").asText();
        iconUrl = UrlHelper.orNull(data.path("icon").asText(null));
        splashUrl = UrlHelper.orNull(data.path("splashUrl").asText(null));
        owner = getOwnerPrivate(data);
        ownPermissions = data.has("permissions") ? new PermissionListInternal(data.get("permissions").asInt()) :
                         PermissionList.EMPTY_LIST;
        voiceRegion = VoiceRegion.getFromRegionKey(data.path("region").asText(null));
        afkTimeout = data.get("afk_timeout").asInt(-1);
        embedEnabled = data.path("embed_enabled").asBoolean(false);
        verificationLevel = VerificationLevel.getFromId(data.get("verification_level").asInt(-1));
        defaultMessageNotificationLevel = DefaultMessageNotificationLevel.getFromId(data.get(
                "default_message_notifications").asInt(-1));
        explicitContentFilterLevel = ExplicitContentFilterLevel.getFromId(data.get("explicit_content_filter")
                                                                                  .asInt(-1));
        mfaLevel = MFALevel.getFromId(data.get("mfa_level").asInt(-1));
        widgetEnabled = data.path("widget_enabled").asBoolean(false);
        large = data.path("large").asBoolean(false);
        unavailable = data.path("unavailable").asBoolean(false);
        memberCount = data.path("member_count").asInt(-1);
        
        data.path("roles").forEach(role -> roles.add(RoleInternal.getInstance(this, role)));
        data.path("emojis").forEach(emoji -> emojis.add(CustomEmojiInternal.getInstance(getDiscord(),
                                                                                        this,
                                                                                        emoji,
                                                                                        true)));
        data.path("features").forEach(feature -> features.add(feature.asText()));
        data.path("voice_states").forEach(state -> voiceStates.add(VoiceStateInternal.getInstance(discord, state)));
        data.path("members").forEach(member -> members.add(UserInternal.getInstance(discord, member)
                                                                   .toServerMember(this)));
        data.path("channels").forEach(channel -> {
            ChannelType type = ChannelType.getFromId(channel.get("type").asInt(-1));
            switch (type) {
                case UNKNOWN:
                case DM:
                case GROUP_DM:
                    break;
                case GUILD_TEXT:
                    channels.add(ServerTextChannelInternal.getInstance(discord, this, channel));
                    break;
                case GUILD_VOICE:
                    channels.add(ServerVoiceChannelInternal.getInstance(discord, this, channel));
                    break;
                case GUILD_CATEGORY:
                    channels.add(ChannelCategoryInternal.getInstance(discord, this, channel));
                    break;
            }
        });
        data.path("presenceStates").forEach(presence -> presenceStates.add(PresenceInternal.getInstance(discord,
                                                                                                        presence)));
        structure = new ChannelStructureInternal(channels);
        
        everyoneRole = roles.stream()
                .filter(role -> role.getName().equalsIgnoreCase("@everyone"))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("No @everyone role found for " + this));
        
        long afk_channel_id = data.path("afk_channel_id").asLong(-1);
        if (afk_channel_id != -1) {
            afkChannel = ChannelInternal.getInstance(discord, afk_channel_id).toServerVoiceChannel().orElse(null);
        }
        long embed_channel_id = data.path("embed_channel_id").asLong(-1);
        if (embed_channel_id != -1) {
            embedChannel = ChannelInternal.getInstance(discord, embed_channel_id).toServerChannel().orElse(null);
        }
        long widget_channel_id = data.path("widget_channel_id").asLong(-1);
        if (widget_channel_id != -1) {
            widgetChannel = ChannelInternal.getInstance(discord, widget_channel_id).toServerChannel().orElse(null);
        }
        long system_channel_id = data.path("system_channel_id").asLong(-1);
        if (system_channel_id != -1) {
            systemChannel = ChannelInternal.getInstance(discord, system_channel_id).toServerTextChannel().orElse(null);
        }
        
        listenerManangers = new ArrayList<>();
        
        instances.put(id, this);
    }
    
// Override Methods
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
        return widgetEnabled;
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
    public List<Role> getRoles() {
        return Collections.unmodifiableList(roles);
    }
    
    @Override
    public List<CustomEmoji> getCustomEmojis() {
        return Collections.unmodifiableList(emojis);
    }
    
    @Override
    public List<String> getFeatures() {
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
    public Role getEveryoneRole() {
        return everyoneRole;
    }
    
    @Override
    public List<VoiceState> getVoiceStates() {
        return Collections.unmodifiableList(voiceStates);
    }
    
    @Override
    public List<ServerMember> getMembers() {
        return members.stream().map(user -> {
            return user.toServerMember(this);
        }).collect(Collectors.toList());
    }
    
    @Override
    public List<ServerChannel> getChannels() {
        return Collections.unmodifiableList(channels);
    }
    
    @Override
    public ChannelStructure getChannelStructure() {
        return structure;
    }
    
    @Override
    public List<Presence> getPresenceStates() {
        return Collections.unmodifiableList(presenceStates);
    }
    
    @Override
    public Optional<User> getUserById(long id) {
        return Optional.empty();
    }
    
    @Override
    public CompletableFuture<Void> leave() {
        return new WebRequest<Void>(discord).method(Method.DELETE).endpoint(Endpoint.of(Endpoint.Location.SELF_GUILD,
                                                                                        this)).execute(node -> null);
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
    
    @Override
    public String toString() {
        return "Server with ID [" + id + "]";
    }
    
    @Override
    public <C extends ServerAttachableListener> ListenerManager<C> attachListener(C listener) {
        ListenerManagerInternal<C> manager = ListenerManagerInternal.getInstance(discord, listener);
        listenerManangers.add(manager);
        return manager;
    }
    
    @Override
    public Evaluation<Boolean> detachListener(ServerAttachableListener listener) {
        ListenerManagerInternal<ServerAttachableListener> manager = ListenerManagerInternal.getInstance(discord,
                                                                                                        listener);
        return Evaluation.of(listenerManangers.remove(manager));
    }
    
    @Override
    public Collection<ServerAttachableListener> getAttachedListeners() {
        return listenerManangers.stream().map(ListenerManager::getListener).collect(Collectors.toList());
    }
    
    private User getOwnerPrivate(JsonNode data) {
        if (data.has("owner_id")) {
            //return new UserInternal(discord, data.get("application_id").asLong());
            return UserInternal.getInstance(discord, data.get("owner_id").asLong());
        } else {
            return null;
        }
    }
    
    public List<ServerAttachableListener> getListeners() {
        return listenerManangers.stream().map(ListenerManager::getListener).collect(Collectors.toList());
    }
    
    public Set<EditTrait<Server>> updateData(JsonNode data) {
        HashSet<EditTrait<Server>> traits = new HashSet<>();
        
        if (!name.equals(data.path("name").asText(""))) {
            name = data.get("name").asText();
            traits.add(NAME);
        }
        if (iconUrl != null && !UrlHelper.equals(iconUrl, data.path("icon").asText(null))) {
            iconUrl = UrlHelper.orNull(data.path("icon").asText(null));
            traits.add(ICON);
        }
        if (splashUrl != null && !UrlHelper.equals(splashUrl, data.path("splash_url").asText(null))) {
            splashUrl = UrlHelper.ignoreIfNull(data.path("splashUrl").asText(null));
            traits.add(SPLASH);
        }
        if ((owner != null ? owner.getId() : -1) != data.path((data.has("owner_id") ? "owner_id" : "application_id"))
                .asLong(-1)) {
            owner = getOwnerPrivate(data);
            traits.add(OWNER);
        }
        if ((ownPermissions != null ? ownPermissions.toPermissionInt() : 0) != data.path("permissions").asInt(0)) {
            ownPermissions = new PermissionListInternal(data.get("permissions").asInt(0));
            traits.add(OWN_PERMISSIONS);
        }
        if (!voiceRegion.getRegionKey().equalsIgnoreCase(data.path("region").asText(voiceRegion.getRegionKey()))) {
            voiceRegion = VoiceRegion.getFromRegionKey(data.path("region").asText(""));
            traits.add(REGION);
        }
        if (afkTimeout != data.path("afk_timeout").asInt(-1)) {
            afkTimeout = data.get("afk_timeout").asInt(-1);
            traits.add(AFK_TIMEOUT);
        }
        if (embedEnabled != data.path("embed_enabled").asBoolean(embedEnabled)) {
            embedEnabled = data.path("embed_enabled").asBoolean(false);
            traits.add(EMBED_ENABLED);
        }
        if (verificationLevel.getId() != data.path("verification_level").asInt(-1)) {
            verificationLevel = VerificationLevel.getFromId(data.get("verification_level").asInt(-1));
            traits.add(VERIFICATION_LEVEL);
        }
        if (defaultMessageNotificationLevel.getId() != data.path("default_message_notification").asInt(
                defaultMessageNotificationLevel.getId())) {
            defaultMessageNotificationLevel = DefaultMessageNotificationLevel.getFromId(data.get(
                    "default_message_notifications").asInt(-1));
            traits.add(DEFAULT_MESSAGE_NOTIFICATION_LEVEL);
        }
        if (explicitContentFilterLevel.getId() != data.path("explicit_content_filter").asInt(-1)) {
            explicitContentFilterLevel = ExplicitContentFilterLevel.getFromId(data.get("explicit_content_filter")
                                                                                      .asInt(-1));
            traits.add(EXPLICIT_CONTENT_FILTER_LEVEL);
        }
        if (mfaLevel.getId() != data.path("mfa_level").asInt(-1)) {
            mfaLevel = MFALevel.getFromId(data.get("mfa_level").asInt(-1));
            traits.add(MFA_LEVEL);
        }
        if (widgetEnabled != data.path("widget_enabled").asBoolean(widgetEnabled)) {
            widgetEnabled = data.path("widget_enabled").asBoolean(false);
            traits.add(WIDGET_ENABLED);
        }
        if (large != data.path("large").asBoolean(false)) {
            large = data.path("large").asBoolean(false);
            traits.add(LARGE);
        }
        if (unavailable != data.path("unavailable").asBoolean(false)) {
            unavailable = data.path("unavailable").asBoolean(false);
            traits.add(AVAILABILITY);
        }
        if (memberCount != data.path("member_count").asInt(memberCount)) {
            memberCount = data.path("member_count").asInt(-1);
            traits.add(MEMBER_COUNT);
        }
        
        if ((afkChannel != null ? afkChannel.getId() : -1) != data.path("afk_channel_id").asLong(-1)) {
            afkChannel = ServerVoiceChannelInternal.getInstance(discord, data.path("afk_channel_id").asLong(-1))
                    .toServerVoiceChannel()
                    .orElseThrow(AssertionError::new);
            traits.add(AFK_CHANNEL);
        }
        if ((embedChannel != null ? embedChannel.getId() : -1) != data.path("embed_channel_id").asLong(-1)) {
            embedChannel = ChannelInternal.getInstance(discord, data.path("embed_channel_id").asLong(-1))
                    .toServerChannel()
                    .orElseThrow(AssertionError::new);
            traits.add(EMBED_CHANNEL);
        }
        if ((widgetChannel != null ? widgetChannel.getId() : -1) != data.path("widget_channel_id").asLong(-1)) {
            widgetChannel = ChannelInternal.getInstance(discord, data.path("widget_channel_id").asLong(-1))
                    .toServerChannel()
                    .orElseThrow(AssertionError::new);
            traits.add(WIDGET_CHANNEL);
        }
        if ((systemChannel != null ? systemChannel.getId() : -1) != data.get("system_channel_id").asLong(-1)) {
            systemChannel = ServerTextChannelInternal.getInstance(discord, data.path("system_channel_id").asLong(-1))
                    .toServerTextChannel()
                    .orElseThrow(AssertionError::new);
            traits.add(SYSTEM_CHANNEL);
        }
        return traits;
    }
    
    public void replaceEmojis(List<CustomEmoji> newEmojis) {
        emojis.clear();
        emojis.addAll(newEmojis);
    }
    
    public void addUser(User user) {
        members.add(user);
    }
    
    public void removeUser(User user) {
        members.remove(user);
    }
    
    public void addRole(Role role) {
        roles.add(role);
    }
    
    public void removeRole(Role role) {
        roles.remove(role);
    }
    
// Static membe
    public static Optional<Server> getInstance(long id) {
        return Optional.ofNullable(instances.getOrDefault(id, null));
    }
    
    public static Server getInstance(Discord discord, long id) {
        return discord.getServers()
                .stream()
                .filter(server -> server.getId() == id)
                .findAny()
                .orElseGet(() -> new WebRequest<Server>(discord).method(Method.GET)
                        .endpoint(Endpoint.Location.GUILD_SPECIFIC.toEndpoint(id))
                        .execute(node -> getInstance(discord, node))
                        .join());
    }
    
    public static Server getInstance(Discord discord, JsonNode data) {
        long id = data.get("id").asLong(-1);
        if (id == -1) throw new NoSuchElementException("No valid ID found.");
        if (instances.containsKey(id)) return instances.get(id);
        else {
            return new ServerInternal(discord, data);
        }
    }
}
