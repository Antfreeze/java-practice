package coursehub.practice6.domain;

import java.math.*;
import java.time.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RefundServiceTest {
    @Test
    void proratesAndRounds() {
        CourseOffering o = new CourseOffering("JAVA-101", new CourseTitle("Java"),
                new Money(new BigDecimal("100"), "EUR"), new WebinarSeriesPlan(Instant.parse("2028-02-29T10:00:00Z"),
                        ZoneId.of("Europe/Berlin"), 3, Period.ofDays(7), LocalDate.of(2028, 2, 29)));
        Purchase p = new Purchase("P", o, new Money(new BigDecimal("100"), "EUR"),
                Instant.parse("2028-02-01T10:00:00Z"), 1);
        assertEquals(new Money(new BigDecimal("66.67"), "EUR"), new RefundService().calculateRefund(p));
    }

    @Test
    void refundSupportsExplicitRoundingMode() {
        CourseOffering o = new CourseOffering("JAVA-101", new CourseTitle("Java"),
                new Money(new BigDecimal("10.00"), "EUR"), new WebinarSeriesPlan(Instant.parse("2028-02-29T10:00:00Z"),
                        ZoneId.of("Europe/Berlin"), 3, Period.ofDays(7), LocalDate.of(2028, 2, 29)));
        Purchase p = new Purchase("P", o, new Money(new BigDecimal("10.00"), "EUR"),
                Instant.parse("2028-02-01T10:00:00Z"), 2);
        assertEquals(new Money(new BigDecimal("3.33"), "EUR"), new RefundService(RoundingMode.DOWN).calculateRefund(p));
    }

    @Test
    void completedCourseHasZeroRefund() {
        CourseOffering o = new CourseOffering("JAVA-101", new CourseTitle("Java"),
                new Money(new BigDecimal("100"), "EUR"), new WebinarSeriesPlan(Instant.parse("2028-02-29T10:00:00Z"),
                        ZoneId.of("Europe/Berlin"), 1, Period.ofDays(7), LocalDate.of(2028, 2, 29)));
        Purchase p = new Purchase("P", o, new Money(new BigDecimal("100"), "EUR"), Instant.now(), 1);
        assertEquals(new Money(BigDecimal.ZERO, "EUR"), new RefundService().calculateRefund(p));
    }
}
