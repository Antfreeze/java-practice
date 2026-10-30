package task7;

import java.util.*;

public record Report(
        Set<Long> uniqueStudents,
        Map<String, CourseStats> courses,
        List<CourseStats> top3,
        Map<String, Long> levelDistribution,
        List<String> anomalies,
        long anomalyCount) {

    public Report {
        uniqueStudents = Collections.unmodifiableSet(new TreeSet<>(uniqueStudents));
        courses = Collections.unmodifiableMap(new TreeMap<>(courses));
        top3 = List.copyOf(top3);
        levelDistribution = Collections.unmodifiableMap(new TreeMap<>(levelDistribution));
        anomalies = List.copyOf(anomalies);
    }

    public record CourseStats(
            String courseCode,
            String level,
            long enrolled,
            long completed,
            double completionRate) {
    }

    public void print() {
        System.out.println("=== CourseHub Report ===");
        System.out.println("Уникальных студентов: " + uniqueStudents.size());

        System.out.println("\nКурсы:");
        courses.values().forEach(System.out::println);

        System.out.println("\nTop-3 курсов:");
        top3.forEach(System.out::println);

        System.out.println("\nРаспределение по уровням:");
        levelDistribution.forEach((level, count) -> System.out.println(level + ": " + count));

        System.out.println("\nАномалии (всего " + anomalyCount + ", показано до "
                + anomalies.size() + "):");
        anomalies.forEach(System.out::println);
    }
}