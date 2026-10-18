package coursehub.practice6.numbers;

import java.math.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DiscountAllocatorTest {
    @Test
    void exactTotal() {
        List<BigDecimal> r = DiscountAllocator.allocate(List.of(new BigDecimal("10.00"), new BigDecimal("20.00")),
                new BigDecimal("1.00"), RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("1.00"), r.get(0).add(r.get(1)));
    }

    @Test
    void rejectsTooLargeDiscount() {
        assertThrows(IllegalArgumentException.class, () -> DiscountAllocator.allocate(List.of(new BigDecimal("5.00")),
                new BigDecimal("6.00"), RoundingMode.HALF_UP));
    }

    @Test
    void allocationIsDeterministicAndPreservesDiscountTotal() {
        List<BigDecimal> prices = List.of(new BigDecimal("19.99"), new BigDecimal("30.01"), new BigDecimal("50.00"));
        BigDecimal discount = new BigDecimal("13.37");
        List<BigDecimal> first = DiscountAllocator.allocate(prices, discount, RoundingMode.HALF_UP);
        List<BigDecimal> second = DiscountAllocator.allocate(prices, discount, RoundingMode.HALF_UP);
        assertEquals(first, second);
        assertEquals(0, first.stream().reduce(BigDecimal.ZERO, BigDecimal::add).compareTo(discount));
        for (int i = 0; i < prices.size(); i++)
            assertTrue(first.get(i).compareTo(prices.get(i)) <= 0);
    }
}
