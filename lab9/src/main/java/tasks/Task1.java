package tasks;

public class Task1 {

    static class Counter {
        private int count = 0;

        void increment() {
            count++;
        }

        synchronized void incrementSyncMethod() {
            count++;
        }

        void incrementSyncBlock() {
            synchronized (this) {
                count++;
            }
        }

        int getCount() {
            return count;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int threadCount = 8;
        int incrementsPerThread = 100_000;
        int expected = threadCount * incrementsPerThread;

        System.out.println("Потоков: " + threadCount
                + ", инкрементов на поток: " + incrementsPerThread);
        System.out.println("Ожидаемое значение: " + expected);

        Counter buggy = new Counter();
        runThreads(threadCount, incrementsPerThread, buggy::increment);
        System.out.println("Без синхронизации:   " + buggy.getCount()
                + " (потеряно " + (expected - buggy.getCount()) + ")");

        Counter method = new Counter();
        runThreads(threadCount, incrementsPerThread, method::incrementSyncMethod);
        System.out.println("synchronized-метод:  " + method.getCount());

        Counter block = new Counter();
        runThreads(threadCount, incrementsPerThread, block::incrementSyncBlock);
        System.out.println("synchronized-блок:   " + block.getCount());
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
