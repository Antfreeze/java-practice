package model;

public record EnrollmentEvent(long studentId, String courseCode, String level, String type, long ts) {
    public EnrollmentEvent {
        if (type == null)
            throw new IllegalArgumentException("type");
    }
}
