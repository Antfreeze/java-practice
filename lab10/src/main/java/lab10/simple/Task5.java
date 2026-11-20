package lab10.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Task5 {
    static final int TASKS = 10_000;

    public static void main(String[] args) throws Exception {
        System.out.println("=== Task 5 ===");
        System.out.println("tasks = " + TASKS);
        ExecutorService platform = Executors.newFixedThreadPool(200);
        ExecutorService virtual = Executors.newVirtualThreadPerTaskExecutor();

        try {
            long platformMs = run("platform", platform, 200);
            long virtualMs = run("virtual", virtual, 0);
            System.out.println("platform = " + platformMs + " ms, virtual = " + virtualMs + " ms");
            System.out.println("observation, not a benchmark");
        } finally {
            platform.shutdown();
            virtual.shutdown();
            platform.awaitTermination(2, TimeUnit.SECONDS);
            virtual.awaitTermination(2, TimeUnit.SECONDS);
        }
    }

    static long run(String name, ExecutorService pool, int platformThreads) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        long heapBefore = runtime.totalMemory() - runtime.freeMemory();
        AtomicInteger started = new AtomicInteger();
        long start = System.nanoTime();

        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < TASKS; i++) {
            futures.add(pool.submit(() -> {
                started.incrementAndGet();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }));
        }

        for (Future<?> future : futures) {
            future.get();
        }

        long ms = (System.nanoTime() - start) / 1_000_000;
        long heapAfter = runtime.totalMemory() - runtime.freeMemory();
        int threads = platformThreads > 0 ? platformThreads : started.get();
        System.out.println(name + ": " + ms + " ms, tasks = " + TASKS
                + ", threads = " + threads
                + ", heap before = " + heapBefore / 1024 / 1024
                + " MB, after = " + heapAfter / 1024 / 1024 + " MB");
        return ms;
    }
}
