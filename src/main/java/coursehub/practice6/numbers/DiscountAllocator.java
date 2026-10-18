package coursehub.practice6.numbers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DiscountAllocator {
    public static List<BigDecimal> allocate(List<BigDecimal> prices, BigDecimal discount,
                                             RoundingMode mode) {
        Objects.requireNonNull(prices); Objects.requireNonNull(discount); Objects.requireNonNull(mode);
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal p : prices) {
            if (p == null || p.signum() < 0) throw new IllegalArgumentException("Цена должна быть >= 0");
            total = total.add(p);
        }
        BigDecimal d = discount.setScale(2, mode);
        if (d.signum() < 0 || d.compareTo(total) > 0) throw new IllegalArgumentException("Недопустимая скидка");
        if (prices.isEmpty()) {
            if (d.signum() != 0) throw new IllegalArgumentException("Нет позиций");
            return new ArrayList<>();
        }
        if (total.signum() == 0) {
            if (d.signum() != 0) throw new IllegalArgumentException("Нулевая сумма");
            List<BigDecimal> zeros = new ArrayList<>();
            for (int i = 0; i < prices.size(); i++) zeros.add(new BigDecimal("0.00"));
            return zeros;
        }
        List<BigDecimal> result = new ArrayList<>();
        BigDecimal allocated = BigDecimal.ZERO;
        for (BigDecimal p : prices) {
            BigDecimal share = d.multiply(p).divide(total, 2, mode);
            if (share.compareTo(p) > 0) share = p.setScale(2, RoundingMode.DOWN);
            result.add(share); allocated = allocated.add(share);
        }
        BigDecimal rest = d.subtract(allocated), cent = new BigDecimal("0.01");
        while (rest.signum() > 0) {
            boolean changed = false;
            for (int i = 0; i < prices.size() && rest.signum() > 0; i++) {
                BigDecimal next = result.get(i).add(cent);
                BigDecimal limit = prices.get(i).setScale(2, RoundingMode.DOWN);
                if (next.compareTo(limit) <= 0) {
                    result.set(i, next); rest = rest.subtract(cent); changed = true;
                }
            }
            if (!changed) throw new IllegalArgumentException("Невозможно распределить остаток");
        }
        return result;
    }
}
