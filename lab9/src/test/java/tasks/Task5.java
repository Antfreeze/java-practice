package tasks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Task5 {

    private static final int THREADS = 8;
    private static final int INCREMENTS = 50_000;
    private static final int EXPECTED = THREADS * INCREMENTS;

    @Test
    @Timeout(10)
    void synchronizedMethodKeepsAllIncrements() throws Exception {
        Task1.Counter counter = new Task1.Counter();
        int actual = runTogether(counter::incrementSyncMethod, counter::getCount);
        assertEquals(EXPECTED, actual);
    }

    @Test
    @Timeout(10)
    void synchronizedBlockKeepsAllIncrements() throws Exception {
        Task1.Counter counter = new Task1.Counter();
        int actual = runTogether(counter::incrementSyncBlock, counter::getCount);
        assertEquals(EXPECTED, actual);
    }

    @Test
    @Timeout(10)
    void buggyCounterLosesIncrementsRepeatedly() throws Exception {
        int rounds = 5;
        int lostRounds = 0;
        for (int round = 1; round <= rounds; round++) {
            Task1.Counter counter = new Task1.Counter();
            int actual = runTogether(counter::increment, counter::getCount);
            System.out.println("Раунд " + round + ": " + actual + " из " + EXPECTED);
            assertTrue(actual <= EXPECTED);
            if (actual < EXPECTED) {
                lostRounds++;
            }
        }
        System.out.println("Раундов с потерями: " + lostRounds + " из " + rounds);
        assertTrue(lostRounds > 0, "Ни в одном раунде не потерялись инкременты");
    }

    private int runTogether(Runnable increment, Supplier<Integer> result) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(THREADS);
        try {
            for (int i = 0; i < THREADS; i++) {
                pool.execute(() -> {
                    try {
                        start.await();
                        for (int j = 0; j < INCREMENTS; j++) {
                            increment.run();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        finish.countDown();
                    }
                });
            }
            start.countDown();
            assertTrue(finish.await(5, TimeUnit.SECONDS), "Потоки не завершились за 5 секунд");
            return result.get();
        } finally {
            pool.shutdown();
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        }
    }
}
