package com.rainman.util;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.*;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Log4j2
public class RunnableUtils {
    static ThreadLocal<Map<String, ListenableFuture<?>>> runnableMapThreadlocal = ThreadLocal.withInitial(() -> Maps.newConcurrentMap());

    private static ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1));

    /**
     * 多线程执行，执行前加入本地线程map,执行后或异常移出本地线程map
     *
     * @param runnable 异步
     */
    public static void execute(Runnable runnable) {
        Map<String, ListenableFuture<?>> runnableMap = runnableMapThreadlocal.get();

        String key = UUID.randomUUID().toString();

        ListenableFuture<?> future = service.submit(runnable);

        runnableMap.put(key, future);

        Futures.addCallback(future, new FutureCallback() {
            @Override
            public void onSuccess(@Nullable Object o) {
                runnableMap.remove(key);
            }

            @Override
            public void onFailure(Throwable throwable) {
                runnableMap.remove(key);

                log.error(throwable);
            }
        }, service);
    }

    /**
     * 等持本地线程map中多线程全部执行完
     */
    public static void waitExecute() {
        Map<String, ListenableFuture<?>> runnableMap = runnableMapThreadlocal.get();

        if (runnableMap.isEmpty()) {
            return;
        }

        while (true) {
            runnableMap.keySet().iterator().forEachRemaining(key -> {
                ListenableFuture<?> future = runnableMap.get(key);

                if (future == null || future.isDone() || future.isCancelled()) {
                    runnableMap.remove(key);
                }
            });

            if (runnableMap.isEmpty()) {
                break;
            }

            try {
                TimeUnit.MICROSECONDS.sleep(100);
            } catch (InterruptedException e) {
                log.error(e);
            }
        }
    }
}