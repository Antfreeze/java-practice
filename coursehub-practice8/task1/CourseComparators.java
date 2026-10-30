package task1;

import model.Course;
import java.util.Comparator;

public final class CourseComparators {
    private CourseComparators() {
    }

    public static final Comparator<Course> DETERMINISTIC = Comparator.comparing(Course::level)
            .thenComparingInt(Course::duration)
            .thenComparing(Course::code);
}
