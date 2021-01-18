package org.comroid.crystalshard.model.guild;

import org.comroid.api.BitmaskEnum;

import java.util.HashSet;

public final class PermissionSet extends HashSet<Permission> {
    public PermissionSet(int value) {
        super(BitmaskEnum.valueOf(value, Permission.class));
    }

    public int getBitmask() {
        return BitmaskEnum.toMask(toArray(new Permission[0]));
    }
}
