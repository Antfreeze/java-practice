package tasks;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Task4 {

    static class Request {
        final int id;

        Request(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Request#" + id;
        }
    }

    static final Request POISON = new Request(-1);

    static class NamedThreadFactory implements ThreadFactory {
        private final String prefix;
        private final AtomicInteger number = new AtomicInteger(1);

        NamedThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable task) {
            return new Thread(task, prefix + "-" + number.getAndIncrement());
        }
    }

    static final int QUEUE_CAPACITY = 5;

    public static void main(String[] args) throws InterruptedException {
        plainThreadsDemo();
        executorDemo();
    }

    private static void plainThreadsDemo() throws InterruptedException {
        System.out.println("=== 1. Обычные потоки + ArrayBlockingQueue ===");
        BlockingQueue<Request> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        int producerCount = 2;
        int consumerCount = 2;
        int perProducer = 10;
        AtomicInteger processed = new AtomicInteger();

        Thread[] threads = new Thread[producerCount + consumerCount];
        for (int i = 0; i < producerCount; i++) {
            int producerId = i + 1;
            threads[i] = new Thread(() -> {
                try {
                    produce(queue, producerId, perProducer);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "producer-" + producerId);
        }
        for (int i = 0; i < consumerCount; i++) {
            threads[producerCount + i] = new Thread(() -> {
                try {
                    processed.addAndGet(consume(queue));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "consumer-" + (i + 1));
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("Всего обработано: " + processed.get()
                + " (ожидалось " + producerCount * perProducer + ")");
    }

    private static void executorDemo() throws InterruptedException {
        System.out.println("=== 2. ExecutorService + именованные потоки ===");
        BlockingQueue<Request> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        int producerCount = 2;
        int consumerCount = 2;
        int perProducer = 10;
        AtomicInteger processed = new AtomicInteger();

        ExecutorService producerPool = Executors.newFixedThreadPool(
                producerCount, new NamedThreadFactory("producer"));
        ExecutorService consumerPool = Executors.newFixedThreadPool(
                consumerCount, new NamedThreadFactory("consumer"));

        try {
            for (int i = 0; i < producerCount; i++) {
                int producerId = i + 1;
                producerPool.execute(() -> {
                    try {
                        produce(queue, producerId, perProducer);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
            for (int i = 0; i < consumerCount; i++) {
                consumerPool.execute(() -> {
                    try {
                        processed.addAndGet(consume(queue));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        } finally {
            shutdownAndAwait(producerPool);
            shutdownAndAwait(consumerPool);
        }

        System.out.println("Всего обработано: " + processed.get()
                + " (ожидалось " + producerCount * perProducer + ")");
    }

    private static void produce(BlockingQueue<Request> queue, int producerId, int count)
            throws InterruptedException {
        for (int i = 1; i <= count; i++) {
            Request request = new Request(producerId * 100 + i);
            queue.put(request);
            System.out.println(Thread.currentThread().getName()
                    + " положил " + request + " (в очереди: " + queue.size() + ")");
        }
        queue.put(POISON);
        System.out.println(Thread.currentThread().getName() + " положил POISON");
    }

    private static int consume(BlockingQueue<Request> queue) throws InterruptedException {
        int processed = 0;
        int work = 0;
        while (true) {
            Request request = queue.take();
            if (request == POISON) {
                System.out.println(Thread.currentThread().getName()
                        + " получил POISON и завершился (обработано: " + processed + ")");
                return processed;
            }
            work += process(request);
            processed++;
            System.out.println(Thread.currentThread().getName()
                    + " обработал " + request + " (в очереди: " + queue.size() + ")");
        }
    }

    private static int process(Request request) {
        int result = 0;
        for (int i = 0; i < 5_000; i++) {
            result += i % 7;
        }
        return result + request.id;
    }

    private static void shutdownAndAwait(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Пул не завершился за 5 секунд — вызываем shutdownNow()");
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
