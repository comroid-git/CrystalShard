package org.comroid.crystalshard.gateway.event.dispatch.user;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.crystalshard.gateway.event.dispatch.interaction.InteractionCreateEvent;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class UserUpdateEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<UserUpdateEvent> TYPE
            = BASETYPE.subGroup("user-update", UserUpdateEvent::new);
    public static final VarBind<UserUpdateEvent, UniObjectNode, User, User> USER
            = TYPE.createBind("")
            .extractAsObject()
            .andResolve(User::resolve)
            .build();
    public final Reference<User> user = getComputedReference(USER);

    public User getUser() {
        return user.assertion();
    }

    public UserUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
