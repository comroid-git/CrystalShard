package de.kaleidox.crystalshard.util.objects.markers;

public class DoublePartObject<A, B> {
    private A partA;
    private B partB;

    public DoublePartObject(A a, B b) {
        partA = a;
        partB = b;
    }

    // Static membe
    public static <A, B> DoublePartObject<A, B> of(A a, B b) {
        return new DoublePartObject<>(a, b);
    }

    public A getA() {
        return partA;
    }

    public B getB() {
        return partB;
    }
}
