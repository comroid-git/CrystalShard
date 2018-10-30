package de.kaleidox.crystalshard.internal.items.message;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.CoreDelegate;
import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.ListenerManagerInternal;
import de.kaleidox.crystalshard.internal.items.message.embed.SentEmbedInternal;
import de.kaleidox.crystalshard.internal.items.message.reaction.ReactionInternal;
import de.kaleidox.crystalshard.internal.items.user.AuthorUserInternal;
import de.kaleidox.crystalshard.internal.items.user.AuthorWebhookInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageAttachableListener;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.*;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.message.embed.SentEmbed;
import de.kaleidox.crystalshard.main.items.message.reaction.Reaction;
import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;
import de.kaleidox.crystalshard.main.items.server.emoji.UnicodeEmoji;
import de.kaleidox.crystalshard.main.items.user.Author;
import de.kaleidox.crystalshard.main.items.user.AuthorUser;
import de.kaleidox.crystalshard.main.items.user.AuthorWebhook;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.util.helpers.FutureHelper;
import de.kaleidox.crystalshard.util.objects.functional.Evaluation;
import de.kaleidox.crystalshard.util.objects.markers.IDPair;

import java.time.DateTimeException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static de.kaleidox.crystalshard.util.helpers.JsonHelper.objectNode;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class MessageInternal implements Message {
    private final static ConcurrentHashMap<Long, Message> instances = new ConcurrentHashMap<>();
    private final static Logger logger = new Logger(MessageInternal.class);
    private final long id;
    private final long channelId;
    private final Author author;
    private final String contentRaw;
    // todo Format and store various content versions
    private final Instant timestamp;
    private final Instant editedTimestamp;
    private final boolean tts;
    private final boolean mentionsEveryone;
    private final boolean pinned;
    private final MessageType type;
    private final MessageActivity activity;
    private final MessageApplication application;
    private final List<User> userMentions = new ArrayList<>();
    private final List<Role> roleMentions = new ArrayList<>();
    private final List<Attachment> attachments = new ArrayList<>();
    private final List<SentEmbed> embeds = new ArrayList<>();
    private final List<Reaction> reactions = new ArrayList<>();
    private final Server server;
    private final Discord discord;
    private final List<Emoji> emojis = new ArrayList<>();
    private final List<Channel> channelMentions = new ArrayList<>();
    private final TextChannel channel;
    private Collection<ListenerManager<? extends MessageAttachableListener>> listenerManagers;

    public MessageInternal(Discord discord, JsonNode data) {
        logger.deeptrace("Creating message object for data: " + data.toString());
        Instant timestamp1;
        this.discord = discord;
        this.id = data.get("id")
                .asLong();
        this.channelId = data.get("channel_id")
                .asLong();
        this.channel = discord.getChannelCache()
                .getOrRequest(channelId, channelId)
                .toTextChannel()
                .get();
        if (channel.toServerChannel()
                .isPresent()) {
            this.server = channel.toServerChannel()
                    .map(ServerChannel::getServer)
                    .get();
        } else this.server = null;
        this.contentRaw = data.get("content")
                .asText();
        try {
            timestamp1 = Instant.parse(data.get("timestamp")
                    .asText());
        } catch (DateTimeException ignored) {
            timestamp1 = null;
        }
        this.timestamp = timestamp1;
        this.editedTimestamp = data.has("edited_timestamp") && !data.get("edited_timestamp")
                .isNull() ? Instant.parse(data.get("edited_timestamp")
                .asText()) :
                null;
        this.tts = data.get("tts")
                .asBoolean(false);
        this.mentionsEveryone = data.get("tts")
                .asBoolean(false);
        this.pinned = data.get("pinned")
                .asBoolean(false);
        this.type = MessageType.getTypeById(data.get("type")
                .asInt());
        this.activity = data.has("activity") ? new MessageActivityInternal(data.get("activity")) : null;
        this.application = data.has("application") ? new MessageApplicationInternal(data.get("application")) : null;
        if (!data.has("webhook_id")) {
            this.author = new AuthorUserInternal(discord, this, data.get("author"));
        } else {
            this.author = new AuthorWebhookInternal(discord, this, data.get("author"));
        }

        for (JsonNode mention : data.get("mentions")) {
            userMentions.add(new UserInternal(discord, mention));
        }

        for (JsonNode role : data.get("mention_roles")) {
            roleMentions.add(discord.getRoleCache()
                    .getOrCreate(discord, server, role));
        }

        for (JsonNode attachment : data.get("attachments")) {
            attachments.add(new AttachmentInternal(attachment));
        }

        for (JsonNode embed : data.get("embeds")) {
            embeds.add(new SentEmbedInternal(this, embed));
        }

        if (data.has("reactions")) {
            for (JsonNode reaction : data.get("reactions")) {
                reactions.add(ReactionInternal.getInstance(server, this, null, reaction, 0));
            }
        }

        listenerManagers = new ArrayList<ListenerManager<? extends MessageAttachableListener>>();

        instances.put(id, this);
    }

    // Override Methods
    @Override
    public TextChannel getChannel() {
        return channel;
    }

    @Override
    public Author getAuthor() {
        return author;
    }

    @Override
    public Optional<AuthorUser> getAuthorAsUser() {
        return author.toAuthorUser();
    }

    @Override
    public Optional<AuthorWebhook> getAuthorAsWebhook() {
        return author.toAuthorWebhook();
    }

    @Override
    public String getContent() {
        return contentRaw;
    }

    @Override
    public String getReadableContent() {
        return null;
    }

    @Override
    public String getTextContent() {
        return null;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public Optional<Instant> getEditedTimestamp() {
        return Optional.ofNullable(editedTimestamp);
    }

    @Override
    public boolean isTTS() {
        return tts;
    }

    @Override
    public boolean mentionsEveryone() {
        return mentionsEveryone;
    }

    @Override
    public boolean isPinned() {
        return pinned;
    }

    @Override
    public boolean isPrivate() {
        return server != null;
    }

    @Override
    public MessageType getType() {
        return type;
    }

    @Override
    public Optional<MessageActivity> getActivity() {
        return Optional.ofNullable(activity);
    }

    @Override
    public Optional<MessageApplication> getApplication() {
        return Optional.ofNullable(application);
    }

    @Override
    public List<User> getUserMentions() {
        return Collections.unmodifiableList(userMentions);
    }

    @Override
    public List<Role> getRoleMentions() {
        return Collections.unmodifiableList(roleMentions);
    }

    @Override
    public List<Channel> getChannelMentions() {
        return Collections.unmodifiableList(channelMentions);
    }

    @Override
    public List<Attachment> getAttachments() {
        return Collections.unmodifiableList(attachments);
    }

    @Override
    public List<SentEmbed> getEmbeds() {
        return Collections.unmodifiableList(embeds);
    }

    @Override
    public List<Reaction> getReactions() {
        return Collections.unmodifiableList(reactions);
    }

    @Override
    public List<Emoji> getEmojis() {
        return Collections.unmodifiableList(emojis);
    }

    @Override
    public List<CustomEmoji> getCustomEmojis() {
        return null;
    }

    @Override
    public List<UnicodeEmoji> getUnicodeEmojis() {
        return null;
    }

    @Override
    public CompletableFuture<Message> edit(String newContent) {
        return null;
    }

    @Override
    public CompletableFuture<Message> edit(Sendable newContent) {
        return null;
    }

    @Override
    public CompletableFuture<Message> edit(EmbedDraft embedDraft) {
        return null;
    }

    @Override
    public CompletableFuture<Void> delete(String reason) {
        if (channel.toServerChannel()
                .isPresent() && !author.isYourself() && !channel.hasPermission(discord, Permission.MANAGE_MESSAGES))
            return FutureHelper.failedFuture(new DiscordPermissionException("Cannot delete " + toString() + "; you are not the author.",
                    Permission.MANAGE_MESSAGES));
        return CoreDelegate.webRequest(discord)
                .setMethod(HttpMethod.DELETE)
                .setUri(DiscordEndpoint.MESSAGE_SPECIFIC.createUri(channelId, id))
                .executeAsVoid();
    }

    @Override
    public CompletableFuture<Void> addReaction(String emojiPrintable) {
        if (!channel.hasPermission(discord, Permission.READ_MESSAGE_HISTORY))
            return FutureHelper.failedFuture(new DiscordPermissionException(
                    "Cannot read message history!",
                    Permission.READ_MESSAGE_HISTORY));
        if (getReactions().stream()
                .map(Reaction::getEmoji)
                .map(Emoji::toDiscordPrintable)
                .noneMatch(emojiPrintable::equalsIgnoreCase) &&
                !channel.hasPermission(discord, Permission.ADD_REACTIONS))
            return FutureHelper.failedFuture(new DiscordPermissionException(
                    "Cannot add new reactions!",
                    Permission.ADD_REACTIONS));
        return CoreDelegate.webRequest(discord)
                .setMethod(HttpMethod.PUT)
                .setUri(DiscordEndpoint.REACTION_OWN.createUri(channelId, id, emojiPrintable))
                .executeAsVoid();
    }

    @Override
    public CompletableFuture<Void> removeAllReactions() {
        if (!channel.hasPermission(discord, Permission.MANAGE_MESSAGES))
            return FutureHelper.failedFuture(new DiscordPermissionException(
                    "Cannot remove other peoples reactions.",
                    Permission.MANAGE_MESSAGES));
        return CoreDelegate.webRequest(discord)
                .setMethod(HttpMethod.DELETE)
                .setUri(DiscordEndpoint.REACTIONS.createUri(channelId, id))
                .executeAsVoid();
    }

    @Override
    public CompletableFuture<Void> removeReactionsByEmoji(Emoji... emojis) {
        if (!channel.hasPermission(discord, Permission.MANAGE_MESSAGES))
            return FutureHelper.failedFuture(new DiscordPermissionException(
                    "Cannot remove other peoples reactions.",
                    Permission.MANAGE_MESSAGES));
        List<CompletableFuture<Void>> deletions = new ArrayList<>();

        reactions.stream()
                .filter(reaction -> Stream.of(emojis)
                        .anyMatch(emoji -> reaction.getEmoji()
                                .equals(emoji)))
                .forEach(reaction -> deletions.add(CoreDelegate.webRequest(discord)
                        .setMethod(HttpMethod.DELETE)
                        .setUri(DiscordEndpoint.REACTION_USER.createUri(channelId,
                                id,
                                reaction.getEmoji()
                                        .toDiscordPrintable(),
                                reaction.getUser()))
                        .executeAsVoid()));

        return CompletableFuture.allOf(deletions.toArray(new CompletableFuture[0]));
    }

    @Override
    public CompletableFuture<Void> removeReactionsByUser(User... users) {
        if (!channel.hasPermission(discord, Permission.MANAGE_MESSAGES))
            return FutureHelper.failedFuture(new DiscordPermissionException(
                    "Cannot remove other peoples reactions.",
                    Permission.MANAGE_MESSAGES));
        List<CompletableFuture<Void>> deletions = new ArrayList<>();

        reactions.stream()
                .filter(reaction -> Stream.of(users)
                        .anyMatch(user -> reaction.getUser()
                                .equals(user)))
                .forEach(reaction -> deletions.add(CoreDelegate.webRequest(discord)
                        .setMethod(HttpMethod.DELETE)
                        .setUri(DiscordEndpoint.REACTION_USER.createUri(channelId,
                                id,
                                reaction.getEmoji()
                                        .toDiscordPrintable(),
                                reaction.getUser()))
                        .executeAsVoid()));

        return CompletableFuture.allOf(deletions.toArray(new CompletableFuture[0]));
    }

    @Override
    public CompletableFuture<Message> pin() {
        return CoreDelegate.webRequest(Message.class, discord)
                .setMethod(HttpMethod.PUT)
                .setUri(DiscordEndpoint.PIN_MESSAGE.createUri(channelId, id))
                .executeAs(node -> this);
    }

    @Override
    public CompletableFuture<Message> unpin() {
        return CoreDelegate.webRequest(Message.class, discord)
                .setMethod(HttpMethod.DELETE)
                .setUri(DiscordEndpoint.PIN_MESSAGE.createUri(channelId, id))
                .executeAs(node -> this);
    }

    @Override
    public BulkDelete getBulkDelete() {
        return new BulkDeleteInternal(discord).addMessage(this);
    }

    @Override
    public <C extends MessageAttachableListener> ListenerManager<C> attachListener(C listener) {
        ListenerManager<C> manager = ListenerManagerInternal.getInstance((DiscordInternal) discord, listener);
        listenerManagers.add(manager);
        return manager;
    }

    @Override
    public Evaluation<Boolean> detachListener(MessageAttachableListener listener) {
        return null;
    }

    @Override
    public Collection<MessageAttachableListener> getAttachedListeners() {
        return null;
    }

    @Override
    public Collection<ListenerManager<? extends MessageAttachableListener>> getListenerManagers() {
        return listenerManagers;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public Discord getDiscord() {
        return null;
    }

    @Override
    public String toString() {
        return "Message with ID [" + id + "]";
    }

    @Override
    public Cache<Message, Long, IDPair> getCache() {
        return discord.getMessageCache();
    }

    public Set<EditTrait<Message>> updateData(JsonNode data) {
        return null; // todo
    }

    public static class BulkDeleteInternal implements BulkDelete {
        private final Discord discord;
        private Long channelId = null;
        private List<Long> ids;

        public BulkDeleteInternal(Discord discord) {
            this.discord = discord;
            ids = new ArrayList<>();
        }

        // Override Methods
        @Override
        public BulkDelete setChannel(long channelId) {
            this.channelId = channelId;
            return this;
        }

        @Override
        public BulkDelete addMessages(Message... messages) {
            for (Message message : messages) {
                addMessage(message);
            }
            return this;
        }

        @Override
        public BulkDelete addMessage(Message message) {
            addId(message.getId());
            return this;
        }

        @Override
        public BulkDelete addIds(long... ids) {
            for (long id : ids) {
                addId(id);
            }
            return this;
        }

        @Override
        public BulkDelete addId(long id) {
            if (ids.size() >= 100)
                throw new IllegalArgumentException("BulkDelete only allowed with up to 100 Messages!");
            ids.add(id);
            return this;
        }

        @Override
        public CompletableFuture<Void> deleteAll() {
            Objects.requireNonNull(channelId, "ChannelID for BulkDelete must not be NULL!");
            if (ids.size() >= 100)
                throw new IllegalArgumentException("BulkDelete only allowed with up to 100 Messages!");
            return CoreDelegate.webRequest(discord)
                    .setMethod(HttpMethod.POST)
                    .setUri(DiscordEndpoint.MESSAGES_BULK_DELETE.createUri(channelId))
                    .setNode(objectNode(
                            "messages",
                            ids))
                    .executeAsVoid();
        }
    }
}
