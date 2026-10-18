package coursehub.practice6.time;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class WebinarSchedule {
    private final Instant webinarInstant;
    public WebinarSchedule(Instant webinarInstant) {
        if (webinarInstant == null) throw new IllegalArgumentException("Instant не должен быть null");
        this.webinarInstant = webinarInstant;
    }
    public Instant getWebinarInstant() { return webinarInstant; }
    public ZonedDateTime displayIn(ZoneId zone) { return webinarInstant.atZone(zone); }
    public static void main(String[] args) {
        ZoneId berlin = ZoneId.of("Europe/Berlin");
        ZonedDateTime start = ZonedDateTime.of(2026, 3, 28, 12, 0, 0, 0, berlin);
        System.out.println(start.plus(Period.ofDays(1)));
        System.out.println(start.plus(Duration.ofHours(24)));
        WebinarSchedule schedule = new WebinarSchedule(Instant.parse("2026-10-16T12:00:00Z"));
        System.out.println(schedule.displayIn(ZoneId.of("Europe/Moscow")));
        System.out.println(schedule.displayIn(ZoneId.of("America/New_York")));
        System.out.println(schedule.displayIn(ZoneId.of("Asia/Tokyo")));
    }
}
