package de.kaleidox.crystalshard.internal.items.user;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.CoreDelegate;
import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.ListenerManagerInternal;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.user.UserAttachableListener;
import de.kaleidox.crystalshard.main.items.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.Sendable;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.ServerMember;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.util.helpers.NullHelper;
import de.kaleidox.util.helpers.UrlHelper;
import de.kaleidox.util.objects.functional.Evaluation;

import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static de.kaleidox.crystalshard.main.handling.editevent.enums.UserEditTrait.*;
import static de.kaleidox.util.helpers.JsonHelper.objectNode;

@SuppressWarnings("unused")
public class UserInternal implements User {
    private final static Logger logger = new Logger(User.class);
    private final static ConcurrentHashMap<Long, User> instances = new ConcurrentHashMap<>();
    private final long id;
    private final boolean bot;
    private final Discord discord;
    private final List<ListenerManager<? extends UserAttachableListener>> listenerManagers;
    String discriminator;
    private String name;
    private URL avatarUrl;
    private boolean mfa;
    private boolean verified;
    private String locale;
    private String email;

    UserInternal(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.discriminator = user.getDiscriminator();
        this.avatarUrl = user.getAvatarUrl()
                .orElse(null);
        this.bot = user.isBot();
        this.mfa = user.hasMultiFactorAuthorization();
        this.verified = user.isVerified();
        this.locale = user.getLocale()
                .orElse(null);
        this.email = user.getEmail()
                .orElse(null);
        this.discord = user.getDiscord();
        this.listenerManagers = ((UserInternal) user).listenerManagers;
    }

    public UserInternal(Discord discord, JsonNode data) {
        logger.deeptrace("Creating user object for data: " + data.toString());
        this.discord = discord;
        this.id = data.get("id")
                .asLong();
        this.name = data.path("username")
                .asText(null);
        this.discriminator = data.get("discriminator")
                .asText();
        this.avatarUrl = data.has("avatar_url") ? UrlHelper.orNull(data.get("avatar_url")
                .asText()) : null;
        this.bot = data.path("bot")
                .asBoolean(false);
        //noinspection SimplifiableConditionalExpression
        this.mfa = data.has("mfa") ? data.get("mfa_enabled")
                .asBoolean(false) : false;
        this.locale = data.has("locale") ? data.get("locale")
                .asText(null) : null;
        //noinspection SimplifiableConditionalExpression
        this.verified = data.has("verified") ? data.get("verified")
                .asBoolean(false) : false;
        this.email = data.has("email") ? data.get("email")
                .asText(null) : null;
        listenerManagers = new ArrayList<>();

        NullHelper.requireNonNull(name, discriminator);
        instances.putIfAbsent(id, this);
    }

    // Override Methods
    @Override
    public String getDiscriminatedName() {
        return name + "#" + discriminator;
    }

    @Override
    public String getDiscriminator() {
        return discriminator;
    }

    @Override
    public Optional<String> getNickname(Server inServer) {
        // todo This
        return null;
    }

    @Override
    public String getDisplayName(Server inServer) {
        if (inServer == null) return getName();
        return getNickname(inServer).orElseGet(this::getName);
    }

    @Override
    public String getNicknameMentionTag() {
        return "<@!" + id + ">";
    }

    @Override
    public Optional<URL> getAvatarUrl() {
        return Optional.ofNullable(avatarUrl);
    }

    @Override
    public boolean isBot() {
        return bot;
    }

    @Override
    public boolean isVerified() {
        return verified;
    }

    @Override
    public boolean hasMultiFactorAuthorization() {
        return mfa;
    }

    @Override
    public boolean isYourself() {
        return equals(discord.getSelf());
    }

    @Override
    public Optional<String> getLocale() {
        return Optional.ofNullable(locale);
    }

    @Override
    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    @Override
    public Collection<Role> getRoles(Server server) {
        return Collections.emptyList();
    }

    @Override
    public CompletableFuture<PrivateTextChannel> openPrivateChannel() {
        return CoreDelegate.webRequest(PrivateTextChannel.class, discord)
                .setMethod(HttpMethod.POST)
                .setUri(DiscordEndpoint.SELF_CHANNELS.createUri())
                .setNode(objectNode(
                        "recipient_id",
                        id))
                .executeAs(node -> discord.getChannelCache()
                        .getOrCreate(discord, node)
                        .toPrivateTextChannel()
                        .orElseThrow(AssertionError::new));
    }

    @Override
    public Optional<ServerMember> toServerMember(Server server) {
        return server.getMembers()
                .stream()
                .filter(usr -> usr.getId() == id)
                .findAny();
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
    public String getMentionTag() {
        return "<@" + id + ">";
    }

    @Override
    public CompletableFuture<Message> sendMessage(Sendable content) {
        return null;
    }

    @Override
    public CompletableFuture<Message> sendMessage(Consumer<Embed.Builder> defaultEmbedModifier) {
        return null;
    }

    @Override
    public CompletableFuture<Message> sendMessage(EmbedDraft embedDraft) {
        return null;
    }

    @Override
    public CompletableFuture<Message> sendMessage(String content) {
        return null;
    }

    @Override
    public CompletableFuture<Void> typing() {
        return null;
    }

    @Override
    public CompletableFuture<Collection<Message>> getMessages(int limit) {
        if (limit < 1 || limit > 100)
            throw new IllegalArgumentException("Parameter 'limit' is not within its bounds! [1, 100]");
        WebRequest<Collection<Message>> request = CoreDelegate.webRequest(discord);
        return openPrivateChannel().thenCompose(ptc -> request.setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.MESSAGE.createUri(ptc))
                .setNode(objectNode("limit", limit))
                .executeAs(data -> {
                    List<Message> list = new ArrayList<>();
                    data.forEach(msg -> list.add(discord.getMessageCache()
                            .getOrCreate(discord, msg)));
                    return list;
                }));
    }

    @Override
    public String toString() {
        return "User with ID [" + id + "]";
    }

    @Override
    public <C extends UserAttachableListener> ListenerManager<C> attachListener(C listener) {
        ListenerManagerInternal<C> manager = ListenerManagerInternal.getInstance((DiscordInternal) discord, listener);
        listenerManagers.add(manager);
        return manager;
    }

    @Override
    public Evaluation<Boolean> detachListener(UserAttachableListener listener) {
        return null;
    }

    @Override
    public Collection<UserAttachableListener> getAttachedListeners() {
        return null;
    }

    @Override
    public Collection<ListenerManager<? extends UserAttachableListener>> getListenerManagers() {
        return listenerManagers;
    }

    @Override
    public ServerMember toServerMember(Server server, JsonNode data) {
        if (this instanceof ServerMember) return (ServerMember) this;
        return ServerMemberInternal.getInstance(this, server, data);
    }

    @Override
    public Cache<User, Long, Long> getCache() {
        return discord.getUserCache();
    }

    public Set<EditTrait<User>> updateData(JsonNode data) {
        Set<EditTrait<User>> traits = new HashSet<>();

        if (!name.equals(data.path("name")
                .asText(name))) {
            name = data.get("name")
                    .asText();
            traits.add(USERNAME);
        }
        if (!discriminator.equals(data.path("discriminator")
                .asText(discriminator))) {
            discriminator = data.get("discriminator")
                    .asText();
            traits.add(DISCRIMINATOR);
        }
        if (!NullHelper.orDefault(locale, "")
                .equals(data.path("avatar_url")
                        .asText(NullHelper.orDefault(locale, "")))) {
            avatarUrl = UrlHelper.orNull(data.get("avatar_url")
                    .asText());
            traits.add(AVATAR);
        }
        if (mfa != data.path("mfa_enabled")
                .asBoolean(mfa)) {
            mfa = data.get("mfa_enabled")
                    .asBoolean();
            traits.add(MFA_STATE);
        }
        if (verified != data.path("verified")
                .asBoolean(verified)) {
            verified = data.get("verified")
                    .asBoolean();
            traits.add(VERIFIED_STATE);
        }
        if (!NullHelper.orDefault(locale, "")
                .equals(data.path("locale")
                        .asText(NullHelper.orDefault(locale, "")))) {
            locale = data.get("locale")
                    .asText();
            traits.add(LOCALE);
        }
        if (!NullHelper.orDefault(email, "")
                .equals(data.path("email")
                        .asText(NullHelper.orDefault(email, "")))) {
            email = data.get("email")
                    .asText();
            traits.add(EMAIL);
        }

        return traits;
    }
}
