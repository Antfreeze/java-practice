package coursehub.practice6.strings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringIdentityDemoTest {
    @Test
    void comparesText() {
        assertTrue(StringIdentityDemo.sameText(new String("JAVA"), "JAVA"));
    }

    @Test
    void rejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> StringIdentityDemo.sameText(null, "JAVA"));
    }
}
