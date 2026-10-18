package coursehub.practice6.numbers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

public class RandomSampleService {
    public static List<Integer> generate(RandomGenerator random, int count, int min, int max) {
        if (random == null || count < 1 || min > max || max == Integer.MAX_VALUE)
            throw new IllegalArgumentException("Неверные параметры");
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < count; i++) result.add(random.nextInt(min, max + 1));
        return result;
    }
    public static List<Integer> generateForProduction(int count, int min, int max) {
        return generate(new Random(), count, min, max);
    }
    public static void main(String[] args) {
        List<Integer> sample = generate(new Random(42L), 10, 1, 100);
        System.out.println(sample);
        System.out.println(SampleStats.calculate(sample));
    }
}
