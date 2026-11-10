package tasks;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Task3 {

    static class SessionRegistry {
        private final ConcurrentHashMap<String, String> sessions = new ConcurrentHashMap<>();

        boolean createIfAbsent(String id) {
            String value = "session-" + id;
            return sessions.putIfAbsent(id, value) == null;
        }

        int size() {
            return sessions.size();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SessionRegistry registry = new SessionRegistry();
        int threadCount = 50;
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger created = new AtomicInteger();

        try {
            for (int i = 0; i < threadCount; i++) {
                pool.execute(() -> {
                    try {
                        start.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    if (registry.createIfAbsent("session-42")) {
                        created.incrementAndGet();
                    }
                });
            }
            start.countDown();
        } finally {
            pool.shutdown();
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        }

        System.out.println("Потоков пытались создать одну сессию: " + threadCount);
        System.out.println("Успешно создали: " + created.get() + " (ожидалось 1)");
        System.out.println("Сессий в реестре: " + registry.size());

        System.out.println("Повторное создание session-42: "
                + registry.createIfAbsent("session-42"));
        System.out.println("Создание новой session-43: "
                + registry.createIfAbsent("session-43"));
        System.out.println("Сессий в реестре теперь: " + registry.size());
    }
}
