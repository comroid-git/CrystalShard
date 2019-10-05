package de.kaleidox.crystalshard.api.entity.message;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.emoji.Emoji;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.guild.Role;
import de.kaleidox.crystalshard.api.entity.guild.webhook.Webhook;
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
import de.kaleidox.crystalshard.core.api.cache.CacheManager;
import de.kaleidox.crystalshard.core.api.cache.Cacheable;
import de.kaleidox.crystalshard.core.api.rest.DiscordEndpoint;
import de.kaleidox.crystalshard.core.api.rest.HTTPStatusCodes;
import de.kaleidox.crystalshard.core.api.rest.RestMethod;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;
import de.kaleidox.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.intellij.lang.annotations.MagicConstant;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.cache;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.collective;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.identity;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.simple;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.underlying;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.underlyingCollective;

@JsonTraits(Message.Trait.class)
public interface Message extends Snowflake, Cacheable, ListenerAttachable<MessageAttachableListener<? extends MessageEvent>> {
    @IntroducedBy(GETTER)
    default TextChannel getChannel() {
        return getTraitValue(Trait.CHANNEL);
    }

    @IntroducedBy(GETTER)
    default Optional<Guild> getGuild() {
        return wrapTraitValue(Trait.GUILD);
    }

    @IntroducedBy(GETTER)
    default MessageAuthor getAuthor() {
        return getTraitValue(Trait.USER_AUTHOR);
    }

    @IntroducedBy(GETTER)
    default String getContent() {
        return getTraitValue(Trait.CONTENT);
    }

    @IntroducedBy(GETTER)
    default Instant getSentTimestamp() {
        return getTraitValue(Trait.SENT_TIMESTAMP);
    }

    @IntroducedBy(GETTER)
    default Instant getEditedTimestamp() {
        return getTraitValue(Trait.EDITED_TIMESTAMP);
    }
    
    @IntroducedBy(GETTER)
    default boolean isTTS() {
        return getTraitValue(Trait.TTS);
    }

    @IntroducedBy(GETTER)
    default boolean isMentioningEveryone() {
        return getTraitValue(Trait.MENTIONS_EVERYONE);
    }

    @IntroducedBy(GETTER)
    default Collection<User> getMentionedUsers() {
        return getTraitValue(Trait.MENTIONED_USERS);
    }

    @IntroducedBy(GETTER)
    default Collection<Role> getMentionedRoles() {
        return getTraitValue(Trait.MENTIONED_ROLES);
    }

    @IntroducedBy(GETTER)
    default Collection<Channel> getMentionedChannels() {
        return getTraitValue(Trait.MENTIONED_CHANNELS);
    }

    @IntroducedBy(GETTER)
    default Collection<MessageAttachment> getAttachments() {
        return getTraitValue(Trait.ATTACHMENTS);
    }

    @IntroducedBy(GETTER)
    default Collection<Embed> getEmbeds() {
        return getTraitValue(Trait.EMBEDS);
    }

    @IntroducedBy(GETTER)
    default Collection<Reaction> getCurrentReactions() {
        return getTraitValue(Trait.CURRENT_REACTIONS);
    }

    @IntroducedBy(GETTER)
    default Optional<Snowflake> getNonce() {
        return wrapTraitValue(Trait.NONCE);
    }

    @IntroducedBy(GETTER)
    default boolean isPinned() {
        return getTraitValue(Trait.PINNED);
    }

    @IntroducedBy(GETTER)
    default MessageType getMessageType() {
        return getTraitValue(Trait.TYPE);
    }

    @IntroducedBy(GETTER)
    default Optional<MessageActivity> getMessageActivity() {
        return wrapTraitValue(Trait.ACTIVITY);
    }

    @IntroducedBy(GETTER)
    default Optional<MessageApplication> getMessageApplication() {
        return wrapTraitValue(Trait.APPLICATION);
    }

    @IntroducedBy(GETTER)
    default Optional<Message> getReferencedMessage() {
        return wrapTraitValue(Trait.REFERENCED_MESSAGE);
    }

    @IntroducedBy(GETTER)
    @MagicConstant(flagsFromClass = MessageFlags.class)
    default int getMessageFlags() {
        return getTraitValue(Trait.FLAGS);
    }

    interface Trait extends Snowflake.Trait {
        JsonTrait<Long, TextChannel> CHANNEL = cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asTextChannel));
        JsonTrait<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JsonTrait<JsonNode, User> USER_AUTHOR = underlying("author", User.class);
        JsonTrait<JsonNode, Webhook> WEBHOOK_AUTHOR = null; // todo
        JsonTrait<String, String> CONTENT = identity(JsonNode::asText, "content");
        JsonTrait<String, Instant> SENT_TIMESTAMP = simple(JsonNode::asText, "timestamp", Instant::parse);
        JsonTrait<String, Instant> EDITED_TIMESTAMP = simple(JsonNode::asText, "edited_timestamp", Instant::parse);
        JsonTrait<Boolean, Boolean> TTS = identity(JsonNode::asBoolean, "tts");
        JsonTrait<Boolean, Boolean> MENTIONS_EVERYONE = identity(JsonNode::asBoolean, "mention_everyone");
        JsonTrait<ArrayNode, Collection<User>> MENTIONED_USERS = underlyingCollective("mentions", User.class);
        JsonTrait<ArrayNode, Collection<Role>> MENTIONED_ROLES = underlyingCollective("mention_roles", Role.class, (api, data) -> api.getCacheManager()
                .getByID(Role.class, data.asLong())
                .orElseThrow());
    }
    
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
