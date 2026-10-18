package coursehub.practice6.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;

public class PricingService {
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    public Money calculate(CourseOffering offering, Instant purchaseTime, EarlyBirdPolicy policy) {
        Objects.requireNonNull(offering);
        Objects.requireNonNull(purchaseTime);
        Objects.requireNonNull(policy);
        Money price = offering.getPrice();
        if (!policy.isEligible(purchaseTime))
            return price;
        BigDecimal discount = price.getAmount().multiply(policy.getDiscountPercent())
                .divide(new BigDecimal("100"), 2, ROUNDING);
        return new Money(price.getAmount().subtract(discount), price.getCurrency());
    }
}
