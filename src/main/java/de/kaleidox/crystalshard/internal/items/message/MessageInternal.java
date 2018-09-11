package de.kaleidox.crystalshard.internal.items.message;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.channel.ChannelInternal;
import de.kaleidox.crystalshard.internal.items.message.embed.SentEmbedInternal;
import de.kaleidox.crystalshard.internal.items.message.reaction.ReactionInternal;
import de.kaleidox.crystalshard.internal.items.role.RoleInternal;
import de.kaleidox.crystalshard.internal.items.user.AuthorUserInternal;
import de.kaleidox.crystalshard.internal.items.user.AuthorWebhookInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageDeleteListener;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionAddListener;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionRemoveListener;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Attachment;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.MessageActivity;
import de.kaleidox.crystalshard.main.items.message.MessageApplication;
import de.kaleidox.crystalshard.main.items.message.MessageType;
import de.kaleidox.crystalshard.main.items.message.Sendable;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.message.embed.SentEmbed;
import de.kaleidox.crystalshard.main.items.message.reaction.Reaction;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;
import de.kaleidox.crystalshard.main.items.server.emoji.UnicodeEmoji;
import de.kaleidox.crystalshard.main.items.user.Author;
import de.kaleidox.crystalshard.main.items.user.AuthorUser;
import de.kaleidox.crystalshard.main.items.user.AuthorWebhook;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.logging.Logger;

import java.time.DateTimeException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class MessageInternal implements Message {
    private final static ConcurrentHashMap<Long, Message> instances = new ConcurrentHashMap<>();
    private final static Logger logger = new Logger(MessageInternal.class);
    private final long id;
    private final long channelId;
    private final Author author;
    private final String contentRaw; // todo Format and store various content versions
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
    private Collection<? extends MessageAttachableListener> listeners;

    private MessageInternal(Discord discord, JsonNode data) {
        logger.deeptrace("Creating message object for data: " + data.toString());
        Instant timestamp1;
        this.discord = discord;
        this.id = data.get("id").asLong();
        this.channelId = data.get("channel_id").asLong();
        this.channel = ChannelInternal.getInstance(discord, channelId).toTextChannel().get();
        if (channel.toServerChannel().isPresent()) {
            this.server = channel.toServerChannel().map(ServerChannel::getServer).get();
        } else this.server = null;
        this.contentRaw = data.get("content").asText();
        try {
            timestamp1 = Instant.parse(data.get("timestamp").asText());
        } catch (DateTimeException ignored) {
            timestamp1 = null;
        }
        this.timestamp = timestamp1;
        this.editedTimestamp = data.has("edited_timestamp") && !data.get("edited_timestamp").isNull() ?
                Instant.parse(data.get("edited_timestamp").asText()) : null;
        this.tts = data.get("tts").asBoolean(false);
        this.mentionsEveryone = data.get("tts").asBoolean(false);
        this.pinned = data.get("pinned").asBoolean(false);
        this.type = MessageType.getTypeById(data.get("type").asInt());
        this.activity = data.has("activity") ?
                new MessageActivityInternal(data.get("activity")) : null;
        this.application = data.has("application") ?
                new MessageApplicationInternal(data.get("application")) : null;
        if (!data.has("webhook_id")) {
            this.author = new AuthorUserInternal(discord, this, data.get("author"));
        } else {
            this.author = new AuthorWebhookInternal(discord, this, data.get("author"));
        }

        for (JsonNode mention : data.get("mentions")) {
            userMentions.add(new UserInternal(discord, mention));
        }

        for (JsonNode role : data.get("mention_roles")) {
            roleMentions.add(RoleInternal.getInstance(server, role));
        }

        for (JsonNode attachment : data.get("attachments")) {
            attachments.add(new AttachmentInternal(attachment));
        }

        for (JsonNode embed : data.get("embeds")) {
            embeds.add(new SentEmbedInternal(embed));
        }

        if (data.has("reactions")) {
            for (JsonNode reaction : data.get("reactions")) {
                reactions.add(ReactionInternal.getInstance(server, this, null, reaction, 0));
            }
        }

        listeners = new ArrayList<>();

        instances.put(id, this);
    }

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
    public MessageActivity getActivity() {
        return activity;
    }

    @Override
    public MessageApplication getApplication() {
        return application;
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
    public List<Channel> getMentionedChannels() {
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
        return null;
    }

    @Override
    public CompletableFuture<Void> addReaction(String emojiPrintable) {
        return null;
    }

    @Override
    public CompletableFuture<Void> addReaction(Emoji emoji) {
        return null;
    }

    @Override
    public CompletableFuture<Void> removeAllReactions() {
        return null;
    }

    @Override
    public CompletableFuture<Void> removeReactionsByEmoji(User user, Emoji... emojis) {
        return null;
    }

    @Override
    public CompletableFuture<Void> removeOwnReactionsByEmoji(Emoji... emojis) {
        return null;
    }

    @Override
    public ListenerManager<MessageDeleteListener> attachMessageDeleteListener(MessageDeleteListener event) {
        return null;
    }

    @Override
    public ListenerManager<ReactionAddListener> attachReactionAddListener(ReactionAddListener event) {
        return null;
    }

    @Override
    public ListenerManager<ReactionRemoveListener> attachReactionRemoveListener(ReactionRemoveListener event) {
        return null;
    }

    @Override
    public <T extends MessageAttachableListener> ListenerManager<T> addListener(T event) {
        return null;
    }

    @Override
    public Collection<? extends MessageAttachableListener> getListeners() {
        return listeners;
    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public void removeAttachedListener(MessageAttachableListener listener) {

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

    public static Message getInstance(Discord discord, JsonNode data) {
        synchronized (instances) {
            long id = data.get("id").asLong(-1);
            if (id == -1) throw new NoSuchElementException("No valid ID found.");
            return instances.getOrDefault(id, new MessageInternal(discord, data));
        }
    }

    public static Message getInstance(TextChannel channel, long id) {
        return instances.getOrDefault(id,
                new WebRequest<Message>(channel.getDiscord())
                        .method(Method.GET)
                        .endpoint(Endpoint.Location.MESSAGE_SPECIFIC.toEndpoint(channel, id))
                        .execute(node -> getInstance(channel.getDiscord(), node))
                        .join()
        );
    }
}
