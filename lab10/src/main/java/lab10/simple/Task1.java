package lab10.simple;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Task1 {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Task 1 ===");
        ExecutorService pool = Executors.newFixedThreadPool(2);

        try {
            Future<?> runnable = pool
                    .submit(() -> System.out.println("Runnable выполнился в " + Thread.currentThread().getName()));
            runnable.get();

            Future<String> callable = pool.submit(() -> {
                Thread.sleep(100);
                return "Callable result";
            });
            System.out.println("Callable вернул: " + callable.get());

            Future<String> slow = pool.submit(() -> {
                Thread.sleep(5_000);
                return "slow";
            });
            try {
                slow.get(300, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                System.out.println("TimeoutException: не успели за 300 мс");
                slow.cancel(true);
            } catch (ExecutionException e) {
                System.out.println("ExecutionException: " + e.getCause());
            }

            try {
                slow.get(1, TimeUnit.SECONDS);
            } catch (CancellationException e) {
                System.out.println("После cancel: CancellationException");
            }

            Future<String> cancellable = pool.submit(() -> {
                try {
                    for (int i = 0; i < 100; i++) {
                        if (Thread.currentThread().isInterrupted()) {
                            System.out.println("Обнаружен флаг прерывания");
                            return "stopped by flag";
                        }
                        Thread.sleep(50);
                    }
                    return "done";
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("InterruptedException, флаг восстановлен");
                    return "stopped by InterruptedException";
                } finally {
                    System.out.println("Ресурс освобождён в " + Thread.currentThread().getName());
                }
            });
            Thread.sleep(200);
            cancellable.cancel(true);
            try {
                cancellable.get(1, TimeUnit.SECONDS);
            } catch (CancellationException e) {
                System.out.println("Задача отменена");
            }
            Future<String> failed = pool.submit(() -> {
                throw new IllegalStateException("boom");
            });
            try {
                failed.get(1, TimeUnit.SECONDS);
            } catch (ExecutionException e) {
                System.out.println("ExecutionException: " + e.getCause());
            }
        } finally {
            pool.shutdown();
            if (!pool.awaitTermination(2, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        }
    }
}
