package task5;

import model.Course;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Task5 {
    private Task5() {
    }

    public static Optional<Course> longest(List<Course> courses) {
        return courses.stream()
                .max(Comparator.comparingInt(Course::duration)
                        .thenComparing(Course::code));
    }

    public static String describeLongest(List<Course> courses, Function<Course, String> map) {
        return longest(courses)
                .map(map)
                .orElse("(no courses)");
    }

    public static List<String> codesOfLongestByLevel(List<Course> courses) {
        return courses.stream()
                .collect(Collectors.groupingBy(
                        Course::level,
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(Course::duration)
                                        .thenComparing(Course::code)),
                                opt -> opt.map(Course::code).orElse(""))))
                .entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .map(e -> e.getKey() + "=" + e.getValue())
                .toList();
    }
}
