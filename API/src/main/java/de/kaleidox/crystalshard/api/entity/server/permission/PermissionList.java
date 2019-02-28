package de.kaleidox.crystalshard.api.entity.server.permission;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PermissionList extends Set<Permission> {
    PermissionList EMPTY_LIST = Injector.create(PermissionList.class, null, 0);

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

    static PermissionList emptyListOf(PermissionOverwritable parent) {
        return Injector.create(PermissionList.class, parent, 0);
    }

    static PermissionList create(PermissionOverwritable parent) {
        return Injector.create(PermissionList.class, parent);
    }

    static PermissionList create(List<Permission> lackingPermission) {
        PermissionList permissions = Injector.create(PermissionList.class, null, 0);
        permissions.addAll(lackingPermission);
        return permissions;
    }
}
