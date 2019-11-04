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
import de.comroid.crystalshard.adapter.MainAPI;
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

import com.alibaba.fastjson.JSONObject;
import org.intellij.lang.annotations.MagicConstant;

import static de.comroid.crystalshard.core.api.cache.Cacheable.makeSubcacheableInfo;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.mappingCollection;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.serializableCollection;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.serialize;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.simple;

@MainAPI
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
        JsonBinding.TwoStage<Long, TextChannel> CHANNEL = cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asTextChannel));
        JsonBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JsonBinding.TwoStage<JSONObject, User> USER_AUTHOR = serialize("author", User.class);
        JsonBinding.TwoStage<JSONObject, Webhook> WEBHOOK_AUTHOR = serialize("webhook", Webhook.class);
        JsonBinding.OneStage<String> CONTENT = identity("content", JSONObject::getString);
        JsonBinding.TwoStage<String, Instant> SENT_TIMESTAMP = simple("timestamp", JSONObject::getString, Instant::parse);
        JsonBinding.TwoStage<String, Instant> EDITED_TIMESTAMP = simple("edited_timestamp", JSONObject::getString, Instant::parse);
        JsonBinding.OneStage<Boolean> TTS = identity("tts", JSONObject::getBoolean);
        JsonBinding.OneStage<Boolean> MENTIONS_EVERYONE = identity("mention_everyone", JSONObject::getBoolean);
        JsonBinding.TriStage<JSONObject, User> MENTIONED_USERS = serializableCollection("mentions", User.class);
        JsonBinding.TriStage<Long, Role> MENTIONED_ROLES = mappingCollection("mention_roles", JSONObject::getLong, (api, id) -> api.getCacheManager()
                .getByID(Role.class, id)
                .orElseThrow());
        JsonBinding.TriStage<JSONObject, ChannelMention> MENTIONED_CHANNELS = serializableCollection("mention_channels", ChannelMention.class);
        JsonBinding.TriStage<JSONObject, MessageAttachment> ATTACHMENTS = serializableCollection("attachments", MessageAttachment.class);
        JsonBinding.TriStage<JSONObject, ActiveEmbed> EMBEDS = serializableCollection("embeds", ActiveEmbed.class);
        JsonBinding.TriStage<JSONObject, Reaction> CURRENT_REACTIONS = serializableCollection("reactions", Reaction.class);
        JsonBinding.TwoStage<Long, Snowflake> NONCE = simple("nonce", JSONObject::getLong, id -> Adapter.require(Snowflake.class, id));
        JsonBinding.OneStage<Boolean> PINNED = identity("pinned", JSONObject::getBoolean);
        JsonBinding.TwoStage<Integer, MessageType> TYPE = simple("type", JSONObject::getInteger, MessageType::valueOf);
        JsonBinding.TwoStage<JSONObject, MessageActivity> ACTIVITY = serialize("activity", MessageActivity.class);
        JsonBinding.TwoStage<JSONObject, MessageApplication> APPLICATION = serialize("application", MessageApplication.class);
        JsonBinding.TwoStage<JSONObject, MessageReference> REFERENCED_MESSAGE = serialize("message_reference", MessageReference.class);
        JsonBinding.OneStage<Integer> FLAGS = identity("flags", JSONObject::getInteger);
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
