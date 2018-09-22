package de.kaleidox.util.objects.functional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Switch<T> {
    private final BiFunction<T, T, Boolean> checker;
    private final List<Case>                cases;
    private       Consumer<T>               defaultExecutor;
    
    public Switch(BiFunction<T, T, Boolean> checker) {
        this.checker = checker;
        this.cases = new ArrayList<>();
    }
    
    public Switch<T> addCase(T item, Consumer<T> executor) {
        cases.add(new Case(item, executor));
        return this;
    }
    
    public Switch<T> defaultCase(Consumer<T> defaultExecutor) {
        this.defaultExecutor = defaultExecutor;
        return this;
    }
    
    public void test(T item) {
        boolean needDefault = true;
        for (Case kase : cases) {
            if (checker.apply(item, kase.item)) {
                needDefault = false;
                kase.executor.accept(item);
            }
        }
        if (needDefault && defaultExecutor != null) defaultExecutor.accept(item);
    }
    
    private class Case {
        private final T           item;
        private final Consumer<T> executor;
        
        private Case(T item, Consumer<T> executor) {
            this.item = item;
            this.executor = executor;
        }
    }
}
