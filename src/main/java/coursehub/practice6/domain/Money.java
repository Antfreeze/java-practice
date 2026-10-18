package coursehub.practice6.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Money {
    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_UP;
    private final BigDecimal amount;
    private final String currency;
    public Money(BigDecimal amount, String currency) {
        this(amount, currency, DEFAULT_ROUNDING);
    }

    /** Создаёт денежную сумму с явно выбранным режимом округления до двух знаков. */
    public Money(BigDecimal amount, String currency, RoundingMode mode) {
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(currency, "currency");
        Objects.requireNonNull(mode, "mode");
        if (!currency.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Ожидается код валюты, например EUR");
        }
        this.amount = amount.setScale(2, mode);
        this.currency = currency;
    }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    private void checkCurrency(Money other) {
        Objects.requireNonNull(other);
        if (!currency.equals(other.currency)) throw new IllegalArgumentException("Разные валюты");
    }
    public Money add(Money other) { checkCurrency(other); return new Money(amount.add(other.amount), currency); }
    public Money subtract(Money other) { checkCurrency(other); return new Money(amount.subtract(other.amount), currency); }
    public Money percentage(BigDecimal percent, RoundingMode mode) {
        Objects.requireNonNull(percent); Objects.requireNonNull(mode);
        return new Money(amount.multiply(percent).divide(new BigDecimal("100"), 2, mode), currency);
    }
    public static List<Money> allocate(Money total, int parts, RoundingMode mode) {
        Objects.requireNonNull(total); Objects.requireNonNull(mode);
        if (parts < 1 || total.amount.signum() < 0) throw new IllegalArgumentException("Неверные параметры");
        // Явно применяем переданный режим при переводе суммы в целые копейки.
        long cents = total.amount.setScale(2, mode).movePointRight(2).longValueExact();
        long base = cents / parts, remainder = cents % parts;
        List<Money> result = new ArrayList<>();
        for (int i = 0; i < parts; i++) result.add(new Money(BigDecimal.valueOf(base + (i < remainder ? 1 : 0), 2), total.currency));
        return result;
    }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money m = (Money) o;
        return amount.compareTo(m.amount) == 0 && currency.equals(m.currency);
    }
    @Override public int hashCode() { return Objects.hash(amount.stripTrailingZeros(), currency); }
    @Override public String toString() { return amount.toPlainString() + " " + currency; }
}
