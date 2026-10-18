package coursehub.practice3;

public final class Task01 {
    private Task01() {
    }

    public static void run() {
        Course course = new Course("JAVA-101", "Основы Java", 12);
        CourseFormatter formatter = new CompactCourseFormatter();
        System.out.println(formatter.format(course));
        formatter = new DetailedCourseFormatter();
        System.out.println(formatter.format(course));
    }

    public record Course(String code, String title, int lessons) {
    }

    public interface CourseFormatter {
        String format(Course course);
    }

    public static final class CompactCourseFormatter implements CourseFormatter {
        @Override
        public String format(Course course) {
            return course.code() + ": " + course.title();
        }
    }

    public static final class DetailedCourseFormatter implements CourseFormatter {
        @Override
        public String format(Course course) {
            return "Курс " + course.title() + " (" + course.code() + "), занятий: " + course.lessons();
        }
    }
}
