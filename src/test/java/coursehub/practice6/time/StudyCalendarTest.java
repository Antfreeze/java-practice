package coursehub.practice6.time;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class StudyCalendarTest {
 @Test void leapDay() { assertEquals(LocalDate.of(2028,2,29), StudyCalendar.lastStudyDay(LocalDate.of(2028,2,28), 2)); }
 @Test void leapYear() { assertTrue(StudyCalendar.isLeapYear(LocalDate.of(2028,1,1))); }
}
