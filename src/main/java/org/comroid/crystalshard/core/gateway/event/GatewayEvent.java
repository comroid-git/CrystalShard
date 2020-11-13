package org.comroid.crystalshard.core.gateway.event;

import org.comroid.api.Polyfill;
import org.comroid.crystalshard.DiscordAPI.Intent;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.payload.channel.*;
import org.comroid.crystalshard.core.gateway.payload.generic.*;
import org.comroid.crystalshard.core.gateway.payload.guild.*;
import org.comroid.crystalshard.core.gateway.payload.guild.ban.GatewayGuildBanAddPayload;
import org.comroid.crystalshard.core.gateway.payload.guild.ban.GatewayGuildBanRemovePayload;
import org.comroid.crystalshard.core.gateway.payload.guild.emoji.GatewayGuildEmojisUpdatePayload;
import org.comroid.crystalshard.core.gateway.payload.guild.integration.GatewayGuildIntegrationsUpdatePayload;
import org.comroid.crystalshard.core.gateway.payload.guild.invite.GatewayGuildInviteCreatePayload;
import org.comroid.crystalshard.core.gateway.payload.guild.invite.GatewayGuildInviteDeletePayload;
import org.comroid.crystalshard.core.gateway.payload.guild.member.GatewayGuildMemberAddPayload;
import org.comroid.crystalshard.core.gateway.payload.guild.member.GatewayGuildMemberRemovePayload;
import org.comroid.crystalshard.core.gateway.payload.guild.member.GatewayGuildMemberUpdatePayload;
import org.comroid.crystalshard.core.gateway.payload.guild.member.GatewayGuildMembersChunkPayload;
import org.comroid.crystalshard.core.gateway.payload.guild.role.GatewayGuildRoleCreatePayload;
import org.comroid.crystalshard.core.gateway.payload.guild.role.GatewayGuildRoleDeletePayload;
import org.comroid.crystalshard.core.gateway.payload.guild.role.GatewayGuildRoleUpdatePayload;
import org.comroid.crystalshard.core.gateway.payload.message.GatewayMessageCreatePayload;
import org.comroid.crystalshard.core.gateway.payload.message.GatewayMessageDeleteBulkPayload;
import org.comroid.crystalshard.core.gateway.payload.message.GatewayMessageDeletePayload;
import org.comroid.crystalshard.core.gateway.payload.message.GatewayMessageUpdatePayload;
import org.comroid.crystalshard.core.gateway.payload.message.reaction.GatewayMessageReactionAddPayload;
import org.comroid.crystalshard.core.gateway.payload.message.reaction.GatewayMessageReactionRemoveAllPayload;
import org.comroid.crystalshard.core.gateway.payload.message.reaction.GatewayMessageReactionRemoveEmojiPayload;
import org.comroid.crystalshard.core.gateway.payload.message.reaction.GatewayMessageReactionRemovePayload;
import org.comroid.crystalshard.core.gateway.payload.user.GatewayUserUpdatePayload;
import org.comroid.crystalshard.core.gateway.payload.voice.GatewayVoiceServerUpdatePayload;
import org.comroid.crystalshard.core.gateway.payload.voice.GatewayVoiceStateUpdatePayload;
import org.comroid.listnr.EventType;
import org.comroid.mutatio.proc.Processor;
import org.comroid.mutatio.span.Span;
import org.comroid.restless.socket.event.WebSocketEvent;
import org.comroid.restless.socket.event.WebSocketPayload;
import org.comroid.trie.TrieMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public final class GatewayEvent<GP extends GatewayPayload> implements EventType<DiscordBot, WebSocketPayload.Data, GP> {
    public static final GatewayEvent<GatewayHelloPayload> HELLO
            = new GatewayEvent<>("HELLO", GatewayHelloPayload::new);
    public static final GatewayEvent<GatewayReadyPayload> READY
            = new GatewayEvent<>("READY", GatewayReadyPayload::new);
    public static final GatewayEvent<GatewayResumedPayload> RESUMED
            = new GatewayEvent<>("RESUMED", GatewayResumedPayload::new);
    public static final GatewayEvent<GatewayReconnectPayload> RECONNECT
            = new GatewayEvent<>("RECONNECT", GatewayReconnectPayload::new);
    public static final GatewayEvent<GatewayInvalidSessionPayload> INVALID_SESSION
            = new GatewayEvent<>("INVALID_SESSION", GatewayInvalidSessionPayload::new);
    public static final GatewayEvent<GatewayChannelCreatePayload> CHANNEL_CREATE
            = new GatewayEvent<>("CHANNEL_CREATE", GatewayChannelCreatePayload::new, Intent.GUILDS, Intent.DIRECT_MESSAGES);
    public static final GatewayEvent<GatewayChannelUpdatePayload> CHANNEL_UPDATE
            = new GatewayEvent<>("CHANNEL_UPDATE", GatewayChannelUpdatePayload::new, Intent.GUILDS);
    public static final GatewayEvent<GatewayChannelDeletePayload> CHANNEL_DELETE
            = new GatewayEvent<>("CHANNEL_DELETE", GatewayChannelDeletePayload::new, Intent.GUILDS);
    public static final GatewayEvent<GatewayChannelPinsUpdatePayload> CHANNEL_PINS_UPDATE
            = new GatewayEvent<>("CHANNEL_PINS_UPDATE", GatewayChannelPinsUpdatePayload::new, Intent.GUILDS, Intent.DIRECT_MESSAGES);
    public static final GatewayEvent<GatewayGuildCreatePayload> GUILD_CREATE
            = new GatewayEvent<>("GUILD_CREATE", GatewayGuildCreatePayload::new, Intent.GUILDS);
    public static final GatewayEvent<GatewayGuildUpdatePayload> GUILD_UPDATE
            = new GatewayEvent<>("GUILD_UPDATE", GatewayGuildUpdatePayload::new, Intent.GUILDS);
    public static final GatewayEvent<GatewayGuildDeletePayload> GUILD_DELETE
            = new GatewayEvent<>("GUILD_DELETE", GatewayGuildDeletePayload::new, Intent.GUILDS);
    public static final GatewayEvent<GatewayGuildBanAddPayload> GUILD_BAN_ADD
            = new GatewayEvent<>("GUILD_BAN_ADD", GatewayGuildBanAddPayload::new, Intent.GUILD_BANS);
    public static final GatewayEvent<GatewayGuildBanRemovePayload> GUILD_BAN_REMOVE
            = new GatewayEvent<>("GUILD_BAN_REMOVE", GatewayGuildBanRemovePayload::new, Intent.GUILD_BANS);
    public static final GatewayEvent<GatewayGuildEmojisUpdatePayload> GUILD_EMOJIS_UPDATE
            = new GatewayEvent<>("GUILD_EMOJIS_UPDATE", GatewayGuildEmojisUpdatePayload::new, Intent.GUILD_EMOJIS);
    public static final GatewayEvent<GatewayGuildIntegrationsUpdatePayload> GUILD_INTEGRATIONS_UPDATE
            = new GatewayEvent<>("GUILD_INTEGRATIONS_UPDATE", GatewayGuildIntegrationsUpdatePayload::new, Intent.GUILD_INTEGRATIONS);
    public static final GatewayEvent<GatewayGuildMemberAddPayload> GUILD_MEMBER_ADD
            = new GatewayEvent<>("GUILD_MEMBER_ADD", GatewayGuildMemberAddPayload::new, Intent.GUILD_MEMBERS);
    public static final GatewayEvent<GatewayGuildMemberRemovePayload> GUILD_MEMBER_REMOVE
            = new GatewayEvent<>("GUILD_MEMBER_REMOVE", GatewayGuildMemberRemovePayload::new, Intent.GUILD_MEMBERS);
    public static final GatewayEvent<GatewayGuildMemberUpdatePayload> GUILD_MEMBER_UPDATE
            = new GatewayEvent<>("GUILD_MEMBER_UPDATE", GatewayGuildMemberUpdatePayload::new, Intent.GUILD_MEMBERS);
    public static final GatewayEvent<GatewayGuildMembersChunkPayload> GUILD_MEMBERS_CHUNK
            = new GatewayEvent<>("GUILD_MEMBERS_CHUNK", GatewayGuildMembersChunkPayload::new, Intent.GUILD_MEMBERS);
    public static final GatewayEvent<GatewayGuildRoleCreatePayload> GUILD_ROLE_CREATE
            = new GatewayEvent<>("GUILD_ROLE_CREATE", GatewayGuildRoleCreatePayload::new, Intent.GUILDS);
    public static final GatewayEvent<GatewayGuildRoleUpdatePayload> GUILD_ROLE_UPDATE
            = new GatewayEvent<>("GUILD_ROLE_UPDATE", GatewayGuildRoleUpdatePayload::new, Intent.GUILDS);
    public static final GatewayEvent<GatewayGuildRoleDeletePayload> GUILD_ROLE_DELETE
            = new GatewayEvent<>("GUILD_ROLE_DELETE", GatewayGuildRoleDeletePayload::new, Intent.GUILDS);
    public static final GatewayEvent<GatewayGuildInviteCreatePayload> INVITE_CREATE
            = new GatewayEvent<>("INVITE_CREATE", GatewayGuildInviteCreatePayload::new, Intent.GUILD_INVITES);
    public static final GatewayEvent<GatewayGuildInviteDeletePayload> INVITE_DELETE
            = new GatewayEvent<>("INVITE_DELETE", GatewayGuildInviteDeletePayload::new, Intent.GUILD_INVITES);
    public static final GatewayEvent<GatewayMessageCreatePayload> MESSAGE_CREATE
            = new GatewayEvent<>("MESSAGE_CREATE", GatewayMessageCreatePayload::new, Intent.DIRECT_MESSAGES, Intent.GUILD_MESSAGES);
    public static final GatewayEvent<GatewayMessageUpdatePayload> MESSAGE_UPDATE
            = new GatewayEvent<>("MESSAGE_UPDATE", GatewayMessageUpdatePayload::new, Intent.DIRECT_MESSAGES, Intent.GUILD_MESSAGES);
    public static final GatewayEvent<GatewayMessageDeletePayload> MESSAGE_DELETE
            = new GatewayEvent<>("MESSAGE_DELETE", GatewayMessageDeletePayload::new, Intent.DIRECT_MESSAGES, Intent.GUILD_MESSAGES);
    public static final GatewayEvent<GatewayMessageDeleteBulkPayload> MESSAGE_DELETE_BULK
            = new GatewayEvent<>("MESSAGE_DELETE_BULK", GatewayMessageDeleteBulkPayload::new, Intent.GUILD_MESSAGES);
    public static final GatewayEvent<GatewayMessageReactionAddPayload> MESSAGE_REACTION_ADD
            = new GatewayEvent<>("MESSAGE_REACTION_ADD", GatewayMessageReactionAddPayload::new, Intent.DIRECT_MESSAGE_REACTIONS, Intent.GUILD_MESSAGE_REACTIONS);
    public static final GatewayEvent<GatewayMessageReactionRemovePayload> MESSAGE_REACTION_REMOVE
            = new GatewayEvent<>("MESSAGE_REACTION_REMOVE", GatewayMessageReactionRemovePayload::new, Intent.DIRECT_MESSAGE_REACTIONS, Intent.GUILD_MESSAGE_REACTIONS);
    public static final GatewayEvent<GatewayMessageReactionRemoveAllPayload> MESSAGE_REACTION_REMOVE_ALL
            = new GatewayEvent<>("MESSAGE_REACTION_REMOVE_ALL", GatewayMessageReactionRemoveAllPayload::new, Intent.DIRECT_MESSAGE_REACTIONS, Intent.GUILD_MESSAGE_REACTIONS);
    public static final GatewayEvent<GatewayMessageReactionRemoveEmojiPayload> MESSAGE_REACTION_REMOVE_EMOJI
            = new GatewayEvent<>("MESSAGE_REACTION_REMOVE_EMOJI", GatewayMessageReactionRemoveEmojiPayload::new, Intent.DIRECT_MESSAGE_REACTIONS, Intent.GUILD_MESSAGE_REACTIONS);
    public static final GatewayEvent<GatewayPresenceUpdatePayload> PRESENCE_UPDATE
            = new GatewayEvent<>("PRESENCE_UPDATE", GatewayPresenceUpdatePayload::new, Intent.GUILD_PRESENCES);
    public static final GatewayEvent<GatewayTypingStartPayload> TYPING_START
            = new GatewayEvent<>("TYPING_START", GatewayTypingStartPayload::new, Intent.DIRECT_MESSAGE_TYPING, Intent.GUILD_MESSAGE_TYPING);
    public static final GatewayEvent<GatewayUserUpdatePayload> USER_UPDATE
            = new GatewayEvent<>("USER_UPDATE", GatewayUserUpdatePayload::new);
    public static final GatewayEvent<GatewayVoiceStateUpdatePayload> VOICE_STATE_UPDATE
            = new GatewayEvent<>("VOICE_STATE_UPDATE", GatewayVoiceStateUpdatePayload::new, Intent.GUILD_VOICE_STATES);
    public static final GatewayEvent<GatewayVoiceServerUpdatePayload> VOICE_SERVER_UPDATE
            = new GatewayEvent<>("VOICE_SERVER_UPDATE", GatewayVoiceServerUpdatePayload::new);
    public static final GatewayEvent<GatewayWebhooksUpdatePayload> WEBHOOKS_UPDATE
            = new GatewayEvent<>("WEBHOOKS_UPDATE", GatewayWebhooksUpdatePayload::new, Intent.GUILD_WEBHOOKS);

    public static final Map<String, GatewayEvent> cache = TrieMap.ofString();

    private final String name;
    private final Span<Intent> intent;
    private final Function<GatewayPayloadWrapper, GP> payloadConstructor;

    @Override
    public Processor<EventType<?, ?, WebSocketPayload.Data>> getCommonCause() {
        return Processor.ofConstant(WebSocketEvent.DATA);
    }

    @Override
    public String getName() {
        return name;
    }

    public Span<Intent> getIntent() {
        return intent;
    }

    private GatewayEvent(String name, Function<GatewayPayloadWrapper, GP> payloadConstructor, Intent... intents) {
        this.name = name;
        this.intent = Span.immutable(intents);
        this.payloadConstructor = payloadConstructor;

        cache.put(name, this);
    }

    public static @Nullable
    GatewayEvent<? extends GatewayPayload> valueOf(String name) {
        return Polyfill.uncheckedCast(cache.get(name));
    }

    @Override
    public boolean triggeredBy(WebSocketPayload.Data data) {
        return GatewayPayloadWrapper.EventType.getFrom(data.getBody().asObjectNode()).equals(this);
    }

    @Override
    public GP createPayload(WebSocketPayload.Data data, DiscordBot bot) {
        final GatewayPayloadWrapper gpw = new GatewayPayloadWrapper(bot, data);
        return payloadConstructor.apply(gpw);
    }

    @Override
    public boolean equals(Object other) {
        //noinspection rawtypes
        return (other instanceof GatewayEvent) && ((GatewayEvent) other).name.equals(name);
    }
}
