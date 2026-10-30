package task7;

import model.EnrollmentEvent;

import java.util.*;

public final class ReportAccumulator {

    private static final int MAX_ANOMALIES = 1000;

    private final Set<Long> students = new TreeSet<>();
    private final Map<String, CourseData> courses = new TreeMap<>();
    private final TreeSet<String> anomalySamples = new TreeSet<>();
    private long anomalyCount = 0;

    private static final class CourseData {
        final SortedSet<Long> enrolled = new TreeSet<>();
        final SortedSet<Long> completed = new TreeSet<>();
        final SortedSet<String> levels = new TreeSet<>();
    }

    public void accept(EnrollmentEvent event) {
        if (event == null) {
            addAnomaly("NULL_EVENT");
            return;
        }

        students.add(event.studentId());

        if (event.courseCode() == null || event.courseCode().isBlank()) {
            addAnomaly("INVALID_COURSE_CODE");
            return;
        }

        String code = event.courseCode().trim();
        String level = event.level() == null || event.level().isBlank()
                ? "UNKNOWN"
                : event.level().trim().toUpperCase(Locale.ROOT);

        CourseData data = courses.computeIfAbsent(code, k -> new CourseData());

        int before = data.levels.size();
        data.levels.add(level);
        if (before == 1 && data.levels.size() == 2) {
            addAnomaly("CONFLICTING_LEVELS:" + code + ":" + data.levels);
        }

        String type = event.type() == null ? "" : event.type().trim().toUpperCase(Locale.ROOT);

        switch (type) {
            case "ENROLL" -> {
                if (!data.enrolled.add(event.studentId())) {
                    addAnomaly("DUPLICATE_ENROLL:" + code + ":" + event.studentId());
                }
            }
            case "COMPLETE" -> {
                if (!data.completed.add(event.studentId())) {
                    addAnomaly("DUPLICATE_COMPLETE:" + code + ":" + event.studentId());
                }
            }
            case "DROP" -> {
            }
            default -> addAnomaly("UNKNOWN_EVENT_TYPE:" + type);
        }
    }

    public ReportAccumulator merge(ReportAccumulator other) {
        students.addAll(other.students);

        other.courses.forEach((code, otherData) -> {
            CourseData data = courses.computeIfAbsent(code, k -> new CourseData());

            mergeIds(data.enrolled, otherData.enrolled, "DUPLICATE_ENROLL:" + code + ":");
            mergeIds(data.completed, otherData.completed, "DUPLICATE_COMPLETE:" + code + ":");

            int before = data.levels.size();
            data.levels.addAll(otherData.levels);
            if (before == 1 && data.levels.size() >= 2) {
                addAnomaly("CONFLICTING_LEVELS:" + code + ":" + data.levels);
            }
        });

        anomalyCount += other.anomalyCount;
        for (String sample : other.anomalySamples) {
            addSample(sample);
        }

        return this;
    }

    private void mergeIds(SortedSet<Long> target, SortedSet<Long> source, String prefix) {
        for (Long id : source) {
            if (!target.add(id)) {
                addAnomaly(prefix + id);
            }
        }
    }

    private void addAnomaly(String anomaly) {
        anomalyCount++;
        addSample(anomaly);
    }

    private void addSample(String anomaly) {
        anomalySamples.add(anomaly);
        if (anomalySamples.size() > MAX_ANOMALIES) {
            anomalySamples.pollLast();
        }
    }

    public Report finish() {
        Map<String, Report.CourseStats> courseStats = new TreeMap<>();
        Map<String, Long> levelDistribution = new TreeMap<>();

        for (var entry : courses.entrySet()) {
            String code = entry.getKey();
            CourseData data = entry.getValue();

            String level = data.levels.isEmpty() ? "UNKNOWN" : data.levels.first();
            long enrolled = data.enrolled.size();

            long completedWithoutEnroll = data.completed.stream()
                    .filter(id -> !data.enrolled.contains(id))
                    .count();
            if (completedWithoutEnroll > 0) {
                addAnomaly("COMPLETE_WITHOUT_ENROLL:" + code + ":" + completedWithoutEnroll);
            }

            long completed = data.completed.stream()
                    .filter(data.enrolled::contains)
                    .count();
            double rate = enrolled == 0 ? 0.0 : (double) completed / enrolled;

            Report.CourseStats stats = new Report.CourseStats(
                    code, level, enrolled, completed, rate);
            courseStats.put(code, stats);

            levelDistribution.merge(level, 1L, Long::sum);
        }

        List<Report.CourseStats> top3 = courseStats.values().stream()
                .sorted(Comparator.comparingDouble(Report.CourseStats::completionRate).reversed()
                        .thenComparing(Report.CourseStats::courseCode))
                .limit(3)
                .toList();

        return new Report(
                students,
                courseStats,
                top3,
                levelDistribution,
                new ArrayList<>(anomalySamples),
                anomalyCount);
    }
}