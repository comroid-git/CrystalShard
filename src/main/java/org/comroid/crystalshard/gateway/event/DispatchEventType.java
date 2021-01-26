package org.comroid.crystalshard.gateway.event;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Named;
import org.comroid.crystalshard.gateway.OpCode;
import org.comroid.crystalshard.gateway.event.dispatch.application.ApplicationCommandCreateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.application.ApplicationCommandDeleteEvent;
import org.comroid.crystalshard.gateway.event.dispatch.application.ApplicationCommandUpdateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.channel.ChannelCreateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.channel.ChannelDeleteEvent;
import org.comroid.crystalshard.gateway.event.dispatch.channel.ChannelUpdateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.channel.pins.ChannelPinsUpdateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.GuildCreateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.GuildDeleteEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.GuildUpdateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.ban.GuildBanAddEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.ban.GuildBanRemoveEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.emoji.GuildEmojisUpdateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.integrations.GuildIntegrationsUpdateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.invite.InviteCreateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.invite.InviteDeleteEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.member.GuildMemberAddEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.member.GuildMemberChunkEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.member.GuildMemberRemoveEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.member.GuildMemberUpdateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.role.GuildRoleCreateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.role.GuildRoleDeleteEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.role.GuildRoleUpdateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.interaction.InteractionCreateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.message.MessageCreateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.message.MessageDeleteBulkEvent;
import org.comroid.crystalshard.gateway.event.dispatch.message.MessageDeleteEvent;
import org.comroid.crystalshard.gateway.event.dispatch.message.MessageUpdateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.message.reaction.MessageReactionAddEvent;
import org.comroid.crystalshard.gateway.event.dispatch.message.reaction.MessageReactionRemoveAllEvent;
import org.comroid.crystalshard.gateway.event.dispatch.message.reaction.MessageReactionRemoveEmojiEvent;
import org.comroid.crystalshard.gateway.event.dispatch.message.reaction.MessageReactionRemoveEvent;
import org.comroid.crystalshard.gateway.event.dispatch.user.PresenceUpdateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.user.TypingStartEvent;
import org.comroid.crystalshard.gateway.event.dispatch.user.UserUpdateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.voice.VoiceServerUpdateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.voice.VoiceStateUpdateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.webhook.WebhooksUpdateEvent;
import org.comroid.crystalshard.gateway.event.generic.*;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public enum DispatchEventType implements Named, Predicate<UniNode> {
    // generic
    HELLO(HelloEvent::new),
    READY(ReadyEvent::new),
    RESUMED(ResumedEvent::new),
    RECONNECT(ReconnectEvent::new),
    INVALID_SESSION(InvalidSessionEvent::new),

    // channel related
    CHANNEL_CREATE(ChannelCreateEvent::new),
    CHANNEL_UPDATE(ChannelUpdateEvent::new),
    CHANNEL_DELETE(ChannelDeleteEvent::new),

    CHANNEL_PINS_UPDATE(ChannelPinsUpdateEvent::new),

    // guild related
    GUILD_CREATE(GuildCreateEvent::new),
    GUILD_UPDATE(GuildUpdateEvent::new),
    GUILD_DELETE(GuildDeleteEvent::new),

    GUILD_BAN_ADD(GuildBanAddEvent::new),
    GUILD_BAN_REMOVE(GuildBanRemoveEvent::new),

    GUILD_EMOJIS_UPDATE(GuildEmojisUpdateEvent::new),

    GUILD_INTEGRATIONS_UPDATE(GuildIntegrationsUpdateEvent::new),

    GUILD_MEMBER_ADD(GuildMemberAddEvent::new),
    GUILD_MEMBER_UPDATE(GuildMemberUpdateEvent::new),
    GUILD_MEMBER_REMOVE(GuildMemberRemoveEvent::new),
    GUILD_MEMBERS_CHUNK(GuildMemberChunkEvent::new),

    GUILD_ROLE_CREATE(GuildRoleCreateEvent::new),
    GUILD_ROLE_UPDATE(GuildRoleUpdateEvent::new),
    GUILD_ROLE_DELETE(GuildRoleDeleteEvent::new),

    INVITE_CREATE(InviteCreateEvent::new),
    INVITE_DELETE(InviteDeleteEvent::new),

    // message related
    MESSAGE_CREATE(MessageCreateEvent::new),
    MESSAGE_UPDATE(MessageUpdateEvent::new),
    MESSAGE_DELETE(MessageDeleteEvent::new),
    MESSAGE_DELETE_BULK(MessageDeleteBulkEvent::new),

    MESSAGE_REACTION_ADD(MessageReactionAddEvent::new),
    MESSAGE_REACTION_REMOVE(MessageReactionRemoveEvent::new),
    MESSAGE_REACTION_REMOVE_EMOJI(MessageReactionRemoveEmojiEvent::new),
    MESSAGE_REACTION_REMOVE_ALL(MessageReactionRemoveAllEvent::new),

    // application related
    APPLICATION_COMMAND_CREATE(ApplicationCommandCreateEvent::new),
    APPLICATION_COMMAND_UPDATE(ApplicationCommandUpdateEvent::new),
    APPLICATION_COMMAND_DELETE(ApplicationCommandDeleteEvent::new),

    // misc
    PRESENCE_UPDATE(PresenceUpdateEvent::new),
    TYPING_START(TypingStartEvent::new),
    USER_UPDATE(UserUpdateEvent::new),

    // voice related
    VOICE_STATE_UPDATE(VoiceStateUpdateEvent::new),
    VOICE_SERVER_UPDATE(VoiceServerUpdateEvent::new),

    // webhook
    WEBHOOKS_UPDATE(WebhooksUpdateEvent::new),

    INTERACTION_CREATE(InteractionCreateEvent::new);

    private final BiFunction<ContextualProvider, UniObjectNode, ? extends GatewayEvent> constructor;

    @Override
    public String getName() {
        return name();
    }

    DispatchEventType(BiFunction<ContextualProvider, UniObjectNode, ? extends GatewayEvent> constructor) {
        this.constructor = constructor;
    }

    public static Optional<DispatchEventType> find(UniNode data) {
        for (DispatchEventType type : values())
            if (type.test(data))
                return Optional.of(type);
        return Optional.empty();
    }

    @Override
    public boolean test(UniNode uniNode) {
        return OpCode.DISPATCH.test(uniNode) && uniNode
                .process("t")
                .map(UniNode::asString)
                .test(name()::equals);
    }

    public GatewayEvent createPayload(ContextualProvider context, UniObjectNode data) {
        return constructor.apply(context, data);
    }
}
