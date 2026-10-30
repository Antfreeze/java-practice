package task1;

import model.Course;
import java.util.*;

public class Task1Demo {
    public static List<Course> sortDeterministic(Collection<Course> in) {
        return in.stream().sorted(CourseComparators.DETERMINISTIC).toList();
    }

    public static void main(String[] args) {
        Course a = new Course("C3", "t", "BEGINNER", 100, List.of());
        Course b = new Course("C1", "t", "BEGINNER", 100, List.of());
        Course c = new Course("C2", "t", "BEGINNER", 50, List.of());
        Course d = new Course("C0", "t", "ADVANCED", 100, List.of());

        List<Course> in1 = List.of(a, b, c, d);
        List<Course> in2 = new ArrayList<>(in1);
        Collections.reverse(in2);

        List<Course> r1 = sortDeterministic(in1);
        List<Course> r2 = sortDeterministic(in2);
        System.out.println("equals=" + r1.equals(r2) + " -> " +
                r1.stream().map(Course::code).toList());
    }
}
