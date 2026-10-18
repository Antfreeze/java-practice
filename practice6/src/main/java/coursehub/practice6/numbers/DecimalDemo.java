package coursehub.practice6.numbers;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalDemo {
    public static BigDecimal calculateThreeCourses(BigDecimal a, BigDecimal b, BigDecimal c) {
        return a.add(b).add(c).setScale(2, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        System.out.println(new BigDecimal(0.1));
        System.out.println(BigDecimal.valueOf(0.1));
        System.out.println(new BigDecimal("0.1"));
        System.out.println(calculateThreeCourses(new BigDecimal("99.90"),
                new BigDecimal("120.00"), new BigDecimal("49.99")));
    }
}
