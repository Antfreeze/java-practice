package task2;

import model.Course;
import java.util.*;
import java.util.stream.*;

public final class Task2 {
    private Task2() {
    }

    public static Set<String> normalizedTopics(List<Course> courses) {
        return courses.stream()
                .flatMap(c -> c.modules().stream())
                .flatMap(m -> m.topics().stream())
                .map(Task2::normalize)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public static Map<String, Set<String>> reverseIndex(List<Course> courses) {
        return courses.stream()
                .flatMap(c -> c.modules().stream()
                        .flatMap(m -> m.topics().stream())
                        .map(Task2::normalize)
                        .filter(s -> !s.isBlank())
                        .map(t -> Map.entry(t, c.code())))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        TreeMap::new,
                        Collectors.mapping(Map.Entry::getValue,
                                Collectors.toCollection(TreeSet::new))));
    }

    static String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }
}
