package de.kaleidox.crystalshard.main.items.message;

import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.main.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.message.generic.MessageDeleteListener;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionAddListener;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionRemoveListener;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.channel.*;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.message.embed.SentEmbed;
import de.kaleidox.crystalshard.main.items.message.reaction.Reaction;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;
import de.kaleidox.crystalshard.main.items.server.emoji.UnicodeEmoji;
import de.kaleidox.crystalshard.main.items.user.Author;
import de.kaleidox.crystalshard.main.items.user.AuthorUser;
import de.kaleidox.crystalshard.main.items.user.AuthorWebhook;
import de.kaleidox.crystalshard.main.items.user.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public interface Message extends DiscordItem, ListenerAttachable<MessageAttachableListener> {
    static Message of(TextChannel channel, long id) {
        return channel.getMessages()
                .stream()
                .filter(msg -> msg.getId() == id)
                .findAny()
                .orElseGet(() -> new WebRequest<Message>(channel.getDiscord())
                        .method(Method.GET)
                        .endpoint(Endpoint.Location.MESSAGE_SPECIFIC.toEndpoint(channel, id))
                        .execute(node -> MessageInternal.getInstance(channel.getDiscord(), node))
                        .join()
                );
    }

    TextChannel getChannel();

    default Optional<ServerTextChannel> getServerTextChannel() {
        return getChannel().toServerTextChannel();
    }

    default Optional<PrivateTextChannel> getPrivateTextChannel() {
        return getChannel().toPrivateTextChannel();
    }

    default Optional<GroupChannel> getGroupChannel() {
        return getChannel().toGroupChannel();
    }

    Author getAuthor();

    Optional<AuthorUser> getAuthorAsUser();

    Optional<AuthorWebhook> getAuthorAsWebhook();

    String getContent();

    String getReadableContent();

    String getTextContent();

    Instant getTimestamp();

    Optional<Instant> getEditedTimestamp();

    boolean isTTS();

    boolean mentionsEveryone();

    boolean isPinned();

    boolean isPrivate();

    MessageType getType();

    MessageActivity getActivity();

    MessageApplication getApplication();

    List<User> getUserMentions();

    List<Role> getRoleMentions();

    // TODO Discord may only send mentioned ServerTextChannels
    List<Channel> getMentionedChannels();

    List<Attachment> getAttachments();

    List<SentEmbed> getEmbeds();

    List<Reaction> getReactions();

    List<Emoji> getEmojis();

    List<CustomEmoji> getCustomEmojis();

    /*
      MODIFIER METHODS
     */

    List<UnicodeEmoji> getUnicodeEmojis();

    CompletableFuture<Message> edit(String newContent);

    CompletableFuture<Message> edit(Sendable newContent);

    CompletableFuture<Message> edit(EmbedDraft embedDraft);

    default CompletableFuture<Void> delete() {
        return delete(null);
    }

    CompletableFuture<Void> delete(String reason);

    CompletableFuture<Void> addReaction(String emojiPrintable);

    CompletableFuture<Void> addReaction(Emoji emoji);

    CompletableFuture<Void> removeAllReactions();

    CompletableFuture<Void> removeReactionsByEmoji(User user, Emoji... emojis);

    /*
      LISTENER METHODS
     */

    CompletableFuture<Void> removeOwnReactionsByEmoji(Emoji... emojis);

    ListenerManager<MessageDeleteListener> attachMessageDeleteListener(MessageDeleteListener event);

    ListenerManager<ReactionAddListener> attachReactionAddListener(ReactionAddListener event);

    ListenerManager<ReactionRemoveListener> attachReactionRemoveListener(ReactionRemoveListener event);

    void removeAllListeners();

    void removeAttachedListener(MessageAttachableListener listener);
}
