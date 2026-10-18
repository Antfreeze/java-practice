package coursehub.practice6.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public final class EarlyBirdPolicy {
    private final Instant deadline;
    private final BigDecimal discountPercent;
    public EarlyBirdPolicy(Instant deadline, BigDecimal discountPercent) {
        this.deadline = Objects.requireNonNull(deadline);
        this.discountPercent = Objects.requireNonNull(discountPercent);
        if (discountPercent.signum() < 0 || discountPercent.compareTo(new BigDecimal("100")) > 0)
            throw new IllegalArgumentException("Скидка должна быть от 0 до 100");
    }
    public Instant getDeadline() { return deadline; }
    public BigDecimal getDiscountPercent() { return discountPercent; }
    public boolean isEligible(Instant purchaseTime) { return purchaseTime.isBefore(deadline); }
}
