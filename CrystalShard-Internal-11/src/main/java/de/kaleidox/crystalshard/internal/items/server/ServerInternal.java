package de.kaleidox.crystalshard.internal.items.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.ListenerManagerInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelStructureInternal;
import de.kaleidox.crystalshard.internal.items.permission.PermissionListInternal;
import de.kaleidox.crystalshard.internal.items.server.interactive.IntegrationInternal;
import de.kaleidox.crystalshard.internal.items.server.interactive.ServerMemberUpdater;
import de.kaleidox.crystalshard.internal.items.user.ServerMemberInternal;
import de.kaleidox.crystalshard.internal.items.user.presence.PresenceInternal;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.main.items.channel.ChannelStructure;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerTextChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.main.items.permission.Permission;
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
import de.kaleidox.crystalshard.main.items.server.interactive.Integration;
import de.kaleidox.crystalshard.main.items.server.interactive.Invite;
import de.kaleidox.crystalshard.main.items.user.ServerMember;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.main.items.user.presence.Presence;
import util.helpers.UrlHelper;
import util.objects.functional.Evaluation;

import java.net.MalformedURLException;
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
    private final static ConcurrentHashMap<Long, ServerInternal>                   instances      = new ConcurrentHashMap<>();
    private final static Logger                                                    logger         = new Logger(ServerInternal.class);
    private final        DiscordInternal                                           discord;
    private final        long                                                      id;
    private final        ArrayList<Role>                                           roles          = new ArrayList<>();
    private final        ArrayList<CustomEmoji>                                    emojis         = new ArrayList<>();
    private final        ArrayList<String>                                         features       = new ArrayList<>();
    private final        ArrayList<VoiceState>                                     voiceStates    = new ArrayList<>();
    private final        ArrayList<ServerMember>                                   members        = new ArrayList<>();
    private final        ArrayList<ServerChannel>                                  channels       = new ArrayList<>();
    private final        ArrayList<Presence>                                       presenceStates = new ArrayList<>();
    private final        List<ListenerManager<? extends ServerAttachableListener>> listenerManangers;
    private              Role                                                      everyoneRole;
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
    
    public ServerInternal(Discord discord, JsonNode data) {
        logger.deeptrace("Creating server object for data: " + data.toString());
        this.discord = (DiscordInternal) discord;
        this.id = data.get("id").asLong();
        if (data.has("unavailable") && data.get("unavailable").asBoolean()) {
            this.unavailable = true;
        } else {
            data.path("roles").forEach(role -> roles.add(discord.getRoleCache().getOrCreate(discord, this, role)));
            data.path("emojis").forEach(emoji -> emojis.add(discord.getEmojiCache().getOrCreate(getDiscord(), this, emoji, true)));
            data.path("features").forEach(feature -> features.add(feature.asText()));
            data.path("voice_states").forEach(state -> voiceStates.add(VoiceStateInternal.getInstance(discord, state)));
            data.path("members").forEach(member -> members.add(discord.getUserCache().getOrCreate(discord, member.get("user")).toServerMember(this, member)));
            data.path("channels").forEach(channel -> channels.add(discord.getChannelCache()
                                                                          .getOrCreate(discord, this, channel)
                                                                          .toServerChannel()
                                                                          .orElseThrow(AssertionError::new)));
            data.path("presenceStates").forEach(presence -> presenceStates.add(PresenceInternal.getInstance(discord, presence)));
            structure = new ChannelStructureInternal(channels);
            updateData(data);
            this.everyoneRole = roles.stream()
                    .filter(role -> role.getName().equalsIgnoreCase("@everyone"))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException("No @everyone Role found!"));
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
        return ServerMemberInternal.getInstance(owner, this);
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
        return members.stream().map(user -> ServerMemberInternal.getInstance(user, this)).collect(Collectors.toList());
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
    public Optional<ServerMember> getServerMember(User ofUser) {
        return members.stream().filter(ofUser::equals).map(ServerMember.class::cast).findAny();
    }
    
    @Override
    public CompletableFuture<Void> delete() {
        if (!getOwner().equals(discord.getSelf())) return CompletableFuture.failedFuture(new DiscordPermissionException("You are not the owner of the guild!"));
        return new WebRequest<Void>(discord).method(Method.DELETE).endpoint(Endpoint.of(Endpoint.Location.SELF_GUILD, this)).execute(node -> null);
    }
    
    @Override
    public CompletableFuture<Void> prune(int days) {
        if (days < 1 || days > 365) throw new IllegalArgumentException("Parameter 'days' is not within its bounds! [1,365]");
        if (!hasPermission(discord, Permission.KICK_MEMBERS)) return CompletableFuture.failedFuture(new DiscordPermissionException("Cannot prune!",
                                                                                                                                   Permission.KICK_MEMBERS));
        return new WebRequest<Void>(discord).method(Method.POST).endpoint(Endpoint.Location.GUILD_PRUNE.toEndpoint(id)).node("days", days).executeNull();
    }
    
    @Override
    public CompletableFuture<Collection<Integration>> requestIntegrations() {
        if (!hasPermission(discord, Permission.MANAGE_GUILD)) return CompletableFuture.failedFuture(new DiscordPermissionException(
                "Cannot get guild integrations!",
                Permission.MANAGE_GUILD));
        return new WebRequest<Collection<Integration>>(discord).method(Method.GET)
                .endpoint(Endpoint.Location.GUILD_INTEGRATIONS.toEndpoint(id))
                .execute(node -> {
                    List<Integration> list = new ArrayList<>();
                    for (JsonNode data : node) {
                        list.add(new IntegrationInternal(discord, this, data));
                    }
                    return list;
                });
    }
    
    @Override
    public CompletableFuture<URL> getVanityUrl() {
        if (!hasPermission(discord, Permission.MANAGE_GUILD)) return CompletableFuture.failedFuture(new DiscordPermissionException("Cannot get the vanity URL!",
                                                                                                                                   Permission.MANAGE_GUILD));
        return new WebRequest<URL>(discord).method(Method.GET).endpoint(Endpoint.Location.GUILD_VANITY_INVITE.toEndpoint(id)).execute(node -> {
            if (!node.has("code")) throw new NullPointerException("Guild does not have a vanity URL!");
            try {
                return new URL(Invite.BASE_INVITE + node.get("code").asText());
            } catch (MalformedURLException e) {
                throw new NullPointerException("Could not create URL: " + e);
            }
        });
    }
    
    @Override
    public ServerMember.Updater getMemberUpdater(ServerMember member) {
        return new ServerMemberUpdater(member);
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
        ListenerManagerInternal<ServerAttachableListener> manager = ListenerManagerInternal.getInstance(discord, listener);
        return Evaluation.of(listenerManangers.remove(manager));
    }
    
    @Override
    public Collection<ServerAttachableListener> getAttachedListeners() {
        return listenerManangers.stream().map(ListenerManager::getListener).collect(Collectors.toList());
    }
    
    @Override
    public boolean hasPermission(User user, Permission permission) {
        return members.stream().filter(user::equals).map(usr -> usr.toServerMember().orElseThrow(AssertionError::new)).flatMap(member -> member.getRoles()
                .stream()).sorted().map(Role::getPermissions).anyMatch(perm -> perm.contains(permission));
    }
    
    @Override
    public Cache<Server, Long, Long> getCache() {
        return discord.getServerCache();
    }
    
    private User getOwner(JsonNode data) {
        if (data.has("owner_id")) {
            //return new UserInternal(discord, data.get("application_id").asLong());
            long userId = data.get("owner_id").asLong();
            return discord.getUserCache().getOrRequest(userId, userId);
        } else {
            return null;
        }
    }
    
    public List<ServerAttachableListener> getListeners() {
        return listenerManangers.stream().map(ListenerManager::getListener).collect(Collectors.toList());
    }
    
    public Set<EditTrait<Server>> updateData(JsonNode data) {
        HashSet<EditTrait<Server>> traits = new HashSet<>();
        
        if (name == null || !name.equals(data.path("name").asText(""))) {
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
        if ((ownPermissions != null ? ownPermissions.toPermissionInt() : 0) != data.path("permissions").asInt(0)) {
            ownPermissions = new PermissionListInternal(data.get("permissions").asInt(0));
            traits.add(OWN_PERMISSIONS);
        }
        if (voiceRegion == null || !voiceRegion.getRegionKey().equalsIgnoreCase(data.path("region").asText(voiceRegion.getRegionKey()))) {
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
        if (verificationLevel == null || verificationLevel.getId() != data.path("verification_level").asInt(-1)) {
            verificationLevel = VerificationLevel.getFromId(data.get("verification_level").asInt(-1));
            traits.add(VERIFICATION_LEVEL);
        }
        if (defaultMessageNotificationLevel == null || defaultMessageNotificationLevel.getId() != data.path("default_message_notification").asInt(
                defaultMessageNotificationLevel.getId())) {
            defaultMessageNotificationLevel = DefaultMessageNotificationLevel.getFromId(data.get("default_message_notifications").asInt(-1));
            traits.add(DEFAULT_MESSAGE_NOTIFICATION_LEVEL);
        }
        if (explicitContentFilterLevel == null || explicitContentFilterLevel.getId() != data.path("explicit_content_filter").asInt(-1)) {
            explicitContentFilterLevel = ExplicitContentFilterLevel.getFromId(data.get("explicit_content_filter").asInt(-1));
            traits.add(EXPLICIT_CONTENT_FILTER_LEVEL);
        }
        if (mfaLevel == null || mfaLevel.getId() != data.path("mfa_level").asInt(-1)) {
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
        
        long afkChannelId = data.path("afk_channel_id").asLong(-1);
        if (afkChannel == null || afkChannel.getId() != afkChannelId) {
            afkChannel = afkChannelId == -1 ? null : discord.getChannelCache().getOrRequest(afkChannelId, afkChannelId).toServerVoiceChannel().orElseThrow(
                    AssertionError::new);
            traits.add(AFK_CHANNEL);
        }
        if (embedChannel == null || embedChannel.getId() != data.path("embed_channel_id").asLong(-1)) {
            long embedChannelId = data.path("embed_channel_id").asLong(-1);
            embedChannel = embedChannelId == -1 ? null : discord.getChannelCache().getOrRequest(embedChannelId, embedChannelId).toServerChannel().orElseThrow(
                    AssertionError::new);
            traits.add(EMBED_CHANNEL);
        }
        if (widgetChannel == null || widgetChannel.getId() != data.path("widget_channel_id").asLong(-1)) {
            long widgetChannelId = data.path("widget_channel_id").asLong(-1);
            widgetChannel = widgetChannelId == -1 ? null : discord.getChannelCache()
                    .getOrRequest(widgetChannelId, widgetChannelId)
                    .toServerChannel()
                    .orElseThrow(AssertionError::new);
            traits.add(WIDGET_CHANNEL);
        }
        if (systemChannel == null || systemChannel.getId() != data.get("system_channel_id").asLong(-1)) {
            long systemChannelId = data.path("system_channel_id").asLong(-1);
            systemChannel = systemChannelId == -1 ? null : discord.getChannelCache()
                    .getOrRequest(systemChannelId, systemChannelId)
                    .toServerTextChannel()
                    .orElseThrow(AssertionError::new);
            traits.add(SYSTEM_CHANNEL);
        }
        if ((owner != null ? owner.getId() : -1) != data.path((data.has("owner_id") ? "owner_id" : "application_id")).asLong(-1)) {
            if (data.has("owner_id")) {
                //return new UserInternal(discord, data.get("application_id").asLong());
                long userId = data.get("owner_id").asLong();
                owner = discord.getUserCache().getOrRequest(userId, userId);
            } else {
                return null;
            }
            traits.add(OWNER);
        }
        return traits;
    }
    
    public void replaceEmojis(List<CustomEmoji> newEmojis) {
        emojis.clear();
        emojis.addAll(newEmojis);
    }
    
    public void addUser(ServerMember user) {
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
}
