package de.kaleidox.crystalshard.internal.items.message;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.items.message.embed.SentEmbedInternal;
import de.kaleidox.crystalshard.internal.items.message.reaction.ReactionInternal;
import de.kaleidox.crystalshard.internal.items.role.RoleInternal;
import de.kaleidox.crystalshard.internal.items.user.AuthorUserInternal;
import de.kaleidox.crystalshard.internal.items.user.AuthorWebhookInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Attachment;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.MessageActivity;
import de.kaleidox.crystalshard.main.items.message.MessageApplication;
import de.kaleidox.crystalshard.main.items.message.MessageType;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class MessageInternal implements Message {
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

    public MessageInternal(Discord discord, Server server, JsonNode data) {
        this.discord = discord;
        this.server = server;
        this.id = data.get("id").asLong();
        this.channelId = data.get("channel_id").asLong();
        this.contentRaw = data.get("content").asText();
        this.timestamp = Instant.parse(data.get("timestamp").asText());
        this.editedTimestamp = data.has("edited_timestamp") ?
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
            roleMentions.add(new RoleInternal(null, null, role));
        }

        for (JsonNode attachment : data.get("attachments")) {
            attachments.add(new AttachmentInternal(attachment));
        }

        for (JsonNode embed : data.get("embeds")) {
            embeds.add(new SentEmbedInternal(embed));
        }

        if (data.has("reactions")) {
            for (JsonNode reaction : data.get("reactions")) {
                reactions.add(new ReactionInternal(reaction));
            }
        }
    }

    @Override
    public TextChannel getChannel() {
        return null;
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
    public long getId() {
        return 0;
    }

    @Override
    public Discord getDiscord() {
        return null;
    }
}
