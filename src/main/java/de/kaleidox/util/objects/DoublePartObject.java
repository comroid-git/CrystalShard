package de.kaleidox.util.objects;

public class DoublePartObject<A, B> {
    private A partA;
    private B partB;
    
    public DoublePartObject(A a, B b) {
        partA = a;
        partB = b;
    }
    
    public A getA() {
        return partA;
    }
    
    public B getB() {
        return partB;
    }
}
