package coursehub.practice3;

public final class Task14 {
    private Task14() {
    }

    public static void run() {
        Course course = new Course("Java", 10);
        CourseOffering first = new CourseOffering(course, new OnlineFormat(), new FixedPaymentPolicy(120));
        CourseOffering second = new CourseOffering(course, new HybridFormat(), new SubscriptionPaymentPolicy(20));
        CourseOffering third = new CourseOffering(course, new WorkshopFormat(), new InstallmentPaymentPolicy(8));

        System.out.println(first.describe());
        System.out.println(second.describe());
        System.out.println(third.describe());
    }

    public record Course(String title, int lessons) {
    }

    public interface CourseFormat {
        String description();
    }

    public static final class OnlineFormat implements CourseFormat {
        @Override
        public String description() {
            return "онлайн";
        }
    }

    public static final class ClassroomFormat implements CourseFormat {
        @Override
        public String description() {
            return "в аудитории";
        }
    }

    public static final class HybridFormat implements CourseFormat {
        @Override
        public String description() {
            return "гибридный";
        }
    }

    public static final class WorkshopFormat implements CourseFormat {
        @Override
        public String description() {
            return "практикум";
        }
    }

    public interface PaymentPolicy {
        double price(Course course);
    }

    public static final class FreePaymentPolicy implements PaymentPolicy {
        @Override
        public double price(Course course) {
            return 0;
        }
    }

    public static final class FixedPaymentPolicy implements PaymentPolicy {
        private final double price;

        public FixedPaymentPolicy(double price) {
            this.price = price;
        }

        @Override
        public double price(Course course) {
            return price;
        }
    }

    public static final class SubscriptionPaymentPolicy implements PaymentPolicy {
        private final double monthlyPrice;

        public SubscriptionPaymentPolicy(double monthlyPrice) {
            this.monthlyPrice = monthlyPrice;
        }

        @Override
        public double price(Course course) {
            return monthlyPrice;
        }
    }

    public static final class InstallmentPaymentPolicy implements PaymentPolicy {
        private final double pricePerLesson;

        public InstallmentPaymentPolicy(double pricePerLesson) {
            this.pricePerLesson = pricePerLesson;
        }

        @Override
        public double price(Course course) {
            return course.lessons() * pricePerLesson;
        }
    }

    public static final class CourseOffering {
        private final Course course;
        private final CourseFormat format;
        private final PaymentPolicy paymentPolicy;

        public CourseOffering(Course course, CourseFormat format, PaymentPolicy paymentPolicy) {
            this.course = course;
            this.format = format;
            this.paymentPolicy = paymentPolicy;
        }

        public String describe() {
            return course.title() + ", формат: " + format.description() + ", цена: " + paymentPolicy.price(course);
        }
    }
}
