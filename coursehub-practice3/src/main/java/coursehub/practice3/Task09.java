package coursehub.practice3;

public final class Task09 {
    private Task09() {
    }

    public static void run() {
        Candidate candidate = new Candidate(20, true, 2);
        EnrollmentService ageService = new EnrollmentService(new AgePolicy());
        EnrollmentService prerequisiteService = new EnrollmentService(new PrerequisitesPolicy());
        EnrollmentService capacityService = new EnrollmentService(new CapacityPolicy());

        System.out.println(ageService.enroll(candidate));
        System.out.println(prerequisiteService.enroll(candidate));
        System.out.println(capacityService.enroll(candidate));
    }

    public record Candidate(int age, boolean prerequisitesMet, int availableSeats) {
    }

    public interface EnrollmentPolicy {
        boolean allows(Candidate candidate);
    }

    public static final class AgePolicy implements EnrollmentPolicy {
        @Override
        public boolean allows(Candidate candidate) {
            return candidate.age() >= 18;
        }
    }

    public static final class PrerequisitesPolicy implements EnrollmentPolicy {
        @Override
        public boolean allows(Candidate candidate) {
            return candidate.prerequisitesMet();
        }
    }

    public static final class CapacityPolicy implements EnrollmentPolicy {
        @Override
        public boolean allows(Candidate candidate) {
            return candidate.availableSeats() > 0;
        }
    }

    public static final class EnrollmentService {
        private final EnrollmentPolicy policy;

        public EnrollmentService(EnrollmentPolicy policy) {
            this.policy = policy;
        }

        public boolean enroll(Candidate candidate) {
            return policy.allows(candidate);
        }
    }
}
