package de.kaleidox.util.objects;

import de.kaleidox.util.objects.functional.Evaluation;

import java.util.ArrayList;
import java.util.List;

public class Difference<T> {
    private final List<T> added;
    private final List<T> removed;
    
    private Difference(List<T> added, List<T> removed) {
        this.added = added;
        this.removed = removed;
    }
    
    public List<T> getAdded() {
        return added;
    }
    
    public Evaluation<Boolean> hasAdded() {
        return Evaluation.of(!added.isEmpty());
    }
    
    public List<T> getRemoved() {
        return removed;
    }
    
    public Evaluation<Boolean> hasRemoved() {
        return Evaluation.of(!removed.isEmpty());
    }
    
// Static members
    // Static membe
    public static <T> Difference<T> of(List<T> added, List<T> removed) {
        return new Difference<>(added, removed);
    }
    
    public static class Builder<A> {
        private final List<A> added;
        private final List<A> removed;
        
        public Builder() {
            added = new ArrayList<>();
            removed = new ArrayList<>();
        }
        
        public void addAdded(A item) {
            added.add(item);
        }
        
        public void addRemoved(A item) {
            removed.add(item);
        }
        
        public Difference<A> build() {
            return new Difference<>(added, removed);
        }
    }
}
