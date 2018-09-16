package de.kaleidox.crystalshard.internal.items.user;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.channel.PrivateTextChannelInternal;
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
import de.kaleidox.logging.Logger;
import de.kaleidox.util.helpers.JsonHelper;
import de.kaleidox.util.helpers.NullHelper;
import de.kaleidox.util.helpers.UrlHelper;
import de.kaleidox.util.objects.Evaluation;

import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static de.kaleidox.crystalshard.main.handling.editevent.enums.UserEditTrait.*;

@SuppressWarnings("unused")
public class UserInternal implements User {
    private final static Logger logger = new Logger(User.class);
    private final static ConcurrentHashMap<Long, User> instances = new ConcurrentHashMap<>();
    private final long id;
    private final boolean bot;
    private final Discord discord;
    private final List<ListenerManager<? extends UserAttachableListener>> listenerManagers;
    private String name;
    private String discriminator;
    private URL avatarUrl;
    private boolean mfa;
    private boolean verified;
    private String locale;
    private String email;

    UserInternal(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.discriminator = user.getDiscriminator();
        this.avatarUrl = user.getAvatarUrl().orElse(null);
        this.bot = user.isBot();
        this.mfa = user.hasMultiFactorAuthorization();
        this.verified = user.isVerified();
        this.locale = user.getLocale().orElse(null);
        this.email = user.getEmail().orElse(null);
        this.discord = user.getDiscord();
        this.listenerManagers = ((UserInternal) user).listenerManagers;
    }

    public UserInternal(Discord discord, JsonNode data) {
        logger.deeptrace("Creating user object for data: " + data.toString());
        this.discord = discord;
        this.id = data.get("id").asLong();
        this.name = data.path("username").asText(null);
        this.discriminator = data.get("discriminator").asText();
        this.avatarUrl = data.has("avatar_url") ?
                UrlHelper.orNull(data.get("avatar_url").asText()) : null;
        this.bot = data.path("bot").asBoolean(false);
        //noinspection SimplifiableConditionalExpression
        this.mfa = data.has("mfa") ?
                data.get("mfa_enabled").asBoolean(false) : false;
        this.locale = data.has("locale") ?
                data.get("locale").asText(null) : null;
        //noinspection SimplifiableConditionalExpression
        this.verified = data.has("verified") ?
                data.get("verified").asBoolean(false) : false;
        this.email = data.has("email") ?
                data.get("email").asText(null) : null;
        listenerManagers = new ArrayList<>();

        logger.nonNullChecks(name, discriminator);
        instances.putIfAbsent(id, this);
    }

    public static User getInstance(Discord discord, long id) {
        assert id != -1 : "No valid ID found.";
        return instances.containsKey(id) ?
                instances.get(id) : new WebRequest<User>(discord)
                .method(Method.GET)
                .endpoint(Endpoint.Location.USER.toEndpoint(id))
                .execute(node -> getInstance(discord, node))
                .join();
    }

    public static User getInstance(Discord discord, JsonNode data) {
        long id = data.path("id").asLong(-1);
        assert id != -1 : "No valid ID found.";
        return instances.containsKey(id) ? instances.get(id) : new UserInternal(discord, data);
    }

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
    public ServerMember toServerMember(Server server) {
        if (this instanceof ServerMember) return (ServerMember) this;
        return new ServerMemberInternal(this, server);
    }

    @Override
    public Collection<Role> getRoles(Server server) {
        return null;
    }

    @Override
    public CompletableFuture<PrivateTextChannel> openPrivateChannel() {
        return new WebRequest<PrivateTextChannel>(discord)
                .method(Method.POST)
                .endpoint(Endpoint.of(Endpoint.Location.SELF_CHANNELS))
                .node(JsonHelper.objectNode().set("recipient_id", JsonHelper.nodeOf(id)))
                .execute(node -> PrivateTextChannelInternal.getInstance(discord, node));
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
    public Collection<Message> getMessages() {
        return null;
    }

    @Override
    public String toString() {
        return "User with ID [" + id + "]";
    }

    @Override
    public <C extends UserAttachableListener> ListenerManager<C> attachListener(C listener) {
        return null; // todo
    }

    @Override
    public Evaluation<Boolean> detachListener(UserAttachableListener listener) {
        return null;
    }

    @Override
    public Collection<UserAttachableListener> getAttachedListeners() {
        return null;
    }

    public Set<EditTrait<User>> updateData(JsonNode data) {
        Set<EditTrait<User>> traits = new HashSet<>();

        if (!name.equals(data.path("name").asText(name))) {
            name = data.get("name").asText();
            traits.add(USERNAME);
        }
        if (!discriminator.equals(data.path("discriminator").asText(discriminator))) {
            discriminator = data.get("discriminator").asText();
            traits.add(DISCRIMINATOR);
        }
        if (!NullHelper.orDefault(locale, "")
                .equals(data.path("avatar_url").asText(NullHelper.orDefault(locale, "")))) {
            avatarUrl = UrlHelper.orNull(data.get("avatar_url").asText());
            traits.add(AVATAR);
        }
        if (mfa != data.path("mfa_enabled").asBoolean(mfa)) {
            mfa = data.get("mfa_enabled").asBoolean();
            traits.add(MFA_STATE);
        }
        if (verified != data.path("verified").asBoolean(verified)) {
            verified = data.get("verified").asBoolean();
            traits.add(VERIFIED_STATE);
        }
        if (!NullHelper.orDefault(locale, "")
                .equals(data.path("locale").asText(NullHelper.orDefault(locale, "")))) {
            locale = data.get("locale").asText();
            traits.add(LOCALE);
        }
        if (!NullHelper.orDefault(email, "")
                .equals(data.path("email").asText(NullHelper.orDefault(email, "")))) {
            email = data.get("email").asText();
            traits.add(EMAIL);
        }

        return traits;
    }
}
