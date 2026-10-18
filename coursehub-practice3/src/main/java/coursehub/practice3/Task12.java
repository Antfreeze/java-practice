package coursehub.practice3;

public final class Task12 {
    private Task12() {
    }

    public static void run() {
        EnrollmentResult[] results = {
                new Accepted("Студент добавлен в группу"),
                new Rejected("Не выполнены prerequisites"),
                new WaitListed(3)
        };

        for (EnrollmentResult result : results) {
            System.out.println(describe(result));
        }
    }

    public sealed interface EnrollmentResult permits Accepted, Rejected, WaitListed {
    }

    public record Accepted(String message) implements EnrollmentResult {
    }

    public record Rejected(String reason) implements EnrollmentResult {
    }

    public record WaitListed(int position) implements EnrollmentResult {
    }

    public static String describe(EnrollmentResult result) {
        return switch (result) {
            case Accepted accepted -> "Успех: " + accepted.message();
            case Rejected rejected -> "Отказ: " + rejected.reason();
            case WaitListed waitListed -> "Лист ожидания, место: " + waitListed.position();
        };
    }
}
