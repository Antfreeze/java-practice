package coursehub.practice3;

import java.util.ArrayList;
import java.util.List;

public final class Task08 {
    private Task08() {
    }

    public static void run() {
        ReadOnlyCourseRepository readOnly = new ReadOnlyCourseRepository(List.of("Java", "SQL"));
        System.out.println(readOnly.findAll());

        InMemoryCourseRepository repository = new InMemoryCourseRepository();
        repository.save("React");
        System.out.println(repository.findAll());
    }

    public interface CourseReader {
        List<String> findAll();
    }

    public interface CourseWriter {
        void save(String courseName);
    }

    public interface CourseRepository extends CourseReader, CourseWriter {
    }

    public static final class ReadOnlyCourseRepository implements CourseReader {
        private final List<String> courses;

        public ReadOnlyCourseRepository(List<String> courses) {
            this.courses = List.copyOf(courses);
        }

        @Override
        public List<String> findAll() {
            return courses;
        }
    }

    public static final class InMemoryCourseRepository implements CourseRepository {
        private final List<String> courses = new ArrayList<>();

        @Override
        public List<String> findAll() {
            return List.copyOf(courses);
        }

        @Override
        public void save(String courseName) {
            courses.add(courseName);
        }
    }
}
