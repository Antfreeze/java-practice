package task7;

import model.EnrollmentEvent;
import java.util.stream.*;

public class Task7Demo {

        private static String levelOf(String code) {
                int n = Integer.parseInt(code.substring(1));
                return switch (n % 3) {
                        case 0 -> "BEGINNER";
                        case 1 -> "INTERMEDIATE";
                        default -> "ADVANCED";
                };
        }

        private static Stream<EnrollmentEvent> realisticEvents() {
                final int students = 5000;
                final int courses = 20;
                return IntStream.range(0, students * courses).boxed().flatMap(p -> {
                        long student = 1 + (p % students);
                        String code = "C" + (p / students);
                        String level = levelOf(code);
                        int courseIdx = p / students;

                        Stream.Builder<EnrollmentEvent> b = Stream.builder();
                        b.add(new EnrollmentEvent(student, code, level, "ENROLL", p * 2L));
                        if (student % (courseIdx + 1) == 0) {
                                b.add(new EnrollmentEvent(student, code, level, "COMPLETE", p * 2L + 1));
                        }
                        return b.build();
                });
        }

        private static Stream<EnrollmentEvent> anomalyEvents() {
                return Stream.of(
                                null,
                                new EnrollmentEvent(1, null, "BEGINNER", "ENROLL", 0),
                                new EnrollmentEvent(1, "", "BEGINNER", "ENROLL", 1),
                                new EnrollmentEvent(1, "C0", "BEGINNER", "ENROLL", 2),
                                new EnrollmentEvent(1, "C0", "BEGINNER", "ENROLL", 3),
                                new EnrollmentEvent(2, "C0", "ADVANCED", "ENROLL", 4),
                                new EnrollmentEvent(3, "C0", "BEGINNER", "COMPLETE", 5),
                                new EnrollmentEvent(4, "C0", "BEGINNER", "WEIRD", 6));
        }

        public static void main(String[] args) {
                Report seq = CourseHubReport.build(realisticEvents(), false);
                Report par = CourseHubReport.build(realisticEvents(), true);

                System.out.println("realistic:");
                System.out.println("equal=" + seq.equals(par));
                System.out.println("students=" + seq.uniqueStudents().size());
                System.out.println("top3=" + seq.top3());
                System.out.println("byLevel=" + seq.levelDistribution());
                System.out.println("anomalyCount=" + seq.anomalyCount());
                System.out.println("anomalies(first5)=" + seq.anomalies().stream().limit(5).toList());

                System.out.println();
                System.out.println("anomalies:");
                Report bad = CourseHubReport.build(anomalyEvents(), false);
                System.out.println("anomalyCount=" + bad.anomalyCount());
                bad.anomalies().forEach(System.out::println);
        }
}