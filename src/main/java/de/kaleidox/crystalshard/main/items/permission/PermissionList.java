package de.kaleidox.crystalshard.main.items.permission;

import de.kaleidox.crystalshard.internal.items.permission.PermissionListInternal;

import java.util.Optional;
import java.util.Set;

public interface PermissionList extends Set<Permission> {
    PermissionList EMPTY_LIST = new PermissionListInternal(null, 0);

    static PermissionList emptyListOf(PermissionOverwritable parent) {
        return new PermissionListInternal(parent, 0);
    }

    static PermissionList create(PermissionOverwritable parent) {
        return new PermissionListInternal(parent);
    }

    Optional<PermissionOverwritable> getParent();

    int toPermissionInt();

    @Override
    boolean add(Permission permission);

    @Override
    boolean remove(Object o);
}
