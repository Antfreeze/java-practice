package coursehub.practice6.strings;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class CourseCodeTest {
 @Test void parses() { CourseCode c = new CourseCode("JAVA-101"); assertEquals("JAVA", c.getPrefix()); assertEquals(101, c.getNumber()); }
 @Test void rejectsBadCode() { assertThrows(IllegalArgumentException.class, () -> new CourseCode("xJAVA-101")); }
}
