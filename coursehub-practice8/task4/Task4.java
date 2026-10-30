package task4;

import model.Course;
import java.util.*;
import java.util.stream.*;

public final class Task4 {
    private Task4() {
    }

    public static Map<String, LevelSummary> summary(List<Course> courses) {
        return courses.stream()
                .collect(Collectors.groupingBy(
                        Course::level,
                        TreeMap::new,
                        Collectors.teeing(
                                Collectors.counting(),
                                Collectors.averagingInt(Course::duration),
                                LevelSummary::new)));
    }
}
