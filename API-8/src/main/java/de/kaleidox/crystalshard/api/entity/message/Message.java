package de.kaleidox.crystalshard.api.entity.message;

import org.intellij.lang.annotations.MagicConstant;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.DiscordItem;
import de.kaleidox.crystalshard.api.entity.Mentionable;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.GroupChannel;
import de.kaleidox.crystalshard.api.entity.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.api.entity.channel.ServerTextChannel;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.message.embed.Embed;
import de.kaleidox.crystalshard.api.entity.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.api.entity.message.embed.SentEmbed;
import de.kaleidox.crystalshard.api.entity.message.reaction.Reaction;
import de.kaleidox.crystalshard.api.entity.permission.Permission;
import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.entity.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.api.entity.server.emoji.Emoji;
import de.kaleidox.crystalshard.api.entity.server.emoji.UnicodeEmoji;
import de.kaleidox.crystalshard.api.entity.user.Author;
import de.kaleidox.crystalshard.api.entity.user.AuthorUser;
import de.kaleidox.crystalshard.api.entity.user.AuthorWebhook;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.api.exception.IllegalThreadException;
import de.kaleidox.crystalshard.api.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.api.handling.listener.message.MessageAttachableListener;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.internal.InternalInjector;
import de.kaleidox.util.annotations.NotContainNull;
import de.kaleidox.util.annotations.Range;
import de.kaleidox.util.markers.IDPair;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This interface represents a normal Discord message.
 */
public interface Message extends DiscordItem, ListenerAttachable<MessageAttachableListener>, Cacheable<Message, Long, IDPair> {
    /**
     * Gets the user (Author) that sent this message.
     *
     * @return The author of the message.
     */
    Author getAuthor();

    /**
     * Gets the author of the message as an AuthorUser.
     *
     * @return The optional AuthorUser.
     */
    Optional<AuthorUser> getAuthorAsUser();

    /**
     * Gets the author of the message as an AuthorWebhook.
     *
     * @return The optional AuthorWebhook.
     */
    Optional<AuthorWebhook> getAuthorAsWebhook();

    /**
     * Gets the raw content of the message, including full mention tags and markdown.
     *
     * @return The raw content of the message.
     * @see #getReadableContent()
     * @see #getTextContent()
     */
    String getContent();

    /**
     * Gets the readable content of the message. All mentions get replaced by their respective names. Markdown tags will not get removed. See {@link
     * #getTextContent()}.
     *
     * @return The readable content of the message.
     * @see de.kaleidox.crystalshard.api.util.DiscordMentiontagPatterns
     */
    String getReadableContent();

    /**
     * Gets the plain text content. All Markdown tags will get removed, as well as mentions are being replaced as in {@link #getReadableContent()}.
     *
     * @return The plain text content of the message.
     */
    String getTextContent();

    /**
     * Gets the timestamp of when the message has been sent.
     *
     * @return An Instant of when the message has been sent.
     */
    Instant getTimestamp();

    /**
     * Gets the timestamp that marks when the message has been last edited.
     *
     * @return The optional last-edit timestamp.
     */
    Optional<Instant> getEditedTimestamp();

    /**
     * Gets whether the message is marked for TextToSpeech.
     *
     * @return Whether the message is marked for TextToSpeech.
     */
    boolean isTTS();

    /**
     * Gets whether the message contains an {@code @everyone} mention; thus mentions everyone available.
     *
     * @return Whether the message mentions every available user.
     */
    boolean mentionsEveryone();

    /**
     * Gets whether the message was sent in a private context or not.
     *
     * @return Whether the message is private.
     */
    boolean isPrivate();

    /**
     * Gets the type of the message. The type of the message can tell whether the message was e.g. sent by a Webhook, or if the message was a "Welcome User XYZ"
     * message.
     *
     * @return The type of the message.
     */
    @MagicConstant(valuesFromClass = Type.class)
    int getType();

    /**
     * Gets the richpresence activity of this message.
     *
     * @return The optional MessageActivity of this message.
     */
    Optional<MessageActivity> getActivity();

    /**
     * Gets the application of this message.
     * <b>NOTE: This feature is untestable yet. It is only included in documentation.</b>
     *
     * @return The optional MessageApplication of this message.
     */
    Optional<MessageApplication> getApplication();

    /**
     * Gets a list of all users that have been mentioned within this message.
     *
     * @return A list of mentioned users.
     */
    List<User> getUserMentions();

    /**
     * Gets a list of all roles that have been mentioned within this message.
     *
     * @return A list of mentioned roles.
     */
    List<Role> getRoleMentions();

    /**
     * Gets a list of all channels that have been mentioned within this message.
     *
     * @return A list of mentioned channels.
     */
    List<Channel> getChannelMentions(); // TODO Discord may only send mentioned ServerTextChannels

    /**
     * Gets a list of the attachments to this message.
     *
     * @return A list of attachments for this message.
     */
    List<Attachment> getAttachments();

    /**
     * Gets a list of all reactions on this message.
     *
     * @return A list with this messages reactions.
     */
    List<Reaction> getReactions();

    /**
     * Gets a list of any emoji used within this message.
     *
     * @return A list of used Emojis.
     * @see com.vdurmont.emoji.EmojiParser
     */
    List<Emoji> getEmojis();

    /**
     * Gets a list of the CustomEmojis that got used within this message.
     *
     * @return A list with used CustomEmojis.
     */
    List<CustomEmoji> getCustomEmojis();

    /**
     * Gets a list of the UnicodeEmojis that got used within this message.
     *
     * @return A list with used UnicodeEmojis.
     */
    List<UnicodeEmoji> getUnicodeEmojis();

    /**
     * Updates the content of the current message. The returned future will complete exceptionally with a {@link IllegalAccessException} if the bot is not the
     * author of this message, as you may never edit other people's messages.
     *
     * @param newContent The new content to set.
     * @return A future that completes with this message when it has been modified.
     */
    CompletableFuture<Message> edit(String newContent);

    /**
     * Updates the content of the current message. The returned future will complete exceptionally with a {@link IllegalAccessException} if the bot is not the
     * author of this message, as you may never edit other people's messages.
     *
     * @param embedDraft The new Embed to set.
     * @return A future that completes with this message when it has been modified.
     */
    CompletableFuture<Message> edit(EmbedDraft embedDraft);

    /**
     * Removes all reactions of the message. This method requires the bot to have the {@link Permission#MANAGE_MESSAGES} within the current context. The
     * returned future will complete exceptionally with a {@link DiscordPermissionException} if the bot does not have the permission required to remove all
     * reactions. This will also occur on Private chats, as you can not have the theoretical permission to remove reactions in a private chat.
     *
     * @return A future that completes when all reactions have been removed.
     */
    CompletableFuture<Void> removeAllReactions();

    /**
     * Removes all reactions from a specific user filtered by the given emojis. Don't include any emoji to remove all known reactions of the user. This method
     * requires the bot to have the {@link Permission#MANAGE_MESSAGES} within the current context. The returned future will complete exceptionally with a {@link
     * DiscordPermissionException} if the bot does not have the permission required to remove other users reactions. This will also occur on Private chats, as
     * you can not have the theoretical permission to remove reactions in a private chat.
     *
     * @param emojis The emojis whose reactions should be removed.
     * @return A future that completes when all reactions have been removed.
     */
    CompletableFuture<Void> removeReactionsByEmoji(@NotContainNull Emoji... emojis);

    /**
     * Removes all reactions of the bot from the message, filtered by the given emojis. Don't include any emoji to remove all known reactions by yourself.
     *
     * @param users The users whose reaction should be removed.
     * @return A future that completes when the reactions are removed.
     */
    CompletableFuture<Void> removeReactionsByUser(@NotContainNull User... users);

    BulkDelete getBulkDelete();

    default CompletableFuture<Message> togglePinned() {
        if (isPinned()) return unpin();
        else return pin();
    }

    /**
     * Gets whether the message is currently pinned in its channel.
     *
     * @return Whether the message is currently pinned.
     */
    boolean isPinned();

    CompletableFuture<Message> pin();

    CompletableFuture<Message> unpin();

    /**
     * Adds a reaction with the given emoji to the message. This method requires the bot to have the {@link Permission#ADD_REACTIONS} within the current
     * context. The returned future will complete exceptionally with a {@link DiscordPermissionException} if the bot does not have the permission required to
     * add reactions.
     *
     * @param emoji The emoji to add as a reaction.
     * @return A future that completes when the reaction has been added.
     */
    default CompletableFuture<Void> addReaction(Emoji emoji) {
        return addReaction(emoji.toDiscordPrintable());
    }

    /**
     * Adds a reaction with the given emoji-printable to the message. Emoji printables of UnicodeEmojis are the actual UnicodeEmoji, the printable of a
     * CustomEmojis is its mention tag. See {@link Emoji#toDiscordPrintable()}. This method requires the bot to have the {@link Permission#ADD_REACTIONS} within
     * the current context. The returned future will complete exceptionally with a {@link DiscordPermissionException} if the bot does not have the permission
     * required to add reactions.
     *
     * @param emojiPrintable The emojis to add as a reaction.
     * @return A future that completes when the reaction has been added.
     */
    CompletableFuture<Void> addReaction(String emojiPrintable);

    /**
     * Gets the TextChannel as a ServerTextChannel.
     *
     * @return The optional ServerTextChannel.
     */
    default Optional<ServerTextChannel> getServerTextChannel() {
        return getChannel().toServerTextChannel();
    }

    /**
     * Gets the TextChannel as a PrivateTextChannel.
     *
     * @return The optional PrivateTextChannel.
     */
    default Optional<PrivateTextChannel> getPrivateTextChannel() {
        return getChannel().toPrivateTextChannel();
    }

    /**
     * Gets the TextChannel as a GroupChannel.
     *
     * @return The optional GroupChannel.
     */
    default Optional<GroupChannel> getGroupChannel() {
        return getChannel().toGroupChannel();
    }

    /**
     * Gets the api embed of this message. See {@link #getEmbeds()} to get a full list of embeds.
     *
     * @return The optional Embed of this message.
     */
    default Optional<SentEmbed> getEmbed() {
        List<SentEmbed> embeds = getEmbeds();
        return Optional.ofNullable(embeds.size() > 0 ? embeds.get(0) : null);
    }

    /**
     * Gets a list of the embeds within this message. Usually, there is only one embed per message. There may be more than one embed per message if there are
     * e.g. embedded YouTube Videos. See {@link #getEmbed()} to only get the "api" embed of the message.
     *
     * @return A list of embeds.
     */
    List<SentEmbed> getEmbeds();

    /**
     * Deletes the current message. If the bot is not the owner of the message, this method requires the bot to have the {@link Permission#MANAGE_MESSAGES}
     * within the current context. The returned future will complete exceptionally with a {@link DiscordPermissionException} if the bot does not have the
     * permission required to close this message. This will always occur in a private chat, as the bot can never have the theoretical permissions to close other
     * peoples messages within a private chat.
     *
     * @return A future that completes when the message has been deleted.
     */
    default CompletableFuture<Void> delete() {
        return delete(null);
    }

    /**
     * Deletes the current message for a specified reason. If the bot is not the owner of the message, this method requires the bot to have the {@link
     * Permission#MANAGE_MESSAGES} within the current context. The returned future will complete exceptionally with a {@link DiscordPermissionException} if the
     * bot does not have the permission required to close this message. This will always occur in a private chat, as the bot can never have the theoretical
     * permissions to close other peoples messages within a private chat.
     *
     * @param reason The reason to close the message for.
     * @return A future that completes when the message has been deleted.
     */
    CompletableFuture<Void> delete(String reason);

    default CompletableFuture<Void> addReaction(String... emojis) {
        CompletableFuture[] cfs = new CompletableFuture[emojis.length];
        for (int i = 0; i < emojis.length; i++) cfs[i] = addReaction(emojis[i]);
        return CompletableFuture.allOf(cfs);
    }

    /**
     * Gets the TextChannel that the message has been sent in.
     *
     * @return The text channel of the message.
     */
    TextChannel getChannel();

    static CompletableFuture<Void> bulkDelete(long channelId, @NotContainNull @Range(min = 2, max = 100) long... messageIds) throws IllegalThreadException {
        Discord discord = ThreadPool.getThreadDiscord();
        return InternalInjector.newInstance(BulkDelete.class, discord)
                .setChannel(channelId)
                .addIds(messageIds)
                .deleteAll();
    }

    static CompletableFuture<Void> bulkDelete(@NotContainNull @Range(min = 2, max = 100) Message... messages) {
        return InternalInjector.newInstance(BulkDelete.class, messages[0].getDiscord())
                .setChannel(messages[0].getChannel()
                        .getId())
                .addMessages(messages)
                .deleteAll();
    }

    static Builder builder() {
        return InternalInjector.newInstance(Builder.class);
    }

    static Builder builder(Message ofMessage) {
        return InternalInjector.newInstance(Builder.class, ofMessage);
    }

    interface Builder {
        /**
         * Appends the provided text as plain string to the message.
         *
         * @param text The text to append.
         * @return The instance of the builder.
         */
        Builder addText(String text);

        /**
         * Adds a mention to the provided mentionable as text to the message.
         *
         * @param mentionable The mentionable to mention.
         * @return The instance of the builder.
         */
        Builder addMention(Mentionable mentionable);

        /**
         * Adds an emoji to the message as text.
         *
         * @param emoji The emoji to add.
         * @return The instance of the builder.
         */
        Builder addEmoji(Emoji emoji);

        /**
         * Sets the embed for the message.
         *
         * @param embed The embed to set.
         * @return The instance of the builder.
         * @see #setEmbed(Embed.Builder)
         */
        Builder setEmbed(EmbedDraft embed);

        /**
         * Builds and sends the message to the given target reciever.
         * The returned future will complete with a {@link DiscordPermissionException}
         * if you are not allowed to send messages to the provided target.
         *
         * @param target The reciever to send the message to.
         * @return A future to contain the sent message.
         */
        CompletableFuture<Message> send(MessageReciever target);

        /**
         * Sets an embed for the message.
         *
         * @param embedBuilder The embed to build and then set.
         * @return The instance of the builder.
         * @see #setEmbed(EmbedDraft)
         */
        default Builder setEmbed(Embed.Builder embedBuilder) {
            return setEmbed(embedBuilder.build());
        }
    }

    interface BulkDelete {
        BulkDelete setChannel(long channelId);

        BulkDelete addMessage(Message message);

        BulkDelete addMessages(@Range(min = 2, max = 100) Message... messages);

        BulkDelete addId(long id);

        BulkDelete addIds(@Range(min = 2, max = 100) long... ids);

        CompletableFuture<Void> deleteAll();
    }

    class Type {
        public static final int DEFAULT = 0;
        public static final int RECIPIENT_ADD = 1;
        public static final int RECIPIENT_REMOVE = 2;
        public static final int CALL = 3;
        public static final int CHANNEL_NAME_CHANGE = 4;
        public static final int CHANNEL_ICON_CHANGE = 5;
        public static final int CHANNEL_PINNED_MESSAGE = 6;
        public static final int GUILD_MEMBER_JOIN = 7;
    }
}
