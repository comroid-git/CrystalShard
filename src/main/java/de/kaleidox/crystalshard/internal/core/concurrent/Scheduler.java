package de.kaleidox.crystalshard.internal.core.concurrent;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    private final static Logger logger = new Logger(Scheduler.class);
    private final TreeMap<Long, List<Object[]>> runnableMap = new TreeMap<>();
    private final ThreadPool schedulerPool;
    private final LinkedBlockingQueue<Runnable> queue;
    private final long heartbeat;
    private final Discord discord;

    public Scheduler(Discord discord,
                     long heartbeat) {
        this.discord = discord;
        this.queue = new LinkedBlockingQueue<>();
        this.schedulerPool = new ThreadPool(discord, 51);
        this.heartbeat = heartbeat;
        schedulerPool.execute(this::cycle);
        scheduleWithDelayAndRate(heartbeat, heartbeat, TimeUnit.MILLISECONDS, () ->
                ((DiscordInternal) discord).getWebSocket().heartbeat());
        scheduleWithDelayAndRate(250, 250, TimeUnit.MILLISECONDS, () ->
                ((DiscordInternal) discord).getRatelimiter().tryRun());
    }

    public void schedule(Runnable task) {
        schedulerPool.execute(task);
    }

    public void scheduleWithDelay(long delay, TimeUnit unit, Runnable task) {
        scheduleWithDelayAndRate(delay, -1, unit, task);
    }

    public void scheduleAtRate(long rate, TimeUnit unit, Runnable task) {
        scheduleWithDelayAndRate(0, rate, unit, task);
    }

    public void scheduleWithDelayAndRate(long delay, long rate, TimeUnit unit, Runnable task) {
        long milis = System.currentTimeMillis();
        long newKey = milis + unit.toMillis(rate);
        runnableMap.putIfAbsent(newKey, new ArrayList<>());
        runnableMap.get(newKey).add(new Object[]{task, rate, unit});
    }

    private void cycle() {
        var ref = new Object() {
            long milis = 0;
        };
        while (true) {
            ref.milis = System.currentTimeMillis();
            try {
                runnableMap.entrySet()
                        .stream()
                        .filter(entry -> entry.getKey() < -ref.milis)
                        .peek(entry -> {
                            List<Object[]> value = entry.getValue();
                            value.forEach(objects -> {
                                Runnable task = (Runnable) objects[0];
                                Long rate = (Long) objects[1];
                                TimeUnit unit = (TimeUnit) objects[2];

                                schedulerPool.execute(task);
                                if (rate != -1) {
                                    long newKey = ref.milis + unit.toMillis(rate);
                                    runnableMap.putIfAbsent(newKey, new ArrayList<>());
                                    runnableMap.get(newKey).add(new Object[]{task, rate, unit});
                                }
                            });
                            value.clear();
                        }).forEachOrdered(entry -> runnableMap.remove(entry.getKey()));
                Thread.sleep(200);
            } catch (InterruptedException e) {
                logger.exception(e, "Fatal error in scheduler thread! Please contact the developer!");
            }
        }
    }
}
