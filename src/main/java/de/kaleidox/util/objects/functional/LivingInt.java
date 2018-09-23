package de.kaleidox.util.objects.functional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LivingInt {
    // Static Fields
    public final static long           MILLIS_MINIMUM = 20;
    private final       int            stopAt;
    private final       int            stepSize;
    private final       List<Runnable> onStopHit;
    private             int            value;
    
    public LivingInt(int initialValue, int stopAt, int stepSize, long stepTime, TimeUnit stepUnit) {
        this.value = initialValue;
        this.stopAt = stopAt;
        this.stepSize = stepSize;
        this.onStopHit = new ArrayList<>();
        if (TimeUnit.MILLISECONDS.convert(stepTime, stepUnit) < MILLIS_MINIMUM) throw new IllegalArgumentException(
                "stepTime must be greater than 50 milliseconds!");
        
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::step, stepTime, stepTime, stepUnit);
    }
    
    public synchronized int change(int delta) {
        return value += delta;
    }
    
    public synchronized int set(int set) {
        value = set;
        return value;
    }
    
    public synchronized int get() {
        return value;
    }
    
    public synchronized void onStopHit(Runnable runnable) {
        onStopHit.add(runnable);
    }
    
    private synchronized void step() {
        if (value == stopAt) {
            onStopHit.forEach(Runnable::run);
            return;
        }
        value = value + stepSize;
    }
}
