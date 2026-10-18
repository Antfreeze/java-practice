package coursehub.practice6.time;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DateParserTest {
    @Test
    void parsesDate() {
        assertEquals(LocalDate.of(2028, 2, 29), DateParser.parse("date", "29.02.2028"));
    }

    @Test
    void rejectsInvalidDate() {
        assertThrows(InvalidDateFieldException.class, () -> DateParser.parse("date", "29.02.2027"));
    }
}
