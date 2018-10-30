package de.kaleidox.crystalshard.main.items.permission;

import de.kaleidox.crystalshard.internal.InternalDelegate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PermissionList extends Set<Permission> {
    PermissionList EMPTY_LIST = InternalDelegate.newInstance(PermissionList.class, null, 0);

    static PermissionList emptyListOf(PermissionOverwritable parent) {
        return InternalDelegate.newInstance(PermissionList.class, parent, 0);
    }

    static PermissionList create(PermissionOverwritable parent) {
        return InternalDelegate.newInstance(PermissionList.class, parent);
    }

    static PermissionList create(List<Permission> lackingPermission) {
        PermissionList permissions = InternalDelegate.newInstance(PermissionList.class, null, 0);
        permissions.addAll(lackingPermission);
        return permissions;
    }

    Optional<PermissionOverwritable> getParent();

    int toPermissionInt();

    /**
     * When using this method, all contained Permissions are mapped as {@link OverrideState#ALLOWED}, and all not contained are mapped as {@link
     * OverrideState#DENIED}.
     *
     * @return A permission override object for this permission list.
     */
    PermissionOverride toOverride();

    @Override
    boolean add(Permission permission);

    @Override
    boolean remove(Object o);
}
