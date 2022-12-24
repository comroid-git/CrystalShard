package org.comroid.crystalshard.model.presence;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.model.DiscordDataContainer;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class ClientStatus extends DataContainerBase<DiscordDataContainer> implements DiscordDataContainer {
    @RootBind
    public static final GroupBind<ClientStatus> TYPE
            = BASETYPE.subGroup("client-status", ClientStatus::new);
    public static final VarBind<ClientStatus, String, UserStatus, UserStatus> DESKTOP
            = TYPE.createBind("desktop")
            .extractAs(StandardValueType.STRING)
            .andRemapRef(UserStatus::byIdent)
            .build();
    public static final VarBind<ClientStatus, String, UserStatus, UserStatus> MOBILE
            = TYPE.createBind("mobile")
            .extractAs(StandardValueType.STRING)
            .andRemapRef(UserStatus::byIdent)
            .build();
    public static final VarBind<ClientStatus, String, UserStatus, UserStatus> WEB
            = TYPE.createBind("web")
            .extractAs(StandardValueType.STRING)
            .andRemapRef(UserStatus::byIdent)
            .build();
    public final Reference<UserStatus> desktopStatus = getComputedReference(DESKTOP);
    public final Reference<UserStatus> mobileStatus = getComputedReference(MOBILE);
    public final Reference<UserStatus> webStatus = getComputedReference(WEB);

    public UserStatus getDesktopStatus() {
        return desktopStatus.orElse(UserStatus.OFFLINE);
    }

    public UserStatus getMobileStatus() {
        return mobileStatus.orElse(UserStatus.OFFLINE);
    }

    public UserStatus getWebStatus() {
        return webStatus.orElse(UserStatus.OFFLINE);
    }

    public ClientStatus(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
