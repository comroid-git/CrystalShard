package org.comroid.crystalshard.gateway.event.dispatch.user;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Rewrapper;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.crystalshard.gateway.event.dispatch.interaction.InteractionCreateEvent;
import org.comroid.crystalshard.model.presence.Activity;
import org.comroid.crystalshard.model.presence.ClientStatus;
import org.comroid.crystalshard.model.presence.UserStatus;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class PresenceUpdateEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<PresenceUpdateEvent> TYPE
            = BASETYPE.subGroup("presence-update", PresenceUpdateEvent::new);
    public static final VarBind<PresenceUpdateEvent, UniObjectNode, User, User> USER
            = TYPE.createBind("user")
            .extractAsObject()
            .andResolve(User::resolve)
            .build();
    public static final VarBind<PresenceUpdateEvent, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.requireFromContext(SnowflakeCache.class).getGuild(id))
            .build();
    public static final VarBind<PresenceUpdateEvent, String, UserStatus, UserStatus> STATUS
            = TYPE.createBind("status")
            .extractAs(StandardValueType.STRING)
            .andRemapRef(UserStatus::byIdent)
            .build();
    public static final VarBind<PresenceUpdateEvent, UniObjectNode, Activity, Span<Activity>> ACTIVITIES
            = TYPE.createBind("activities")
            .extractAsArray()
            .andResolve(Activity::new)
            .intoSpan()
            .build();
    public static final VarBind<PresenceUpdateEvent, UniObjectNode, ClientStatus, ClientStatus> CLIENT_STATUS
            = TYPE.createBind("client_status")
            .extractAsObject()
            .andConstruct(ClientStatus.TYPE)
            .build();
    public final Reference<User> user = getComputedReference(USER);
    public final Reference<Guild> guild = getComputedReference(GUILD);
    public final Reference<UserStatus> status = getComputedReference(STATUS);
    public final Reference<ClientStatus> clientStatus = getComputedReference(CLIENT_STATUS);

    public User getUser() {
        return user.assertion();
    }

    public Guild getGuild() {
        return guild.assertion();
    }

    public UserStatus getStatus() {
        return status.assertion();
    }

    public Span<Activity> getActivities() {
        return getComputedReference(ACTIVITIES).orElseGet(Span::empty);
    }

    public PresenceUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        // todo
    }
}
