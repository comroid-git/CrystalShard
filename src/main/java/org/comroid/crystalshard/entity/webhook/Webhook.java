package org.comroid.crystalshard.entity.webhook;

import org.comroid.api.ContextualProvider;
import org.comroid.api.IntEnum;
import org.comroid.api.Named;
import org.comroid.api.Rewrapper;
import org.comroid.common.info.Described;
import org.comroid.crystalshard.SnowflakeCache;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.bind.builder.BuilderStep3$Finishing;

public final class Webhook extends Snowflake.Abstract {
    @RootBind
    public static final GroupBind<Webhook> TYPE
            = BASETYPE.rootGroup("webhook");
    public static final VarBind<Webhook, Integer, Type, Type> HOOK_TYPE
            = TYPE.createBind("type")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(Type::valueOf)
            .build();
    public static final VarBind<Webhook, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((hook, id) -> hook.requireFromContext(SnowflakeCache.class).getGuild(id))
            .build();
    public static final VarBind<Webhook, Long, Channel, Channel> CHANNEL
            = TYPE.createBind("channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((hook, id) -> hook.requireFromContext(SnowflakeCache.class).getChannel(id))
            .build();
    public static final VarBind<Webhook, UniObjectNode, User, User> CREATOR
            = TYPE.createBind("user")
            .extractAsObject()
            .andProvideRef(User.ID, (hook, id) -> hook.requireFromContext(SnowflakeCache.class).getUser(id), User.TYPE)
            .build();
    public static final VarBind<Webhook, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Webhook, String, String, String> AVATAR
            = TYPE.createBind("avatar")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Webhook, String, String, String> TOKEN
            = TYPE.createBind("token")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Webhook, Long, Long, Long> CREATOR_APP
            = TYPE.createBind("application_id")
            .extractAs(StandardValueType.LONG)
            .build();

    protected Webhook(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.WEBHOOK);
    }

    public enum Type implements IntEnum, Named, Described {
        INCOMING(1, "Incoming Webhooks can post messages to channels with a generated token"),
        CHANNEL(2, "Follower	Channel Follower Webhooks are internal webhooks used with Channel Following to post new messages into channels");

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

        Type(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public static Rewrapper<Type> valueOf(int value) {
            return IntEnum.valueOf(value, Type.class);
        }
    }
}
