package coursehub.practice6.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class RefundService {
    private final RoundingMode roundingMode;

    public RefundService() {
        this(RoundingMode.HALF_UP);
    }

    public RefundService(RoundingMode roundingMode) {
        this.roundingMode = Objects.requireNonNull(roundingMode, "roundingMode");
    }

    public Money calculateRefund(Purchase purchase) {
        Objects.requireNonNull(purchase, "purchase");
        int total = purchase.getOffering().getPlan().getSessionCount();
        int remaining = total - purchase.getCompletedSessions();
        BigDecimal refund = purchase.getPaidAmount().getAmount()
                .multiply(BigDecimal.valueOf(remaining))
                .divide(BigDecimal.valueOf(total), 2, roundingMode);
        return new Money(refund, purchase.getPaidAmount().getCurrency(), roundingMode);
    }
}
