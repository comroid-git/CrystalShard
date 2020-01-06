package org.comroid.crystalshard.util.markers;

import java.util.Objects;

public class Value {
    protected final Setter setter;
    protected Object value;

    public Value(Object value) {
        this.value = (value instanceof Value ? ((Value) value).getValue() : value);

        setter = new Setter();
    }

    public Object getValue() {
        return value;
    }

    public boolean isNull() {
        return Objects.isNull(value);
    }

    public String asString() {
        if (value instanceof String) return (String) value;
        return String.valueOf(value);
    }

    public boolean asBoolean() {
        if (value instanceof Boolean) return (boolean) value;
        return Boolean.valueOf(asString());
    }

    public byte asByte() {
        if (value instanceof Byte) return (byte) value;
        return Byte.valueOf(asString());
    }

    public short asShort() {
        if (value instanceof Short) return (short) value;
        return Short.valueOf(asString());
    }

    public int asInt() {
        if (value instanceof Integer) return (int) value;
        return Integer.valueOf(asString());
    }

    public float asFloat() {
        if (value instanceof Float) return (float) value;
        return Float.valueOf(asString());
    }

    public double asDouble() {
        if (value instanceof Double) return (double) value;
        return Double.valueOf(asString());
    }

    public long asLong() {
        if (value instanceof Long) return (long) value;
        return Long.valueOf(asString());
    }

    public char asChar() {
        if (value instanceof Character) return (char) value;
        return asString().charAt(0);
    }

    public Setter setter() {
        return setter;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Value) return ((Value) obj).asString().equals(asString());
        else return String.valueOf(obj).equals(asString());
    }

    @SuppressWarnings("unchecked")
    public <R> R as(Class<? extends R> targetType) {
        if (Boolean.class.isAssignableFrom(targetType))
            return (R) (Boolean) asBoolean();
        else if (Byte.class.isAssignableFrom(targetType))
            return (R) (Byte) asByte();
        else if (Short.class.isAssignableFrom(targetType))
            return (R) (Short) asShort();
        else if (Integer.class.isAssignableFrom(targetType))
            return (R) (Integer) asInt();
        else if (Float.class.isAssignableFrom(targetType))
            return (R) (Float) asFloat();
        else if (Double.class.isAssignableFrom(targetType))
            return (R) (Double) asDouble();
        else if (Long.class.isAssignableFrom(targetType))
            return (R) (Long) asLong();
        else if (Character.class.isAssignableFrom(targetType))
            return (R) (Character) asChar();
        else if (String.class.isAssignableFrom(targetType))
            return (R) asString();
        else {
            if (targetType.isInstance(value))
                return (R) value;

            try {
                return (R) targetType.getMethod("valueOf", String.class)
                        .invoke(null, asString());
            } catch (Throwable t) {
                throw new ClassCastException("Cannot deserialize to type \"" + targetType.getSimpleName() + "\"; " +
                        "static method \"#valueOf(String)\" is missing.");
            }
        }
    }

    public class Setter {
        private Setter() {
        }

        public void toObject(Object o) {
            value = o;
        }

        public void toString(String s) {
            value = s;
        }

        public void toBoolean(boolean b) {
            value = b;
        }

        public void toByte(byte b) {
            value = b;
        }

        public void toShort(short s) {
            value = s;
        }

        public void toInt(int i) {
            value = i;
        }

        public void toFloat(float f) {
            value = f;
        }

        public void toDouble(double d) {
            value = d;
        }

        public void toLong(long l) {
            value = l;
        }

        public void toChar(char c) {
            value = c;
        }
    }
}
