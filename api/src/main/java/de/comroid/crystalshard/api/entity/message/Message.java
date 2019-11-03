package de.comroid.crystalshard.api.entity.message;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.entity.emoji.Emoji;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.guild.Role;
import de.comroid.crystalshard.api.entity.guild.webhook.Webhook;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.event.message.MessageEvent;
import de.comroid.crystalshard.api.listener.message.MessageAttachableListener;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.model.channel.ChannelMention;
import de.comroid.crystalshard.api.model.message.MessageActivity;
import de.comroid.crystalshard.api.model.message.MessageAuthor;
import de.comroid.crystalshard.api.model.message.MessageFlags;
import de.comroid.crystalshard.api.model.message.MessageReference;
import de.comroid.crystalshard.api.model.message.MessageType;
import de.comroid.crystalshard.api.model.message.TextDecoration;
import de.comroid.crystalshard.api.model.message.embed.ActiveEmbed;
import de.comroid.crystalshard.api.model.message.embed.Embed;
import de.comroid.crystalshard.api.model.message.reaction.Reaction;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.core.api.cache.Cacheable;
import de.comroid.crystalshard.core.api.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.api.rest.HTTPStatusCodes;
import de.comroid.crystalshard.core.api.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.intellij.lang.annotations.MagicConstant;

import static de.comroid.crystalshard.core.api.cache.Cacheable.makeSubcacheableInfo;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.simple;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.underlying;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.underlyingMappingCollection;

@JsonTraits(Message.Trait.class)
public interface Message extends Snowflake, Cacheable, ListenerAttachable<MessageAttachableListener<? extends MessageEvent>> {
    @CacheInformation.Marker
    CacheInformation<TextChannel> CACHE_INFORMATION = makeSubcacheableInfo(TextChannel.class, Message::getChannel);
    
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
        return getTraitValue(Trait.MENTIONED_CHANNELS)
                .stream()
                .map(ChannelMention::getChannel)
                .collect(Collectors.toList());
    } // TODO do this with regexp

    @IntroducedBy(GETTER)
    default Collection<MessageAttachment> getAttachments() {
        return getTraitValue(Trait.ATTACHMENTS);
    }

    @IntroducedBy(GETTER)
    default Collection<ActiveEmbed> getEmbeds() {
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
        return wrapTraitValue(Trait.REFERENCED_MESSAGE)
                .flatMap(MessageReference::getMessage);
    }

    @IntroducedBy(GETTER)
    @MagicConstant(flagsFromClass = MessageFlags.class)
    default int getMessageFlags() {
        return getTraitValue(Trait.FLAGS);
    }

    interface Trait extends Snowflake.Trait {
        JsonBinding<Long, TextChannel> CHANNEL = cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asTextChannel));
        JsonBinding<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JsonBinding<JsonNode, User> USER_AUTHOR = underlying("author", User.class);
        JsonBinding<JsonNode, Webhook> WEBHOOK_AUTHOR = null; // todo
        JsonBinding<String, String> CONTENT = identity(JsonNode::asText, "content");
        JsonBinding<String, Instant> SENT_TIMESTAMP = simple(JsonNode::asText, "timestamp", Instant::parse);
        JsonBinding<String, Instant> EDITED_TIMESTAMP = simple(JsonNode::asText, "edited_timestamp", Instant::parse);
        JsonBinding<Boolean, Boolean> TTS = identity(JsonNode::asBoolean, "tts");
        JsonBinding<Boolean, Boolean> MENTIONS_EVERYONE = identity(JsonNode::asBoolean, "mention_everyone");
        JsonBinding<ArrayNode, Collection<User>> MENTIONED_USERS = underlyingMappingCollection("mentions", User.class);
        JsonBinding<ArrayNode, Collection<Role>> MENTIONED_ROLES = underlyingMappingCollection("mention_roles", Role.class, (api, data) -> api.getCacheManager()
                .getByID(Role.class, data.asLong())
                .orElseThrow());
        JsonBinding<ArrayNode, Collection<ChannelMention>> MENTIONED_CHANNELS = underlyingMappingCollection("mention_channels", ChannelMention.class);
        JsonBinding<ArrayNode, Collection<MessageAttachment>> ATTACHMENTS = underlyingMappingCollection("attachments", MessageAttachment.class);
        JsonBinding<ArrayNode, Collection<ActiveEmbed>> EMBEDS = underlyingMappingCollection("embeds", ActiveEmbed.class);
        JsonBinding<ArrayNode, Collection<Reaction>> CURRENT_REACTIONS = underlyingMappingCollection("reactions", Reaction.class);
        JsonBinding<Long, Snowflake> NONCE = simple(JsonNode::asLong, "nonce", id -> Adapter.require(Snowflake.class, id));
        JsonBinding<Boolean, Boolean> PINNED = identity(JsonNode::asBoolean, "pinned");
        JsonBinding<Integer, MessageType> TYPE = simple(JsonNode::asInt, "type", MessageType::valueOf);
        JsonBinding<JsonNode, MessageActivity> ACTIVITY = underlying("activity", MessageActivity.class);
        JsonBinding<JsonNode, MessageApplication> APPLICATION = underlying("application", MessageApplication.class);
        JsonBinding<JsonNode, MessageReference> REFERENCED_MESSAGE = underlying("message_reference", MessageReference.class);
        JsonBinding<Integer, Integer> FLAGS = identity(JsonNode::asInt, "flags");
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

    static Composer createComposer(TextChannel channel) {
        return Adapter.require(Composer.class, channel);
    }

    static BulkDeleter createBulkDeleter(TextChannel channel) {
        return Adapter.require(BulkDeleter.class, channel);
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
