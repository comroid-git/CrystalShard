package de.comroid.crystalshard.util.model;

public abstract class Pair<A, B> {
    protected final A aValue;
    protected final B bValue;

    public Pair(A aValue, B bValue) {
        this.aValue = aValue;
        this.bValue = bValue;
    }

    public A getAValue() {
        return aValue;
    }

    public B getBValue() {
        return bValue;
    }
}
