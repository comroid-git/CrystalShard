package org.comroid.crystalshard.entity.message;

import org.comroid.api.*;
import org.comroid.common.info.Described;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.entity.webhook.Webhook;
import org.comroid.crystalshard.model.message.MessageActivity;
import org.comroid.crystalshard.model.message.MessageReference;
import org.comroid.crystalshard.model.message.Reaction;
import org.comroid.crystalshard.model.message.embed.Embed;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class Message extends Snowflake.Abstract {
    @RootBind
    public static final GroupBind<Message> TYPE
            = BASETYPE.rootGroup("message");
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
            .andProvideRef(User.ID, (msg, id) -> msg.requireFromContext(SnowflakeCache.class).getUser(id), User.TYPE)
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
            .andProvideRef(User.ID, (msg, id) -> msg.requireFromContext(SnowflakeCache.class).getUser(id), User.TYPE)
            .intoSpan()
            .build();
    public static final VarBind<Message, UniObjectNode, Role, Span<Role>> MENTIONED_ROLES
            = TYPE.createBind("mention_roles")
            .extractAsArray()
            .andProvideRef(Role.ID, (msg, id) -> msg.requireFromContext(SnowflakeCache.class).getRole(id), Role.TYPE)
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
            .andResolve((msg, obj) -> msg.reactions.computeIfAbsent(
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
            .andProvideRef(MessageApplication.ID, (msg, id) -> msg.requireFromContext(SnowflakeCache.class).getSnowflake(EntityType.MESSAGE_APPLICATION, id), MessageApplication.TYPE)
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
            .andProvideRef(MessageSticker.ID, (msg, id) -> msg.requireFromContext(SnowflakeCache.class).getSnowflake(EntityType.MESSAGE_STICKER, id), MessageSticker.TYPE)
            .intoSpan()
            .build();
    public static final VarBind<Message, UniObjectNode, Message, Message> REFERENCED_MESSAGE
            = TYPE.createBind("referenced_message")
            .extractAsObject()
            .andProvideRef(Message.ID, (msg, id) -> msg.requireFromContext(SnowflakeCache.class).getMessage(id), Message.TYPE)
            .build();
    private final Map<String, Reaction> reactions = new ConcurrentHashMap<>();

    private Message(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.MESSAGE);
    }

    public static Message resolve(ContextualProvider context, UniObjectNode data) {
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
        public int getValue() {
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
        public int getValue() {
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
