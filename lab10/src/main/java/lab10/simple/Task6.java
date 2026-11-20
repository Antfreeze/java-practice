package lab10.simple;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Task6 {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Task 6 ===");
        ExecutorService pool = Executors.newFixedThreadPool(3);

        long deadlineNanos = System.nanoTime() + Duration.ofSeconds(2).toNanos();
        CountDownLatch done1 = new CountDownLatch(1);
        CountDownLatch done2 = new CountDownLatch(1);
        CountDownLatch done3 = new CountDownLatch(1);

        List<Future<String>> futures = List.of(
                pool.submit(() -> work("fast", 100, done1)),
                pool.submit(() -> work("medium", 800, done2)),
                pool.submit(() -> work("slow", 5_000, done3)));
        List<CountDownLatch> latches = List.of(done1, done2, done3);
        try {
            for (int i = 0; i < futures.size(); i++) {
                long remaining = deadlineNanos - System.nanoTime();
                if (remaining <= 0) {
                    cancelAndWait(futures.get(i), latches.get(i), i);
                    continue;
                }

                try {
                    System.out.println("remaining ms for task " + i + ": "
                            + remaining / 1_000_000);
                    System.out.println("result: "
                            + futures.get(i).get(remaining, TimeUnit.NANOSECONDS));
                } catch (TimeoutException e) {
                    System.out.println("timeout: task " + i);
                    cancelAndWait(futures.get(i), latches.get(i), i);
                } catch (CancellationException | ExecutionException e) {
                    System.out.println("task " + i + " did not finish: " + e);
                }
            }
        } finally {
            for (CountDownLatch latch : latches) {
                latch.await(1, TimeUnit.SECONDS);
            }
            pool.shutdown();
            pool.awaitTermination(2, TimeUnit.SECONDS);
        }
    }

    static void cancelAndWait(Future<String> future, CountDownLatch latch, int index)
            throws InterruptedException {
        System.out.println("cancel task " + index + " = " + future.cancel(true));
        latch.await(1, TimeUnit.SECONDS);
        try {
            future.get();
        } catch (CancellationException e) {
            System.out.println("task " + index + " cancelled and cleaned up");
        } catch (ExecutionException e) {
            System.out.println("task " + index + " failed: " + e.getCause());
        }
    }

    static String work(String name, long millis, CountDownLatch done) {
        try {
            System.out.println("start " + name + " on " + Thread.currentThread().getName());
            Thread.sleep(millis);
            System.out.println("done " + name);
            return name + " done";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(name + " interrupted, flag restored");
            return name + " cancelled";
        } finally {
            done.countDown();
        }
    }
}
