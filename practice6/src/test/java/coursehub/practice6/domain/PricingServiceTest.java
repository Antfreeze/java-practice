package coursehub.practice6.domain;

import java.math.*;
import java.time.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PricingServiceTest {
  @Test
  void deadlineIsExclusive() {
    Instant deadline = Instant.parse("2026-10-20T12:00:00Z");
    CourseOffering o = new CourseOffering("JAVA-101", new CourseTitle("Java"), new Money(new BigDecimal("100"), "EUR"),
        new WebinarSeriesPlan(deadline, ZoneId.of("Europe/Berlin"), 4, Period.ofDays(7), LocalDate.of(2028, 2, 29)));
    EarlyBirdPolicy p = new EarlyBirdPolicy(deadline, new BigDecimal("10"));
    PricingService s = new PricingService();
    assertEquals(new Money(new BigDecimal("90"), "EUR"), s.calculate(o, deadline.minusNanos(1), p));
    assertEquals(new Money(new BigDecimal("100"), "EUR"), s.calculate(o, deadline, p));
  }

  @Test
  void scheduleContainsAllSessionsAndKeepsOrganizerWallTimeAcrossDst() {
    WebinarSeriesPlan plan = new WebinarSeriesPlan(
        Instant.parse("2026-03-22T09:00:00Z"), ZoneId.of("Europe/Berlin"), 3,
        Period.ofDays(7), LocalDate.of(2026, 3, 22));
    var schedule = plan.getSchedule();
    assertEquals(3, schedule.size());
    assertEquals(10, schedule.get(0).getHour());
    assertEquals(10, schedule.get(1).getHour());
    assertEquals(10, schedule.get(2).getHour());
    assertEquals(ZoneId.of("Europe/Berlin"), schedule.get(2).getZone());
  }

  @Test
  void purchaseRejectsCurrencyMismatch() {
    CourseOffering o = new CourseOffering("JAVA-101", new CourseTitle("Java"), new Money(new BigDecimal("100"), "EUR"),
        new WebinarSeriesPlan(Instant.parse("2028-02-29T10:00:00Z"), ZoneId.of("Europe/Berlin"), 2, Period.ofDays(7),
            LocalDate.of(2028, 2, 29)));
    assertThrows(IllegalArgumentException.class,
        () -> new Purchase("P", o, new Money(new BigDecimal("100"), "USD"), Instant.now(), 0));
  }
}
