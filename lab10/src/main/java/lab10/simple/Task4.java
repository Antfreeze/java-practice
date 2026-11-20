package lab10.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Task4 {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Task 4 ===");
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("cores = " + cores);

        ExecutorService cpuPool = Executors.newFixedThreadPool(cores);

        ExecutorService ioPool = Executors.newCachedThreadPool();

        ExecutorService virtualPool = Executors.newVirtualThreadPerTaskExecutor();
        ExecutorService smallPool = Executors.newFixedThreadPool(2);

        try {
            List<Future<Long>> cpuFutures = new ArrayList<>();
            for (int i = 0; i < cores; i++) {
                cpuFutures.add(cpuPool.submit(Task4::cpuWork));
            }
            for (Future<Long> future : cpuFutures) {
                future.get();
            }
            System.out.println("CPU pool finished");

            long start = System.nanoTime();
            List<Future<String>> ioFutures = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                ioFutures.add(ioPool.submit(() -> ioWork("io", 300)));
            }
            for (Future<String> future : ioFutures) {
                future.get();
            }
            System.out.println("ioPool ms: " + (System.nanoTime() - start) / 1_000_000);

            start = System.nanoTime();
            List<Future<String>> virtualFutures = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                virtualFutures.add(virtualPool.submit(() -> ioWork("virtual", 300)));
            }
            for (Future<String> future : virtualFutures) {
                future.get();
            }
            System.out.println("virtualPool ms: " + (System.nanoTime() - start) / 1_000_000);

            List<Future<String>> blockers = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                blockers.add(smallPool.submit(() -> ioWork("block", 500)));
            }
            Thread.sleep(100);

            Future<Long> cpu = smallPool.submit(Task4::cpuWork);
            try {
                cpu.get(200, TimeUnit.MILLISECONDS);
                System.out.println("CPU task unexpectedly finished");
            } catch (TimeoutException e) {
                System.out.println("STARVATION: CPU task waits while both threads sleep");
            }

            for (Future<String> future : blockers) {
                future.get();
            }
            System.out.println("CPU after blockers: " + cpu.get());
        } finally {
            smallPool.shutdown();
            cpuPool.shutdown();
            ioPool.shutdown();
            virtualPool.shutdown();
            smallPool.awaitTermination(2, TimeUnit.SECONDS);
            cpuPool.awaitTermination(2, TimeUnit.SECONDS);
            ioPool.awaitTermination(2, TimeUnit.SECONDS);
            virtualPool.awaitTermination(2, TimeUnit.SECONDS);
        }
    }

    static long cpuWork() {
        System.out.println("CPU work on " + Thread.currentThread().getName());
        long sum = 0;
        for (long i = 1; i <= 50_000_000; i++) {
            sum += i * i;
        }
        return sum;
    }

    static String ioWork(String name, long millis) {
        System.out.println(name + " start " + Thread.currentThread()
                + " virtual=" + Thread.currentThread().isVirtual());
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(name + " end " + Thread.currentThread());
        return name;
    }
}
