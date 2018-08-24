package de.kaleidox.crystalshard.internal.items.permission;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.crystalshard.main.items.permission.*;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.util.JsonHelper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PermissionOverrideInternal
        extends ConcurrentHashMap<Permission, OverrideState>
        implements PermissionOverride {
    private final PermissionApplyable parent;
    private final PermissionList scopedPermission;

    public PermissionOverrideInternal(PermissionApplyable parent, User scope) {
        this.parent = parent;
        this.scopedPermission = parent.getListFor(scope);
    }

    @Override
    public PermissionApplyable getParent() {
        return parent;
    }

    @Override
    public PermissionOverride addOverride(Permission permission, OverrideState state) {
        removeOverride(permission);
        put(permission, state);
        return this;
    }

    @Override
    public PermissionOverride removeOverride(Permission permission) {
        remove(permission);
        return this;
    }

    public JsonNode toJsonNode() {
        ObjectNode node = JsonHelper.objectNode();
        node.set("id", JsonHelper.nodeOf(Long.toUnsignedString(parent.getId())));
        node.set("type", JsonHelper.nodeOf(parent instanceof Role ? "role" : "member"));
        node.set("allow", JsonHelper.nodeOf(entrySet().stream()
                .filter(entry -> entry.getValue() == OverrideState.ALLOWED)
                .map(Entry::getKey)
                .collect(Collectors.toCollection(() -> PermissionList.create(parent)))
                .toPermissionInt()));
        node.set("deny", JsonHelper.nodeOf(entrySet().stream()
                .filter(entry -> entry.getValue() == OverrideState.DENIED)
                .map(Entry::getKey)
                .collect(Collectors.toCollection(() -> PermissionList.create(parent)))
                .toPermissionInt()));

        return node;
    }
}
