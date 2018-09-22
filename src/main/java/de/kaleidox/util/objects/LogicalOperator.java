package de.kaleidox.util.objects;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public enum LogicalOperator {
    UNKNOWN("unknown"),
    AND("and"),
    OR("or"),
    NOT("not"),
    XOR("xor");
    String name;
    
    LogicalOperator(String name) {
        this.name = name;
    }
    
    // Override Methods
    @Override
    public String toString() {
        return "LogicalOperator (" + name + ")";
    }
    
    public boolean test(Stream<Boolean> booleans) {
        if (this == UNKNOWN) throw new NullPointerException();
        switch (this) {
            case AND:
                return booleans.allMatch(b -> b);
            case OR:
                return booleans.anyMatch(b -> b);
            case XOR:
                return booleans.filter(b -> b).count() > 1;
            case NOT:
                return booleans.noneMatch(b -> b);
            default:
                return false;
        }
    }
    
    public <T> boolean test(Predicate<T> predicate, Collection<T> collection) {
        return test(collection.stream().map(predicate::test));
    }
    
    public String getName() {
        return name;
    }
    
// Static members
    // Static membe
    public static Optional<LogicalOperator> find(String tag) {
        return Stream.of(values()).filter(lo -> lo.name.equalsIgnoreCase(tag)).findAny();
    }
}
