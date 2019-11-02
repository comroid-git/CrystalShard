package de.comroid.crystalshard.core.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.comroid.crystalshard.core.api.concurrent.ThreadPool;

import org.jetbrains.annotations.NotNull;

public class ThreadPoolImpl implements ThreadPool {
    private final String name;
    
    private final ExecutorService executorService;

    public ThreadPoolImpl(String name) {
        this.name = name;
        
        new Factory();
        
        executorService = Executors.newCachedThreadPool();
    }
    
    @Override 
    public Executor getExecutor() {
        return this;
    }
    
    @Override 
    public ScheduledExecutorService getScheduler() {
        
    }

    @Override public void shutdown() {

    }

    @NotNull @Override public List<Runnable> shutdownNow() {
        return null;
    }

    @Override public boolean isShutdown() {
        return false;
    }

    @Override public boolean isTerminated() {
        return false;
    }

    @Override public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return false;
    }

    @NotNull @Override public <T> Future<T> submit(@NotNull Callable<T> task) {
        return null;
    }

    @NotNull @Override public <T> Future<T> submit(@NotNull Runnable task, T result) {
        return null;
    }

    @NotNull @Override public Future<?> submit(@NotNull Runnable task) {
        return null;
    }

    @NotNull @Override
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return null;
    }

    @NotNull @Override
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return null;
    }

    @NotNull @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    @Override public void execute(@NotNull Runnable command) {

    }

    @Override public Thread newThread(@NotNull Runnable r) {
        return null;
    }

    private class Factory implements ThreadFactory {
        @Override
        public Thread newThread(@NotNull Runnable r) {
        }
    }
}
