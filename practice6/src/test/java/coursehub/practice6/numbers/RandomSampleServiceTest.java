package coursehub.practice6.numbers;

import java.util.Random;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RandomSampleServiceTest {
    @Test
    void seedRepeats() {
        assertEquals(RandomSampleService.generate(new Random(42), 10, 1, 100),
                RandomSampleService.generate(new Random(42), 10, 1, 100));
    }

    @Test
    void statsWork() {
        SampleStats s = SampleStats.calculate(List.of(2, 4, 6));
        assertEquals(2, s.getMin());
        assertEquals(6, s.getMax());
    }
}
