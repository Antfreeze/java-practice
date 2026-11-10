package tasks;

public class Task2 {

    static class Worker {
        private boolean running = true;
        private long spins = 0;

        void run() {
            while (running) {
                spins++;
            }
        }

        void stop() {
            running = false;
        }
    }

    static class VolatileWorker {
        private volatile boolean running = true;
        private long spins = 0;

        void run() {
            while (running) {
                spins++;
            }
        }

        void stop() {
            running = false;
        }
    }

    static class VolatileCounter {
        private volatile int count = 0;

        void increment() {
            count++;
        }

        int getCount() {
            return count;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 1. Флаг без volatile ===");
        Worker worker = new Worker();
        Thread thread = new Thread(worker::run, "worker-no-volatile");
        thread.setDaemon(true);
        thread.start();
        Thread.sleep(200);
        worker.stop();
        thread.join(1000);
        if (thread.isAlive()) {
            System.out.println("Поток НЕ остановился: изменение флага не видно");
        } else {
            System.out.println("Поток остановился (в этом запуске JIT не закэшировал флаг)");
        }

        System.out.println("=== 2. Флаг с volatile ===");
        VolatileWorker volatileWorker = new VolatileWorker();
        Thread volatileThread = new Thread(volatileWorker::run, "worker-volatile");
        volatileThread.start();
        Thread.sleep(200);
        volatileWorker.stop();
        volatileThread.join(2000);
        if (volatileThread.isAlive()) {
            System.out.println("volatile-поток НЕ остановился (этого быть не должно)");
        } else {
            System.out.println("volatile-поток остановился, запись running = false видна");
        }

        System.out.println("=== 3. volatile не спасает составные операции ===");
        int threadCount = 8;
        int increments = 100_000;
        int expected = threadCount * increments;
        VolatileCounter counter = new VolatileCounter();
        runThreads(threadCount, increments, counter::increment);
        System.out.println("Ожидалось: " + expected
                + ", получено с volatile i++: " + counter.getCount()
                + " (потеряно " + (expected - counter.getCount()) + ")");
    }

    private static void runThreads(int threadCount, int increments, Runnable action)
            throws InterruptedException {
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    action.run();
                }
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
