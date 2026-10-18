package coursehub.practice6.numbers;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DecimalDemoTest {
    @Test
    void sumsPrices() {
        assertEquals(new BigDecimal("269.89"), DecimalDemo.calculateThreeCourses(new BigDecimal("99.90"),
                new BigDecimal("120.00"), new BigDecimal("49.99")));
    }

    @Test
    void valueOfIsDecimalFriendly() {
        assertEquals(new BigDecimal("0.1"), BigDecimal.valueOf(0.1));
    }
}
