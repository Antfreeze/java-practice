package coursehub.practice3;

import java.time.Clock;

public final class Task05 {
    private Task05() {
    }

    public static void run() {
        CourseEntity entity = new CourseEntity("C-15", "Java для начинающих");
        Formatter formatter = new SimpleFormatter();
        CoursePolicy policy = age -> age >= 16;
        System.out.println(formatter.format(entity.title()));
        System.out.println("ID: " + entity.id());
        System.out.println("Допуск по возрасту: " + policy.isAllowed(18));
        System.out.println("Текущее время: " + Clock.systemUTC().instant());
    }

    public interface Identifiable {
        String id();
    }

    public interface CoursePolicy {
        boolean isAllowed(int age);
    }

    public abstract static class BaseEntity implements Identifiable {
        private final String id;

        protected BaseEntity(String id) {
            this.id = id;
        }

        @Override
        public final String id() {
            return id;
        }
    }

    public interface Formatter {
        String format(String value);
    }

    public static final class SimpleFormatter implements Formatter {
        @Override
        public String format(String value) {
            return "Курс: " + value;
        }
    }

    public static final class CourseEntity extends BaseEntity {
        private final String title;

        public CourseEntity(String id, String title) {
            super(id);
            this.title = title;
        }

        public String title() {
            return title;
        }
    }
}
