package org.comroid.crystalshard.adapter;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.Arrays;

class ClassSignature<T> {
    final Class<T> klass;
    final Class[] param;

    final boolean methodBased;

    ClassSignature(Class<T> klass, Class[] param) {
        this.klass = klass;
        this.param = param;

        this.methodBased = false;
    }

    ClassSignature(Class<T> klass, Executable executable) {
        this.klass = klass;
        this.param = executable.getParameterTypes();

        this.methodBased = executable instanceof Method;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ClassSignature)
            return klass.isAssignableFrom(((ClassSignature) obj).klass)
                    && allAssignable(param, ((ClassSignature) obj).param);

        return false;
    }

    @Override
    public String toString() {
        return String.format("ClassSignature %s(%s)", klass.getName(), Arrays.toString(param));
    }

    private static boolean allAssignable(Class[] signBase, Class[] signTarget) {
        if (signBase.length != signTarget.length) return false;

        for (int i = 0; i < signBase.length; i++) {
            Class bc = signBase[i];
            Class tc = signTarget[i];

            //noinspection unchecked
            if (!bc.isAssignableFrom(tc))
                return false;
        }

        return true;
    }
}
