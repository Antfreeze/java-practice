package task7;

import model.EnrollmentEvent;
import java.util.stream.Stream;

public final class CourseHubReport {

    private CourseHubReport() {
    }

    public static Report build(Stream<EnrollmentEvent> events, boolean parallel) {

        Stream<EnrollmentEvent> stream =
                parallel ? events.parallel() : events.sequential();

        ReportAccumulator acc = stream.collect(
                ReportAccumulator::new,
                ReportAccumulator::accept,
                ReportAccumulator::merge
        );

        return acc.finish();
    }
}
