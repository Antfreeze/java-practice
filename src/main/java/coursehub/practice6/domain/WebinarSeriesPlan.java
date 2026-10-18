package coursehub.practice6.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

public final class WebinarSeriesPlan {
    private final Instant firstWebinar;
    private final ZoneId organizerZone;
    private final int sessionCount;
    private final Period interval;
    private final LocalDate courseStartDate;
    public WebinarSeriesPlan(Instant firstWebinar, ZoneId organizerZone, int sessionCount, Period interval, LocalDate courseStartDate) {
        this.firstWebinar = Objects.requireNonNull(firstWebinar);
        this.organizerZone = Objects.requireNonNull(organizerZone);
        this.interval = Objects.requireNonNull(interval);
        this.courseStartDate = Objects.requireNonNull(courseStartDate);
        if (sessionCount < 1 || interval.isZero() || interval.isNegative()) throw new IllegalArgumentException("Неверный интервал или число занятий");
        this.sessionCount = sessionCount;
    }
    public Instant getFirstWebinar() { return firstWebinar; }
    public ZoneId getOrganizerZone() { return organizerZone; }
    public int getSessionCount() { return sessionCount; }
    public Period getInterval() { return interval; }
    public LocalDate getCourseStartDate() { return courseStartDate; }
    public ZonedDateTime getFirstWebinarInOrganizerZone() { return firstWebinar.atZone(organizerZone); }

    /**
     * Возвращает полное расписание. Интервал Period применяется к локальному календарному
     * времени организатора, поэтому занятие сохраняет локальные часы при обычном переходе DST.
     * Если вычисленное время попадает в DST-gap, Java сдвигает его вперёд на длину разрыва;
     * при повторённом времени сохраняется смещение предыдущего занятия, если оно допустимо.
     */
    public List<ZonedDateTime> getSchedule() {
        List<ZonedDateTime> schedule = new ArrayList<>();
        ZonedDateTime current = getFirstWebinarInOrganizerZone();
        schedule.add(current);
        for (int i = 1; i < sessionCount; i++) {
            current = current.plus(interval);
            schedule.add(current);
        }
        return List.copyOf(schedule);
    }

    public LocalDate getLastCourseDate() {
        return courseStartDate.plus(interval.multipliedBy(sessionCount - 1));
    }
}
