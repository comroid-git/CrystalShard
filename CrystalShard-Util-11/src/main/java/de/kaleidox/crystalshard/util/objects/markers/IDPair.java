package de.kaleidox.crystalshard.util.objects.markers;

public class IDPair {
    private final long one;
    private final long two;
    
    private IDPair(long one, long two) {
        this.one = one;
        this.two = two;
    }
    
    public long getOne() {
        return one;
    }
    
    public long getTwo() {
        return two;
    }
    
    public static IDPair of(long one, long two) {
        return new IDPair(one, two);
    }
}
