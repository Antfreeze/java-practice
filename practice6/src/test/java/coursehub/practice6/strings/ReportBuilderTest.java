package coursehub.practice6.strings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReportBuilderTest {
    @Test
    void sameResult() {
        assertEquals(ReportBuilder.buildWithPlus(10), ReportBuilder.buildWithBuilder(10));
    }

    @Test
    void emptyResult() {
        assertEquals("", ReportBuilder.buildWithBuilder(0));
    }
}
