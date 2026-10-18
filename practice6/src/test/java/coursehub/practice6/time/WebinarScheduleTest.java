package coursehub.practice6.time;

import java.time.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WebinarScheduleTest {
    @Test
    void periodAndDurationDiffer() {
        ZoneId z = ZoneId.of("Europe/Berlin");
        ZonedDateTime d = ZonedDateTime.of(2026, 3, 28, 12, 0, 0, 0, z);
        assertEquals(12, d.plus(Period.ofDays(1)).getHour());
        assertEquals(13, d.plus(Duration.ofHours(24)).getHour());
    }

    @Test
    void sameInstantInZones() {
        Instant i = Instant.parse("2026-10-16T12:00:00Z");
        assertEquals(i, new WebinarSchedule(i).displayIn(ZoneId.of("Asia/Tokyo")).toInstant());
    }
}
