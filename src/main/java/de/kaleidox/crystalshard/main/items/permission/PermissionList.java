package de.kaleidox.crystalshard.main.items.permission;

import de.kaleidox.crystalshard.internal.items.permission.PermissionListInternal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PermissionList extends Set<Permission> {
    // Static Fields
    PermissionList EMPTY_LIST = new PermissionListInternal(null, 0);
    
    Optional<PermissionOverwritable> getParent();
    
    int toPermissionInt();
    
    // Override Methods
    @Override
    boolean add(Permission permission);
    
    @Override
    boolean remove(Object o);
    
    // Static members
    // Static membe
    static PermissionList emptyListOf(PermissionOverwritable parent) {
        return new PermissionListInternal(parent, 0);
    }
    
    static PermissionList create(PermissionOverwritable parent) {
        return new PermissionListInternal(parent);
    }
    
    static PermissionList create(List<Permission> lackingPermission) {
        PermissionListInternal permissions = new PermissionListInternal(null, 0);
        permissions.addAll(lackingPermission);
        return permissions;
    }
}
