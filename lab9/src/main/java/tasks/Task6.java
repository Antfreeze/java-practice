package tasks;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public class Task6 {

    static class Course {
        private final int capacity;
        private final Set<String> students = new HashSet<>();
        private int enrolled = 0;

        Course(int capacity) {
            this.capacity = capacity;
        }

        synchronized boolean enroll(String studentId) {
            if (students.contains(studentId)) {
                return false;
            }
            if (enrolled >= capacity) {
                return false;
            }
            students.add(studentId);
            enrolled++;
            return true;
        }

        synchronized int enrolledCount() {
            return enrolled;
        }

        synchronized int size() {
            return students.size();
        }

        synchronized boolean contains(String studentId) {
            return students.contains(studentId);
        }
    }

    static class AtomicCourse {
        private final int capacity;
        private final Set<String> students = ConcurrentHashMap.newKeySet();
        private final AtomicInteger enrolled = new AtomicInteger();

        AtomicCourse(int capacity) {
            this.capacity = capacity;
        }

        boolean enroll(String studentId) {
            if (!students.add(studentId)) {
                return false;
            }
            int places = enrolled.incrementAndGet();
            if (places > capacity) {
                enrolled.decrementAndGet();
                students.remove(studentId);
                return false;
            }
            return true;
        }

        int enrolledCount() {
            return enrolled.get();
        }

        int size() {
            return students.size();
        }

        boolean contains(String studentId) {
            return students.contains(studentId);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int threadCount = 100;
        int capacity = 10;

        System.out.println("=== (а) synchronized на весь enroll ===");
        Course course = new Course(capacity);
        int successes = runEnroll(course::enroll, threadCount);
        System.out.println("Успешных записей: " + successes + " (ожидалось " + capacity + ")");
        System.out.println("Занято мест: " + course.enrolledCount()
                + ", студентов в set: " + course.size());

        System.out.println("=== (б) AtomicInteger + ConcurrentHashMap.newKeySet ===");
        AtomicCourse atomicCourse = new AtomicCourse(capacity);
        int atomicSuccesses = runEnroll(atomicCourse::enroll, threadCount);
        System.out.println("Успешных записей: " + atomicSuccesses
                + " (ожидалось " + capacity + ")");
        System.out.println("Занято мест: " + atomicCourse.enrolledCount()
                + ", студентов в set: " + atomicCourse.size());

        System.out.println("=== Повторная запись одного студента ===");
        Course simple = new Course(5);
        System.out.println("Первый раз ivanov: " + simple.enroll("ivanov"));
        System.out.println("Второй раз ivanov: " + simple.enroll("ivanov"));
        System.out.println("Занято мест: " + simple.enrolledCount());

        AtomicCourse atomic = new AtomicCourse(5);
        System.out.println("Первый раз petrov: " + atomic.enroll("petrov"));
        System.out.println("Второй раз petrov: " + atomic.enroll("petrov"));
        System.out.println("Занято мест: " + atomic.enrolledCount());

        System.out.println("=== Отказ не меняет состояние ===");
        Course full = new Course(1);
        System.out.println("Записали a: " + full.enroll("a"));
        System.out.println("Пытаемся записать b: " + full.enroll("b"));
        System.out.println("Мест: " + full.enrolledCount()
                + ", b в списке: " + full.contains("b"));
    }

    static int runEnroll(Predicate<String> enroll, int threadCount) throws InterruptedException {
        return runEnroll(enroll, threadCount, i -> "student-" + i);
    }

    static int runEnrollSameId(Predicate<String> enroll, int threadCount, String studentId)
            throws InterruptedException {
        return runEnroll(enroll, threadCount, i -> studentId);
    }

    private static int runEnroll(Predicate<String> enroll, int threadCount,
                                 IntFunction<String> idForThread) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger successes = new AtomicInteger();

        try {
            for (int i = 0; i < threadCount; i++) {
                String studentId = idForThread.apply(i);
                pool.execute(() -> {
                    try {
                        start.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    if (enroll.test(studentId)) {
                        successes.incrementAndGet();
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
        return successes.get();
    }
}
