package org.comroid.crystalshard.entity.webhook;

import org.comroid.api.*;
import org.comroid.common.info.Described;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.DiscordREST;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.entity.user.SimpleUser;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.model.message.MessageBuilder;
import org.comroid.crystalshard.model.message.MessageTarget;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.mutatio.model.Ref;
import org.comroid.mutatio.ref.Reference;
import org.comroid.restless.REST;
import org.comroid.restless.body.BodyBuilderType;
import org.comroid.restless.endpoint.QueryParameter;
import org.comroid.uniform.SerializationAdapter;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Webhook extends Snowflake.Abstract implements Named, MessageTarget {
    @Language("RegExp")
    public static final String TOKEN_REGEX = "[a-zA-Z_\\-\\d]{16,}";
    public static final Pattern URL_PATTERN = Pattern.compile("https://discord.com/api/webhooks/(?<id>\\d{12,32})/(?<token>" + TOKEN_REGEX + ")");
    @RootBind
    public static final GroupBind<Webhook> TYPE
            = BASETYPE.subGroup("webhook", Webhook::resolve);
    public static final VarBind<Webhook, Integer, Type, Type> HOOK_TYPE
            = TYPE.createBind("type")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(Type::valueOf)
            .build();
    public static final VarBind<Webhook, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((hook, id) -> hook.getCache().getGuild(id))
            .build();
    public static final VarBind<Webhook, Long, Channel, Channel> CHANNEL
            = TYPE.createBind("channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((hook, id) -> hook.getCache().getChannel(id))
            .build();
    public static final VarBind<Webhook, UniObjectNode, User, User> CREATOR
            = TYPE.createBind("user")
            .extractAsObject()
            .andResolve(SimpleUser::resolve)
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
    public final Reference<String> token = getComputedReference(TOKEN);

    private Webhook(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.WEBHOOK);
    }

    public static Webhook resolve(ContextualProvider context, UniNode data) {
        return Snowflake.resolve(context, data, SnowflakeCache::getWebhook, Webhook::new);
    }

    public static Webhook fromURL(DiscordAPI api, String url) {
        final Matcher matcher = URL_PATTERN.matcher(url);

        if (matcher.matches()) {
            long id = Long.parseLong(Polyfill.regexGroupOrDefault(matcher, "id", "0"));

            Ref<Webhook> cached = api.getCache().getWebhook(id);
            if (cached.isNonNull())
                return cached.assertion();

            String token = Polyfill.regexGroupOrDefault(matcher, "token", null);

            if (id == 0 || token == null)
                throw new IllegalArgumentException("Could not extract parameters from URL: " + url);

            UniObjectNode obj = api.requireFromContext(SerializationAdapter.class).createObjectNode();
            obj.put(ID, id);
            obj.put(TOKEN, token);

            return Webhook.resolve(api, obj);
        } else throw new IllegalArgumentException("Invalid URL: " + url);
    }

    @Override
    public CompletableFuture<Message> executeMessage(MessageBuilder builder) {
        return token.ifPresentMapOrElseGet(
                token -> requireFromContext(DiscordAPI.class)
                        .getREST()
                        .request(Message.TYPE)
                        .endpoint(Endpoint.EXECUTE_WEBHOOK, getID(), token, QueryParameter.param("wait", true))
                        .method(REST.Method.POST)
                        .addHeaders(DiscordREST.createHeaders(null))
                        .buildBody(BodyBuilderType.OBJECT, builder)
                        .execute$deserializeSingle(),
                () -> Polyfill.failedFuture(new NoSuchElementException("Token is missing"))
        );
    }

    public enum Type implements IntegerAttribute, Named, Described {
        INCOMING(1, "Incoming Webhooks can post messages to channels with a generated token"),
        CHANNEL(2, "Follower	Channel Follower Webhooks are internal webhooks used with Channel Following to post new messages into channels");

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

        Type(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public static Rewrapper<Type> valueOf(int value) {
            return IntegerAttribute.valueOf(value, Type.class);
        }
    }
}
