package org.comroid.crystalshard.model.permission;

import org.comroid.common.util.Bitmask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public interface PermissionSet extends Bitmask.Enum, Set<Permission> {
    @Override
    int getValue();

    static PermissionSet ofMask(int permission) {
        return Support.cache.computeIfAbsent(permission, Support.PermissionBitmask::new);
    }

    final class Support {
        private final static Map<Integer, PermissionSet> cache = new ConcurrentHashMap<>();

        private static final class PermissionBitmask extends AbstractSet<Permission> implements PermissionSet {
            private final Set<Permission> bits;
            private final int mask;

            @Override
            public int getValue() {
                return mask;
            }

            private PermissionBitmask(int mask) {
                this.mask = mask;
                this.bits = Arrays.stream(Permission.values())
                        .filter(perm -> Bitmask.isFlagSet(mask, perm.getValue()))
                        .collect(Collectors.toUnmodifiableSet());
            }

            @Override
            public boolean contains(Object other) {
                if (other instanceof Integer)
                    return Bitmask.isFlagSet(mask, (int) other);

                return super.contains(other);
            }

            @Override
            public Iterator<Permission> iterator() {
                return bits.iterator();
            }

            @Override
            public int size() {
                return bits.size();
            }
        }
    }
}
