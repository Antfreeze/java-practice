package coursehub.practice6.time;
import java.time.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class LocalDateTimeResolverTest {
 @Test void rejectsGap() { assertThrows(InvalidLocalTimeException.class, () -> LocalDateTimeResolver.resolve(LocalDateTime.of(2026,3,29,2,30), ZoneId.of("Europe/Berlin"), false)); }
 @Test void overlapHasTwoInstants() { LocalDateTime l=LocalDateTime.of(2026,10,25,2,30); ZoneId z=ZoneId.of("Europe/Berlin"); assertNotEquals(LocalDateTimeResolver.resolve(l,z,false).toInstant(), LocalDateTimeResolver.resolve(l,z,true).toInstant()); }
}
