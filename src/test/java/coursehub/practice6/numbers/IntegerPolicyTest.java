package coursehub.practice6.numbers;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class IntegerPolicyTest {
 @Test void unboxingNullThrows() { assertThrows(NullPointerException.class, () -> IntegerPolicy.unsafeUnbox(null)); }
 @Test void comparesValues() { assertTrue(IntegerPolicy.sameValue(128, 128)); }
}
