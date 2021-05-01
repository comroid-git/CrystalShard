package org.comroid.crystalshard.model.guild;

import org.comroid.api.BitmaskAttribute;

import java.util.HashSet;

public final class PermissionSet extends HashSet<Permission> {
    public PermissionSet(int value) {
        super(BitmaskAttribute.valueOf(value, Permission.class));
    }

    public int getBitmask() {
        return BitmaskAttribute.toMask(toArray(new Permission[0]));
    }
}
