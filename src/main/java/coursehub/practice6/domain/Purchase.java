package coursehub.practice6.domain;

import java.time.Instant;
import java.util.Objects;

public final class Purchase {
    private final String purchaseId;
    private final CourseOffering offering;
    private final Money paidAmount;
    private final Instant purchaseTime;
    private final int completedSessions;
    public Purchase(String purchaseId, CourseOffering offering, Money paidAmount, Instant purchaseTime, int completedSessions) {
        this.purchaseId = Objects.requireNonNull(purchaseId);
        this.offering = Objects.requireNonNull(offering);
        this.paidAmount = Objects.requireNonNull(paidAmount);
        this.purchaseTime = Objects.requireNonNull(purchaseTime);
        if (!offering.getPrice().getCurrency().equals(paidAmount.getCurrency())) throw new IllegalArgumentException("Валюта покупки не совпадает с курсом");
        if (completedSessions < 0 || completedSessions > offering.getPlan().getSessionCount()) throw new IllegalArgumentException("Неверное число завершённых занятий");
        this.completedSessions = completedSessions;
    }
    public String getPurchaseId() { return purchaseId; }
    public CourseOffering getOffering() { return offering; }
    public Money getPaidAmount() { return paidAmount; }
    public Instant getPurchaseTime() { return purchaseTime; }
    public int getCompletedSessions() { return completedSessions; }
}
