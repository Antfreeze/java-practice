package coursehub.practice6.domain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class CourseTitleTest {
 @Test void sameSearchKey() { assertEquals(new CourseTitle(" Java  Basics ").getSearchKey(), new CourseTitle("JAVA BASICS").getSearchKey()); }
 @Test void rejectsBlank() { assertThrows(IllegalArgumentException.class, () -> new CourseTitle(" ")); }
}
