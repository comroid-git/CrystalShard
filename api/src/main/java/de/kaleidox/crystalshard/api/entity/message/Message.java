package de.kaleidox.crystalshard.api.entity.message;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.emoji.Emoji;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.guild.Role;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.event.message.MessageEvent;
import de.kaleidox.crystalshard.api.listener.message.MessageAttachableListener;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.api.model.message.MessageActivity;
import de.kaleidox.crystalshard.api.model.message.MessageAuthor;
import de.kaleidox.crystalshard.api.model.message.MessageFlags;
import de.kaleidox.crystalshard.api.model.message.MessageType;
import de.kaleidox.crystalshard.api.model.message.TextDecoration;
import de.kaleidox.crystalshard.api.model.message.embed.Embed;
import de.kaleidox.crystalshard.api.model.message.reaction.Reaction;
import de.kaleidox.crystalshard.core.api.cache.Cacheable;
import de.kaleidox.crystalshard.core.api.rest.DiscordEndpoint;
import de.kaleidox.crystalshard.core.api.rest.HTTPStatusCodes;
import de.kaleidox.crystalshard.core.api.rest.RestMethod;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import org.intellij.lang.annotations.MagicConstant;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

public interface Message extends Snowflake, Cacheable, ListenerAttachable<MessageAttachableListener<? extends MessageEvent>> {
    @IntroducedBy(GETTER)
    TextChannel getChannel();

    @IntroducedBy(GETTER)
    Optional<Guild> getGuild();

    @IntroducedBy(GETTER)
    MessageAuthor getAuthor();

    @IntroducedBy(GETTER)
    String getContent();

    @IntroducedBy(GETTER)
    Instant getSentTimestamp();

    @IntroducedBy(GETTER)
    Instant getEditedTimestamp();

    @IntroducedBy(GETTER)
    boolean isTTS();

    @IntroducedBy(GETTER)
    boolean isMentioningEveryone();

    @IntroducedBy(GETTER)
    Collection<User> getMentionedUsers();

    @IntroducedBy(GETTER)
    Collection<Role> getMentionedRoles();

    @IntroducedBy(GETTER)
    Collection<Channel> getMentionedChannels();

    @IntroducedBy(GETTER)
    Collection<MessageAttachment> getAttachments();

    @IntroducedBy(GETTER)
    Collection<Embed> getEmbeds();

    @IntroducedBy(GETTER)
    Collection<Reaction> getCurrentReactions();

    @IntroducedBy(GETTER)
    Snowflake getNonce();

    @IntroducedBy(GETTER)
    boolean isPinned();

    @IntroducedBy(GETTER)
    MessageType getMessageType();

    @IntroducedBy(GETTER)
    Optional<MessageActivity> getMessageActivity();

    @IntroducedBy(GETTER)
    Optional<MessageApplication> getMessageApplication();

    @IntroducedBy(GETTER)
    Optional<Message> getReferencedMessage();

    @MagicConstant(flagsFromClass = MessageFlags.class)
    @IntroducedBy(GETTER)
    int getMessageFlags();

    @IntroducedBy(API)
    CompletableFuture<Reaction> addReaction(Emoji emoji);

    @IntroducedBy(API)
    default CompletableFuture<Void> delete() {
        return Adapter.<Void>request(getAPI())
                .endpoint(DiscordEndpoint.MESSAGE_SPECIFIC, getChannel().getID(), getID())
                .method(RestMethod.DELETE)
                .expectCode(HTTPStatusCodes.NO_CONTENT)
                .executeAs(data -> getAPI().getCacheManager()
                        .delete(Message.class, getID()));
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#add-pinned-channel-message")
    CompletableFuture<Void> togglePinned();

    Editor editor();

    @Override
    default OptionalLong getCacheParentID() {
        return OptionalLong.of(getChannel().getID());
    }

    @Override
    default Optional<Class<? extends Cacheable>> getCacheParentType() {
        return Optional.of(TextChannel.class);
    }

    @Override
    default boolean isSubcacheMember() {
        return true;
    }

    static Composer createComposer(TextChannel channel) {
        return Adapter.create(Composer.class, channel);
    }

    static BulkDeleter createBulkDeleter(TextChannel channel) {
        return Adapter.create(BulkDeleter.class, channel);
    }

    @IntroducedBy(PRODUCTION)
    interface BulkDeleter {
        TextChannel getChannel();

        BulkDeleter addMessage(Message message) throws IllegalArgumentException;

        BulkDeleter removeMessageIf(Predicate<Message> tester);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#bulk-delete-messages")
        CompletableFuture<Void> deleteAll();
    }

    @IntroducedBy(PRODUCTION)
    interface Composer {
        TextChannel getChannel();

        Optional<String> getText();

        Composer setText(String text);

        Composer addText(String text, TextDecoration... decorations);

        Optional<Embed> getEmbed();

        Composer setEmbed(Embed embed);

        Embed.Composer composeEmbed();

        Optional<MessageAttachment> getAttachment();

        Composer setAttachment(File file);

        Composer setAttachment(URL url);

        Composer setAttachment(InputStream inputStream);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#create-message")
        CompletableFuture<Message> send();
    }

    @IntroducedBy(PRODUCTION)
    interface Editor {
        Optional<String> getContent();

        Editor setContent(String content);

        Optional<Embed> getEmbed();

        Editor setEmbed(Embed embed);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#edit-message")
        CompletableFuture<Message> edit();
    }
}
