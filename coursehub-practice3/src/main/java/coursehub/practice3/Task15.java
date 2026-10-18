package coursehub.practice3;

import java.util.ArrayList;
import java.util.List;

public final class Task15 {
    private Task15() {
    }

    public static void run() {
        EnrollmentContext candidate = new EnrollmentContext("Анна", 20, true, 0, true, true);
        List<EnrollmentRule> individualRules = List.of(
                new AgeRule(),
                new PrerequisiteRule(),
                new RegistrationOpenRule(),
                new ActiveStudentRule(),
                new CapacityRule()
        );
        for (EnrollmentRule rule : individualRules) {
            System.out.println(rule.getClass().getSimpleName() + " -> " + rule.evaluate(candidate));
        }
        EnrollmentRule normalRegistration = RuleCombinators.allOf(individualRules.toArray(new EnrollmentRule[0]));
        EnrollmentService service = new EnrollmentService(normalRegistration);
        System.out.println(service.enroll(candidate));

        EnrollmentRule alternativeAccess = RuleCombinators.anyOf(
                new AgeRule(),
                new SpecialProgramRule()
        );
        System.out.println(new EnrollmentService(alternativeAccess).enroll(candidate));
    }

    public record EnrollmentContext(String studentName, int age, boolean prerequisitesMet,
                                    int freeSeats, boolean registrationOpen, boolean activeStudent) {
    }

    public sealed interface EnrollmentResult permits Accepted, Rejected, WaitListed {
    }

    public record Accepted(String message) implements EnrollmentResult {
    }

    public record Rejected(String reason) implements EnrollmentResult {
    }

    public record WaitListed(String reason) implements EnrollmentResult {
    }

    public interface EnrollmentRule {
        EnrollmentResult evaluate(EnrollmentContext context);
    }

    public static final class AgeRule implements EnrollmentRule {
        @Override
        public EnrollmentResult evaluate(EnrollmentContext context) {
            return context.age() >= 18
                    ? new Accepted("Возраст подходит")
                    : new Rejected("Возраст меньше 18 лет");
        }
    }

    public static final class PrerequisiteRule implements EnrollmentRule {
        @Override
        public EnrollmentResult evaluate(EnrollmentContext context) {
            return context.prerequisitesMet()
                    ? new Accepted("Prerequisites выполнены")
                    : new Rejected("Не выполнены prerequisites");
        }
    }

    public static final class CapacityRule implements EnrollmentRule {
        @Override
        public EnrollmentResult evaluate(EnrollmentContext context) {
            return context.freeSeats() > 0
                    ? new Accepted("Есть свободное место")
                    : new WaitListed("Мест нет, студент добавлен в лист ожидания");
        }
    }

    public static final class RegistrationOpenRule implements EnrollmentRule {
        @Override
        public EnrollmentResult evaluate(EnrollmentContext context) {
            return context.registrationOpen()
                    ? new Accepted("Регистрация открыта")
                    : new Rejected("Регистрация закрыта");
        }
    }

    public static final class ActiveStudentRule implements EnrollmentRule {
        @Override
        public EnrollmentResult evaluate(EnrollmentContext context) {
            return context.activeStudent()
                    ? new Accepted("Статус студента подтверждён")
                    : new Rejected("Студент не активен");
        }
    }

    public static final class SpecialProgramRule implements EnrollmentRule {
        @Override
        public EnrollmentResult evaluate(EnrollmentContext context) {
            return context.prerequisitesMet() && context.registrationOpen()
                    ? new Accepted("Разрешён специальный доступ")
                    : new Rejected("Условия специальной программы не выполнены");
        }
    }

    public static final class AllOfRule implements EnrollmentRule {
        private final List<EnrollmentRule> rules;

        public AllOfRule(List<EnrollmentRule> rules) {
            this.rules = List.copyOf(rules);
        }

        @Override
        public EnrollmentResult evaluate(EnrollmentContext context) {
            List<String> waitingReasons = new ArrayList<>();
            for (EnrollmentRule rule : rules) {
                EnrollmentResult result = rule.evaluate(context);
                switch (result) {
                    case Accepted ignored -> {
                    }
                    case Rejected rejected -> {
                        return rejected;
                    }
                    case WaitListed waitListed -> waitingReasons.add(waitListed.reason());
                }
            }
            if (!waitingReasons.isEmpty()) {
                return new WaitListed(String.join("; ", waitingReasons));
            }
            return new Accepted("Все правила выполнены");
        }
    }

    public static final class AnyOfRule implements EnrollmentRule {
        private final List<EnrollmentRule> rules;

        public AnyOfRule(List<EnrollmentRule> rules) {
            this.rules = List.copyOf(rules);
        }

        @Override
        public EnrollmentResult evaluate(EnrollmentContext context) {
            String waitingReason = null;
            String rejectedReason = "Ни одно правило не выполнено";
            for (EnrollmentRule rule : rules) {
                EnrollmentResult result = rule.evaluate(context);
                switch (result) {
                    case Accepted accepted -> {
                        return accepted;
                    }
                    case Rejected rejected -> rejectedReason = rejected.reason();
                    case WaitListed waitListed -> waitingReason = waitListed.reason();
                }
            }
            if (waitingReason != null) {
                return new WaitListed(waitingReason);
            }
            return new Rejected(rejectedReason);
        }
    }

    public static final class RuleCombinators {
        private RuleCombinators() {
        }

        public static EnrollmentRule allOf(EnrollmentRule... rules) {
            return new AllOfRule(List.of(rules));
        }

        public static EnrollmentRule anyOf(EnrollmentRule... rules) {
            return new AnyOfRule(List.of(rules));
        }
    }

    public static final class EnrollmentService {
        private final EnrollmentRule rule;

        public EnrollmentService(EnrollmentRule rule) {
            this.rule = rule;
        }

        public EnrollmentResult enroll(EnrollmentContext context) {
            return rule.evaluate(context);
        }
    }
}
