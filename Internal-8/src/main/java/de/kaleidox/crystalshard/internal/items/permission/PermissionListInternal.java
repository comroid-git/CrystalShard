package de.kaleidox.crystalshard.internal.items.permission;

import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverride;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverwritable;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PermissionListInternal extends HashSet<Permission> implements PermissionList {
    private final PermissionOverwritable parent;

    public PermissionListInternal(PermissionOverwritable parent) {
        super();
        this.parent = parent;
    }

    public PermissionListInternal(int permissionInteger) {
        this(null, permissionInteger);
    }

    public PermissionListInternal(PermissionOverwritable parent, int permissionInteger) {
        super(Stream.of(Permission.values())
                .filter(permission -> permission.partOf(permissionInteger))
                .filter(permission -> permission != Permission.EMPTY)
                .collect(Collectors.toList()));
        this.parent = parent;
    }

    @Override
    public Optional<PermissionOverwritable> getParent() {
        return Optional.ofNullable(parent);
    }

    // Override Methods
    @Override
    public int toPermissionInt() {
        int value = Permission.EMPTY.getValue();

        forEach(permission -> permission.apply(value, true));

        return value;
    }

    @Override
    public PermissionOverride toOverride() {
        if (parent == null) throw new NullPointerException("No parent is defined!");
        return new PermissionOverrideInternal(parent.getDiscord(), parent.getServer(), parent, this);
    }

    @Override
    public boolean add(Permission permission) {
        boolean success = false;

        if (!contains(permission)) {
            super.add(permission);
        }

        return success;
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof Permission) {
            boolean success = false;

            if (contains(o)) {
                super.remove(o);
            }

            return success;
        } else return false;
    }
}
