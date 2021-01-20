package org.comroid.crystalshard.gateway.event.dispatch.guild.invite;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Polyfill;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.crystalshard.model.invite.TargetUserType;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;

public final class InviteCreateEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<InviteCreateEvent> TYPE
            = BASETYPE.subGroup("invite-create", InviteCreateEvent::new);
    public static final VarBind<InviteCreateEvent, Long, Channel, Channel> CHANNEL
            = TYPE.createBind("channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.requireFromContext(SnowflakeCache.class).getChannel(id))
            .build();
    public static final VarBind<InviteCreateEvent, String, String, String> CODE
            = TYPE.createBind("code")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<InviteCreateEvent, String, Instant, Instant> CREATED_AT
            = TYPE.createBind("created_at")
            .extractAs(StandardValueType.STRING)
            .andRemap(Instant::parse)
            .build();
    public static final VarBind<InviteCreateEvent, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.requireFromContext(SnowflakeCache.class).getGuild(id))
            .build();
    public static final VarBind<InviteCreateEvent, UniObjectNode, User, User> INVITER
            = TYPE.createBind("inviter")
            .extractAsObject()
            .andResolve(User::resolve)
            .build();
    public static final VarBind<InviteCreateEvent, Integer, Duration, Duration> MAX_AGE
            = TYPE.createBind("max_age")
            .extractAs(StandardValueType.INTEGER)
            .andRemap(Duration::ofSeconds)
            .build();
    public static final VarBind<InviteCreateEvent, Integer, Integer, Integer> MAX_USES
            = TYPE.createBind("max_uses")
            .extractAs(StandardValueType.INTEGER)
            .build();
    public static final VarBind<InviteCreateEvent, UniObjectNode, User, User> TARGET_USER
            = TYPE.createBind("target_user")
            .extractAsObject()
            .andResolve(User::resolve)
            .build();
    public static final VarBind<InviteCreateEvent, Integer, TargetUserType, TargetUserType> TARGET_USER_TYPE
            = TYPE.createBind("target_user_type")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(TargetUserType::valueOf)
            .build();
    public static final VarBind<InviteCreateEvent, Boolean, Boolean, Boolean> IS_TEMPORARY
            = TYPE.createBind("is_temporary")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<InviteCreateEvent, Integer, Integer, Integer> USES
            = TYPE.createBind("uses")
            .extractAs(StandardValueType.INTEGER)
            .build();
    public final Reference<Channel> channel = getComputedReference(CHANNEL);
    public final Reference<String> code = getComputedReference(CODE);
    public final Reference<Instant> createdAt = getComputedReference(CREATED_AT);
    public final Reference<Guild> guild = getComputedReference(GUILD);
    public final Reference<User> inviter = getComputedReference(INVITER);
    public final Reference<Duration> maxAge = getComputedReference(MAX_AGE);
    public final Reference<Integer> maxUses = getComputedReference(MAX_USES);
    public final Reference<User> targetUser = getComputedReference(TARGET_USER);
    public final Reference<TargetUserType> targetUserType = getComputedReference(TARGET_USER_TYPE);
    public final Reference<Boolean> isTemporary = getComputedReference(IS_TEMPORARY);
    public final Reference<Integer> uses = getComputedReference(USES);

    public Channel getChannel() {
        return channel.assertion();
    }

    public URL getInviteURL() {
        return code.map(code -> "https://discord.gg/" + code)
                .map(Polyfill::url)
                .assertion("Code missing");
    }

    public Instant getCreationTime() {
        return createdAt.assertion();
    }

    public Guild getGuild() {
        return guild.assertion();
    }

    public User getInviter() {
        return inviter.assertion();
    }

    public Duration getMaxAge() {
        return maxAge.assertion();
    }

    public Integer getMaxUses() {
        return maxUses.assertion();
    }

    public User getTargetUser() {
        return targetUser.assertion();
    }

    public TargetUserType getTargetUserType() {
        return targetUserType.assertion();
    }

    public boolean isTemporary() {
        return isTemporary.assertion();
    }

    public int getUseCount() {
        return uses.assertion();
    }

    public InviteCreateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        // todo Add Invite functionality
    }
}
