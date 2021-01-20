package org.comroid.crystalshard.entity.message;

import org.comroid.api.*;
import org.comroid.common.info.Described;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.channel.TextChannel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.entity.webhook.Webhook;
import org.comroid.crystalshard.model.message.MessageActivity;
import org.comroid.crystalshard.model.message.MessageReference;
import org.comroid.crystalshard.model.message.Reaction;
import org.comroid.crystalshard.model.message.embed.Embed;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class Message extends Snowflake.Abstract {
    @RootBind
    public static final GroupBind<Message> TYPE
            = BASETYPE.subGroup("message", Message::resolve);
    public static final VarBind<Message, Long, Channel, Channel> CHANNEL
            = TYPE.createBind("channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((message, id) -> message.requireFromContext(SnowflakeCache.class).getChannel(id))
            .build();
    public static final VarBind<Message, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((message, id) -> message.requireFromContext(SnowflakeCache.class).getGuild(id))
            .build();
    public static final VarBind<Message, UniObjectNode, User, User> AUTHOR
            = TYPE.createBind("author")
            .extractAsObject() // todo: handle WebHook author case
            .andResolve(User::resolve)
            .build();
    public static final VarBind<Message, UniObjectNode, UniObjectNode, UniObjectNode> MEMBER
            = TYPE.createBind("member")
            .extractAsObject()
            .asIdentities() // todo GuildMember stuff
            .build();
    public static final VarBind<Message, String, String, String> CONTENT
            = TYPE.createBind("content")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Message, String, Instant, Instant> TIMESTAMP
            = TYPE.createBind("timestamp")
            .extractAs(StandardValueType.STRING)
            .andRemap(Instant::parse)
            .build();
    public static final VarBind<Message, String, Instant, Instant> EDITED_TIMESTAMP
            = TYPE.createBind("edited_timestamp")
            .extractAs(StandardValueType.STRING)
            .andRemap(Instant::parse)
            .build();
    public static final VarBind<Message, Boolean, Boolean, Boolean> TTS
            = TYPE.createBind("tts")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Message, Boolean, Boolean, Boolean> MENTIONS_EVERYONE
            = TYPE.createBind("mention_everyone")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Message, UniObjectNode, User, Span<User>> MENTIONED_USERS
            = TYPE.createBind("mentions")
            .extractAsArray()
            .andResolve(User::resolve)
            .intoSpan()
            .build();
    public static final VarBind<Message, UniObjectNode, Role, Span<Role>> MENTIONED_ROLES
            = TYPE.createBind("mention_roles")
            .extractAsArray()
            .andResolve(Role::resolve)
            .intoSpan()
            .build();
    public static final VarBind<Message, UniObjectNode, UniObjectNode, Span<UniObjectNode>> MENTIONED_CHANNELS
            = TYPE.createBind("mention_channels")
            .extractAsArray()
            .asIdentities() // todo Handle ChannelMention Object
            .intoSpan()
            .build();
    public static final VarBind<Message, UniObjectNode, MessageAttachment, Span<MessageAttachment>> ATTACHMENTS
            = TYPE.createBind("attachments")
            .extractAsArray()
            .andResolve(MessageAttachment::resolve)
            .intoSpan()
            .build();
    public static final VarBind<Message, UniObjectNode, Embed, Span<Embed>> EMBEDS
            = TYPE.createBind("embeds")
            .extractAsArray()
            .andResolve(Embed::new)
            .intoSpan()
            .build();
    public static final VarBind<Message, UniObjectNode, Reaction, Span<Reaction>> REACTIONS
            = TYPE.createBind("reactions")
            .extractAsArray()
            .andResolve((msg, obj) -> msg.reactionsCache.computeIfAbsent(
                    obj.process("id")
                            .or(() -> obj.get("name"))
                            .map(UniNode::asString)
                            .assertion("Invalid data: " + obj),
                    k -> new Reaction(msg, obj)))
            .intoSpan()
            .build();
    public static final VarBind<Message, String, String, String> NONCE
            = TYPE.createBind("nonce")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Message, Boolean, Boolean, Boolean> PINNED
            = TYPE.createBind("pinned")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Message, Long, Webhook, Webhook> WEBHOOK_AUTHOR
            = TYPE.createBind("webhook_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((msg, id) -> msg.requireFromContext(SnowflakeCache.class).getSnowflake(EntityType.WEBHOOK, id))
            .build();
    public static final VarBind<Message, Integer, Type, Type> MESSAGE_TYPE
            = TYPE.createBind("type")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(Type::valueOf)
            .build();
    public static final VarBind<Message, UniObjectNode, MessageActivity, MessageActivity> ACTIVITY
            = TYPE.createBind("activity")
            .extractAsObject()
            .andResolve(MessageActivity::new)
            .build();
    public static final VarBind<Message, UniObjectNode, MessageApplication, MessageApplication> APPLICATION
            = TYPE.createBind("application")
            .extractAsObject()
            .andResolve(MessageApplication::resolve)
            .build();
    public static final VarBind<Message, UniObjectNode, MessageReference, MessageReference> REFERENCE
            = TYPE.createBind("message_reference")
            .extractAsObject()
            .andResolve(MessageReference::new)
            .build();
    public static final VarBind<Message, Integer, Set<Flags>, Set<Flags>> FLAGS
            = TYPE.createBind("flags")
            .extractAs(StandardValueType.INTEGER)
            .andRemap(Flags::valueOf)
            .build();
    public static final VarBind<Message, UniObjectNode, MessageSticker, Span<MessageSticker>> STICKERS
            = TYPE.createBind("stickers")
            .extractAsArray()
            .andResolve(MessageSticker::resolve)
            .intoSpan()
            .build();
    public static final VarBind<Message, UniObjectNode, Message, Message> REFERENCED_MESSAGE
            = TYPE.createBind("referenced_message")
            .extractAsObject()
            .andResolve(Message::resolve)
            .build();
    private final Map<String, Reaction> reactionsCache = new ConcurrentHashMap<>();
    public final Reference<Channel> channel = getComputedReference(CHANNEL);
    public final Reference<Guild> guild = getComputedReference(GUILD);
    public final Reference<User> author = getComputedReference(AUTHOR);
    public final Reference<String> content = getComputedReference(CONTENT);
    public final Reference<Instant> timestamp = getComputedReference(TIMESTAMP);
    public final Reference<Instant> editedTimestamp = getComputedReference(EDITED_TIMESTAMP);
    public final Reference<Boolean> isTTS = getComputedReference(TTS);
    public final Reference<Boolean> mentionsEveryone = getComputedReference(MENTIONS_EVERYONE);
    public final Reference<Span<User>> userMentions = getComputedReference(MENTIONED_USERS);
    public final Reference<Span<Role>> roleMentions = getComputedReference(MENTIONED_ROLES);
    public final Reference<Span<UniObjectNode>> channelMentions = getComputedReference(MENTIONED_CHANNELS); // todo
    public final Reference<Span<MessageAttachment>> attachments = getComputedReference(ATTACHMENTS);
    public final Reference<Span<Embed>> embeds = getComputedReference(EMBEDS);
    public final Reference<Span<Reaction>> reactions = getComputedReference(REACTIONS);
    public final Reference<Boolean> isPinned = getComputedReference(PINNED);
    public final Reference<Type> messageType = getComputedReference(MESSAGE_TYPE);
    public final Reference<MessageActivity> activity = getComputedReference(ACTIVITY);
    public final Reference<MessageApplication> application = getComputedReference(APPLICATION);
    public final Reference<MessageReference> messageReference = getComputedReference(REFERENCE);
    public final Reference<Set<Flags>> flags = getComputedReference(FLAGS);
    public final Reference<Span<MessageSticker>> stickers = getComputedReference(STICKERS);

    public TextChannel getChannel() {
        return channel.flatMap(TextChannel.class).assertion();
    }

    public Guild getGuild() {
        return guild.assertion();
    }

    public User getUserAuthor() {
        return author.assertion();
    }

    public String getContent() {
        return content.assertion();
    }

    public Instant getTimestamp() {
        return timestamp.assertion();
    }

    public Instant getEditedTimestamp() {
        return editedTimestamp.assertion();
    }

    public boolean isTTS() {
        return isTTS.assertion();
    }

    public boolean mentionsEveryone() {
        return mentionsEveryone.assertion();
    }

    public Span<User> getUserMentions() {
        return userMentions.orElseGet(Span::empty);
    }

    public Span<Role> getRoleMentions() {
        return roleMentions.orElseGet(Span::empty);
    }

    public Span<UniObjectNode> getChannelMentions() {
        return channelMentions.orElseGet(Span::empty);
    }

    public Span<MessageAttachment> getAttachments() {
        return attachments.orElseGet(Span::empty);
    }

    public Span<Embed> getEmbeds() {
        return embeds.orElseGet(Span::empty);
    }

    public Span<Reaction> getReactions() {
        return reactions.orElseGet(Span::empty);
    }

    public boolean isPinned() {
        return isPinned.assertion();
    }

    public Type getMessageType() {
        return messageType.assertion();
    }

    public MessageActivity getActivity() {
        return activity.assertion();
    }

    public MessageApplication getApplication() {
        return application.assertion();
    }

    public MessageReference getMessageReference() {
        return messageReference.assertion();
    }

    public Set<Flags> getFlags() {
        return flags.assertion();
    }

    public Span<MessageSticker> getStickers() {
        return stickers.orElseGet(Span::empty);
    }

    private Message(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.MESSAGE);
    }

    public static Message resolve(ContextualProvider context, UniNode data) {
        return Snowflake.resolve(context, data, SnowflakeCache::getMessage, Message::new);
    }

    public enum Type implements IntEnum, Named {
        DEFAULT(0),
        RECIPIENT_ADD(1),
        RECIPIENT_REMOVE(2),
        CALL(3),
        CHANNEL_NAME_CHANGE(4),
        CHANNEL_ICON_CHANGE(5),
        CHANNEL_PINNED_MESSAGE(6),
        GUILD_MEMBER_JOIN(7),
        USER_PREMIUM_GUILD_SUBSCRIPTION(8),
        USER_PREMIUM_GUILD_SUBSCRIPTION_TIER_1(9),
        USER_PREMIUM_GUILD_SUBSCRIPTION_TIER_2(10),
        USER_PREMIUM_GUILD_SUBSCRIPTION_TIER_3(11),
        CHANNEL_FOLLOW_ADD(12),
        GUILD_DISCOVERY_DISQUALIFIED(14),
        GUILD_DISCOVERY_REQUALIFIED(15),
        REPLY(19),
        APPLICATION_COMMAND(20);

        private final int value;

        @Override
        public @NotNull Integer getValue() {
            return value;
        }

        Type(int value) {
            this.value = value;
        }

        public static Rewrapper<Type> valueOf(int value) {
            return IntEnum.valueOf(value, Type.class);
        }
    }

    @SuppressWarnings("PointlessBitwiseExpression")
    public enum Flags implements BitmaskEnum<Flags>, Named, Described {
        CROSSPOSTED(1 << 0, "this message has been published to subscribed channels (via Channel Following)"),
        IS_CROSSPOST(1 << 1, "this message originated from a message in another channel (via Channel Following)"),
        SUPPRESS_EMBEDS(1 << 2, "do not include any embeds when serializing this message"),
        SOURCE_MESSAGE_DELETED(1 << 3, "the source message for this crosspost has been deleted (via Channel Following)"),
        URGENT(1 << 4, "this message came from the urgent message system");

        private final int value;
        private final String description;

        @Override
        public @NotNull Integer getValue() {
            return value;
        }

        @Override
        public String getDescription() {
            return description;
        }

        Flags(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public static Set<Flags> valueOf(int mask) {
            return BitmaskEnum.valueOf(mask, Flags.class);
        }
    }
}
