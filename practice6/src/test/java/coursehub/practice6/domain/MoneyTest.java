package coursehub.practice6.domain;
import java.math.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class MoneyTest {
 @Test void rejectsMixedCurrencies() { assertThrows(IllegalArgumentException.class, () -> new Money(new BigDecimal("1"),"EUR").add(new Money(new BigDecimal("1"),"USD"))); }
 @Test void allocationKeepsTotal() {
  Money m=new Money(new BigDecimal("10.00"),"EUR");
  var parts=Money.allocate(m,3,RoundingMode.HALF_UP);
  Money sum=parts.get(0).add(parts.get(1)).add(parts.get(2));
  assertEquals(m,sum);
  assertEquals(new BigDecimal("3.34"),parts.get(0).getAmount());
 }
 @Test void constructorHonorsExplicitRoundingMode() {
  assertEquals(new BigDecimal("1.23"),new Money(new BigDecimal("1.239"),"EUR",RoundingMode.DOWN).getAmount());
  assertEquals(new BigDecimal("1.24"),new Money(new BigDecimal("1.231"),"EUR",RoundingMode.CEILING).getAmount());
 }
 @Test void percentageUsesRequestedRoundingMode() {
  Money amount=new Money(new BigDecimal("10.00"),"EUR");
  assertEquals(new BigDecimal("0.33"),amount.percentage(new BigDecimal("3.333"),RoundingMode.DOWN).getAmount());
 }
}
