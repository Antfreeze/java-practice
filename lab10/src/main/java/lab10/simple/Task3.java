package lab10.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Task3 {

    record CourseData(int id, String title, int lessons) {
    }

    record LoadResult(int id, CourseData data, Throwable error) {
    }

    public static void main(String[] args) {
        System.out.println("=== Task 3 ===");
        List<Integer> courseIds = List.of(1, 2, 3, 4, 5, 6);

        List<CompletableFuture<CourseData>> futures = courseIds.stream()
                .map(Task3::loadCourseData)
                .toList();
        try {
            CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
            List<CourseData> ordered = new ArrayList<>();
            for (CompletableFuture<CourseData> future : futures) {
                ordered.add(future.join());
            }
            System.out.println("fail-fast result: " + ordered);
        } catch (CompletionException e) {
            System.out.println("fail-fast failed: " + e.getCause());
        }
        List<CompletableFuture<LoadResult>> partial = courseIds.stream()
                .map(id -> loadCourseData(id)
                        .handle((data, error) -> new LoadResult(id, data, error)))
                .toList();
        CompletableFuture.allOf(partial.toArray(CompletableFuture[]::new)).join();

        List<CourseData> ok = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        for (CompletableFuture<LoadResult> future : partial) {
            LoadResult result = future.join();
            if (result.error() == null) {
                ok.add(result.data());
            } else {
                errors.add(result.id() + ": " + result.error().getMessage());
            }
        }
        System.out.println("partial ok: " + ok);
        System.out.println("partial errors: " + errors);
    }

    static CompletableFuture<CourseData> loadCourseData(int id) {
        return CompletableFuture.supplyAsync(() -> {
            sleep((7 - id) * 100L);
            if (id == 4) {
                throw new IllegalStateException("course " + id + " failed");
            }
            return new CourseData(id, "Course " + id, 8 + id);
        });
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
