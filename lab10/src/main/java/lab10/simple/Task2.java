package lab10.simple;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Task2 {

    record User(long id, String name) {
    }

    record Course(String title, int hours) {
    }

    record Enrollment(Course course, int progress) {
    }

    record CourseCard(User user, List<Enrollment> enrollments) {
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Task 2 ===");

        System.out.println("-- commonPool --");
        CompletableFuture<CourseCard> defaultCard = CompletableFuture.supplyAsync(() -> {
            System.out.println("supplyAsync: " + Thread.currentThread().getName());
            sleep(100);
            return new CourseCard(new User(1, "Anna"), List.of(
                    new Enrollment(new Course("Java", 40), 70)));
        });
        defaultCard.thenApply(Task2::format)
                .thenAccept(text -> {
                    System.out.println("thenAccept: " + Thread.currentThread().getName());
                    System.out.println(text);
                }).join();

        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            System.out.println("-- fixed pool --");
            CompletableFuture<CourseCard> explicitCard = CompletableFuture.supplyAsync(() -> {
                System.out.println("supplyAsync: " + Thread.currentThread().getName());
                sleep(100);
                return new CourseCard(new User(1, "Anna"), List.of(
                        new Enrollment(new Course("Java", 40), 70)));
            }, pool);
            explicitCard.thenApply(Task2::format)
                    .thenAccept(text -> {
                        System.out.println("thenAccept: " + Thread.currentThread().getName());
                        System.out.println(text);
                    }).join();

            System.out.println("-- thenCompose --");
            CompletableFuture<User> userFuture = CompletableFuture.supplyAsync(() -> {
                System.out.println("user: " + Thread.currentThread().getName());
                sleep(100);
                return new User(1, "Anna");
            }, pool);

            CompletableFuture<List<Enrollment>> enrollmentsFuture = userFuture
                    .thenCompose(Task2::loadEnrollmentsForUser);

            CompletableFuture<String> textFuture = userFuture
                    .thenCombine(enrollmentsFuture, CourseCard::new)
                    .thenApply(Task2::format);

            textFuture.thenAccept(text -> {
                System.out.println("result: " + Thread.currentThread().getName());
                System.out.println(text);
            }).join();
        } finally {
            pool.shutdown();
            pool.awaitTermination(2, TimeUnit.SECONDS);
        }
    }

    static CompletableFuture<List<Enrollment>> loadEnrollmentsForUser(User user) {
        // Без явного executor используется ForkJoinPool.commonPool().
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("enrollments: " + Thread.currentThread().getName());
            sleep(150);
            return List.of(
                    new Enrollment(new Course("Java", 40), 70),
                    new Enrollment(new Course("SQL", 24), 90));
        });
    }

    static String format(CourseCard card) {
        System.out.println("thenApply: " + Thread.currentThread().getName());
        StringBuilder text = new StringBuilder(card.user().name() + "\n");
        for (Enrollment enrollment : card.enrollments()) {
            text.append(enrollment.course().title())
                    .append(": ")
                    .append(enrollment.progress())
                    .append("%\n");
        }
        return text.toString();
    }

    static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
