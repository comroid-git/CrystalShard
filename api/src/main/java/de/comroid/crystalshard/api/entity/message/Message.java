package de.comroid.crystalshard.api.entity.message;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.PrivateChannel;
import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.entity.emoji.Emoji;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.guild.Role;
import de.comroid.crystalshard.api.entity.guild.webhook.Webhook;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.listener.ListenerSpec;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.model.channel.ChannelMention;
import de.comroid.crystalshard.api.model.message.MessageActivity;
import de.comroid.crystalshard.api.model.message.MessageAuthor;
import de.comroid.crystalshard.api.model.message.MessageFlags;
import de.comroid.crystalshard.api.model.message.MessageReference;
import de.comroid.crystalshard.api.model.message.MessageType;
import de.comroid.crystalshard.api.model.message.Messageable;
import de.comroid.crystalshard.api.model.message.TextDecoration;
import de.comroid.crystalshard.api.model.message.embed.ActiveEmbed;
import de.comroid.crystalshard.api.model.message.embed.Embed;
import de.comroid.crystalshard.api.model.message.reaction.Reaction;
import de.comroid.crystalshard.core.cache.CacheManager;
import de.comroid.crystalshard.core.cache.Cacheable;
import de.comroid.crystalshard.core.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.rest.HTTPStatusCodes;
import de.comroid.crystalshard.core.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.BackTo;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;
import org.intellij.lang.annotations.MagicConstant;

import static de.comroid.crystalshard.core.cache.Cacheable.makeSubcacheableInfo;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.mappingCollection;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.serializableCollection;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.require;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.simple;

@MainAPI
@JSONBindingLocation(Message.JSON.class)
public interface Message extends Snowflake, Cacheable, ListenerAttachable<ListenerSpec.AttachableTo.Message> {
    @CacheInformation.Marker
    CacheInformation<TextChannel> CACHE_INFORMATION = makeSubcacheableInfo(TextChannel.class, Message::getChannel);
    
    @IntroducedBy(GETTER)
    default TextChannel getChannel() {
        return getBindingValue(JSON.CHANNEL);
    }

    @IntroducedBy(GETTER)
    default Optional<Guild> getGuild() {
        return wrapBindingValue(JSON.GUILD);
    }

    @IntroducedBy(GETTER)
    default MessageAuthor getAuthor() {
        return getBindingValue(JSON.USER_AUTHOR);
    }

    @IntroducedBy(GETTER)
    default String getContent() {
        return getBindingValue(JSON.CONTENT);
    }

    @IntroducedBy(GETTER)
    default Instant getSentTimestamp() {
        return getBindingValue(JSON.SENT_TIMESTAMP);
    }

    @IntroducedBy(GETTER)
    default Instant getEditedTimestamp() {
        return getBindingValue(JSON.EDITED_TIMESTAMP);
    }
    
    @IntroducedBy(GETTER)
    default boolean isTTS() {
        return getBindingValue(JSON.TTS);
    }

    @IntroducedBy(GETTER)
    default boolean isMentioningEveryone() {
        return getBindingValue(JSON.MENTIONS_EVERYONE);
    }

    @IntroducedBy(GETTER)
    default Collection<User> getMentionedUsers() {
        return getBindingValue(JSON.MENTIONED_USERS);
    }

    @IntroducedBy(GETTER)
    default Collection<Role> getMentionedRoles() {
        return getBindingValue(JSON.MENTIONED_ROLES);
    }

    @IntroducedBy(GETTER)
    default Collection<Channel> getMentionedChannels() {
        return getBindingValue(JSON.MENTIONED_CHANNELS)
                .stream()
                .map(ChannelMention::getChannel)
                .collect(Collectors.toList());
    } // TODO do this with regexp

    @IntroducedBy(GETTER)
    default Collection<MessageAttachment> getAttachments() {
        return getBindingValue(JSON.ATTACHMENTS);
    }

    @IntroducedBy(GETTER)
    default Collection<ActiveEmbed> getEmbeds() {
        return getBindingValue(JSON.EMBEDS);
    }

    @IntroducedBy(GETTER)
    default Collection<Reaction> getCurrentReactions() {
        return getBindingValue(JSON.CURRENT_REACTIONS);
    }

    @IntroducedBy(GETTER)
    default Optional<Snowflake> getNonce() {
        return wrapBindingValue(JSON.NONCE);
    }

    @IntroducedBy(GETTER)
    default boolean isPinned() {
        return getBindingValue(JSON.PINNED);
    }

    @IntroducedBy(GETTER)
    default MessageType getMessageType() {
        return getBindingValue(JSON.TYPE);
    }

    @IntroducedBy(GETTER)
    default Optional<MessageActivity> getMessageActivity() {
        return wrapBindingValue(JSON.ACTIVITY);
    }

    @IntroducedBy(GETTER)
    default Optional<MessageApplication> getMessageApplication() {
        return wrapBindingValue(JSON.APPLICATION);
    }

    @IntroducedBy(GETTER)
    default Optional<Message> getReferencedMessage() {
        return wrapBindingValue(JSON.REFERENCED_MESSAGE)
                .flatMap(MessageReference::getMessage);
    }

    @IntroducedBy(GETTER)
    @MagicConstant(flagsFromClass = MessageFlags.class)
    default int getMessageFlags() {
        return getBindingValue(JSON.FLAGS);
    }

    CompletableFuture<Void> removeAllReactions(); // todo

    Reaction.Remover removeReactions();

    default boolean isPrivate() {
        return getChannel() instanceof PrivateChannel;
    }

    interface JSON extends Snowflake.JSON {
        JSONBinding.TwoStage<Long, TextChannel> CHANNEL = cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asTextChannel));
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.TwoStage<JSONObject, User> USER_AUTHOR = require("author", User.class);
        JSONBinding.TwoStage<JSONObject, Webhook> WEBHOOK_AUTHOR = require("webhook", Webhook.class);
        JSONBinding.OneStage<String> CONTENT = identity("content", JSONObject::getString);
        JSONBinding.TwoStage<String, Instant> SENT_TIMESTAMP = simple("timestamp", JSONObject::getString, Instant::parse);
        JSONBinding.TwoStage<String, Instant> EDITED_TIMESTAMP = simple("edited_timestamp", JSONObject::getString, Instant::parse);
        JSONBinding.OneStage<Boolean> TTS = identity("tts", JSONObject::getBoolean);
        JSONBinding.OneStage<Boolean> MENTIONS_EVERYONE = identity("mention_everyone", JSONObject::getBoolean);
        JSONBinding.TriStage<JSONObject, User> MENTIONED_USERS = serializableCollection("mentions", User.class);
        JSONBinding.TriStage<Long, Role> MENTIONED_ROLES = mappingCollection("mention_roles", JSONObject::getLong, (api, id) -> api.getCacheManager()
                .getByID(Role.class, id)
                .orElseThrow());
        JSONBinding.TriStage<JSONObject, ChannelMention> MENTIONED_CHANNELS = serializableCollection("mention_channels", ChannelMention.class);
        JSONBinding.TriStage<JSONObject, MessageAttachment> ATTACHMENTS = serializableCollection("attachments", MessageAttachment.class);
        JSONBinding.TriStage<JSONObject, ActiveEmbed> EMBEDS = serializableCollection("embeds", ActiveEmbed.class);
        JSONBinding.TriStage<JSONObject, Reaction> CURRENT_REACTIONS = serializableCollection("reactions", Reaction.class);
        JSONBinding.TwoStage<Long, Snowflake> NONCE = simple("nonce", JSONObject::getLong, id -> Adapter.require(Snowflake.class, id));
        JSONBinding.OneStage<Boolean> PINNED = identity("pinned", JSONObject::getBoolean);
        JSONBinding.TwoStage<Integer, MessageType> TYPE = simple("type", JSONObject::getInteger, MessageType::valueOf);
        JSONBinding.TwoStage<JSONObject, MessageActivity> ACTIVITY = require("activity", MessageActivity.class);
        JSONBinding.TwoStage<JSONObject, MessageApplication> APPLICATION = require("application", MessageApplication.class);
        JSONBinding.TwoStage<JSONObject, MessageReference> REFERENCED_MESSAGE = require("message_reference", MessageReference.class);
        JSONBinding.OneStage<Integer> FLAGS = identity("flags", JSONObject::getInteger);
    }
    
    @IntroducedBy(API)
    CompletableFuture<Reaction> addReaction(Emoji emoji);

    @IntroducedBy(API)
    default CompletableFuture<Void> delete() {
        return Adapter.<Void>request(getAPI())
                .endpoint(DiscordEndpoint.MESSAGE_SPECIFIC, getChannel().getID(), getID())
                .method(RestMethod.DELETE)
                .expectCode(HTTPStatusCodes.NO_CONTENT)
                .executeAsObject(data -> getAPI().getCacheManager()
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

    static CompletableFuture<Message> request(Discord api, long channelId, long messageId) {
        return Adapter.<Message>request(api)
                .endpoint(DiscordEndpoint.MESSAGE_SPECIFIC, channelId, messageId)
                .method(RestMethod.GET)
                .executeAsObject(json -> Adapter.require(Message.class, api, json));
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

        <ComposerPair extends Embed.Composer & BackTo<Composer>> ComposerPair composeEmbed();

        Optional<MessageAttachment> getAttachment();

        Composer setAttachment(File file);

        Composer setAttachment(URL url);

        Composer setAttachment(InputStream inputStream);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#create-message")
        CompletableFuture<Message> send();
        
        CompletableFuture<Message> send(TextChannel to);
    }

    @IntroducedBy(PRODUCTION)
    interface Editor {
        Optional<String> getContent();

        Editor setText(String content);

        Optional<Embed> getEmbed();

        Editor setEmbed(Embed embed);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#edit-message")
        CompletableFuture<Message> edit();
    }
}
