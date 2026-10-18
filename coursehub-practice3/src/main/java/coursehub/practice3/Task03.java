package coursehub.practice3;

public final class Task03 {
    private Task03() {
    }

    public static void run() {
        Task01.Course course = new Task01.Course("JAVA-102", "Коллекции", 8);
        Task01.CourseFormatter[] formatters = {
                new Task01.CompactCourseFormatter(),
                new Task01.DetailedCourseFormatter(),
                new UppercaseCourseFormatter()
        };

        for (Task01.CourseFormatter formatter : formatters) {
            System.out.println(formatter.format(course));
        }
    }

    public static final class UppercaseCourseFormatter implements Task01.CourseFormatter {
        @Override
        public String format(Task01.Course course) {
            return (course.code() + " — " + course.title()).toUpperCase();
        }
    }
}
