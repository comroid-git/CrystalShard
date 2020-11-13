package org.comroid.crystalshard.core.gateway.payload.guild.invite;

import org.comroid.common.info.MessageSupplier;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.DiscordEntity;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.model.guild.Invite;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;

public final class GatewayGuildInviteCreatePayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<AbstractGatewayPayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-guild-invite-create");
    public static final VarBind<Object, Long, Channel, Channel> channel
            = Root.createBind("channel_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(DiscordEntity.Type.CHANNEL, id)
                    .requireNonNull(MessageSupplier.format("Channel with ID %d not found", id)))
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, String, URL, URL> invite
            = Root.createBind("code")
            .extractAs(ValueType.STRING)
            .andRemap(Invite::createUrl)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, String, Instant, Instant> createdAt
            = Root.createBind("created_at")
            .extractAs(ValueType.STRING)
            .andRemap(Instant::parse)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, Long, Guild, Guild> guild
            = Root.createBind("guild_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(DiscordEntity.Type.GUILD, id)
                    .requireNonNull(MessageSupplier.format("Guild with ID %d not found", id)))
            .onceEach()
            .build();
    public static final VarBind<Object, UniObjectNode, User, User> inviter
            = Root.createBind("inviter")
            .extractAsObject()
            .andResolve(User::find)
            .onceEach()
            .onceEach()
            .build();
    public static final VarBind<Object, Integer, Duration, Duration> maxAge
            = Root.createBind("max_age")
            .extractAs(ValueType.INTEGER)
            .andRemap(Duration::ofSeconds)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, Integer, Integer, Integer> maxUses
            = Root.createBind("max_uses")
            .extractAs(ValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, UniObjectNode, User, User> targetUser
            = Root.createBind("target_user")
            .extractAsObject()
            .andResolve(User::find)
            .onceEach()
            .build();
    public static final VarBind<Object, Integer, Invite.TargetUserType, Invite.TargetUserType> targetUserType
            = Root.createBind("target_user_type")
            .extractAs(ValueType.INTEGER)
            .andRemap(Invite.TargetUserType::valueOf)
            .onceEach()
            .build();
    public static final VarBind<Object, Boolean, Boolean, Boolean> temporary
            = Root.createBind("temporary")
            .extractAs(ValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, Integer, Integer, Integer> uses
            = Root.createBind("uses")
            .extractAs(ValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();

    public Channel getChannel() {
        return requireNonNull(channel);
    }

    public URL getInviteURL() {
        return requireNonNull(invite);
    }

    public Instant getCreationTimestamp() {
        return requireNonNull(createdAt);
    }

    public @Nullable
    Guild getGuild() {
        return get(guild);
    }

    public @Nullable
    User getInviter() {
        return get(inviter);
    }

    public Duration getMaximumAge() {
        return requireNonNull(maxAge);
    }

    public int getMaximumUses() {
        return requireNonNull(maxUses);
    }

    public @Nullable
    User getTargetUser() {
        return get(targetUser);
    }

    public @Nullable
    Invite.TargetUserType getTargetUserType() {
        return get(targetUserType);
    }

    public boolean isTemporary() {
        return requireNonNull(temporary);
    }

    public int getUseCount() {
        return requireNonNull(uses);
    }

    public GatewayGuildInviteCreatePayload(GatewayPayloadWrapper gpw) {
        super(gpw);

        getChannel().addInvite(new Invite(
                process(guild),
                process(channel),
                getInviteURL(),
                getCreationTimestamp(),
                getMaximumAge(),
                getInviter(),
                getTargetUser(),
                getTargetUserType(),
                isTemporary(),
                getUseCount()
        ));
    }
}
